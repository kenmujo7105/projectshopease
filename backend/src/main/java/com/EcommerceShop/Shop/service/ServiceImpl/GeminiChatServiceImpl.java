package com.EcommerceShop.Shop.service.ServiceImpl;

import com.EcommerceShop.Shop.entity.Product;
import com.EcommerceShop.Shop.repository.ProductRepository;
import com.EcommerceShop.Shop.service.AiChatService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GeminiChatServiceImpl – Integration with Google Gemini API with Function Calling support.
 */
@Slf4j
@Service
public class GeminiChatServiceImpl implements AiChatService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
    private static final Duration TIMEOUT = Duration.ofSeconds(30);

    private static final String SYSTEM_INSTRUCTION = "Bạn là trợ lý AI chuyên nghiệp của cửa hàng công nghệ ShopEase. "
            + "Bạn trả lời bằng tiếng Việt về thiết bị công nghệ và chính sách cửa hàng (giao hàng, đổi trả, bảo hành, thanh toán). "
            + "ShopEase hỗ trợ giao hàng toàn quốc, miễn phí ship từ 300.000₫, đổi trả 7 ngày, bảo hành 12-24 tháng. "
            + "Khi người dùng hỏi mua một sản phẩm cụ thể hoặc xin link sản phẩm, "
            + "BẠN PHẢI sử dụng công cụ (function) 'search_product_links' để tìm kiếm trong cơ sở dữ liệu thật của cửa hàng. "
            + "Tuyệt đối không bịa đặt link URL. Dùng kết quả trả về từ function để chèn Markdown link (ví dụ: [Tên Sản Phẩm](http://localhost:5173/product/1)) vào câu trả lời.";

    private static final String FALLBACK_MESSAGE = "⚠️ Hệ thống AI đang bận, vui lòng thử lại sau ít phút. Bạn cũng có thể liên hệ hotline: 1900-xxxx để được hỗ trợ trực tiếp!";

    private final WebClient webClient;
    private final String apiKey;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public GeminiChatServiceImpl(@Value("${gemini.api.key}") String apiKey, ProductRepository productRepository) {
        this.apiKey = apiKey;
        this.productRepository = productRepository;
        this.objectMapper = new ObjectMapper();
        this.webClient = WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com")
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                .build();
    }

    @PostConstruct
    public void init() {
        boolean keyLoaded = apiKey != null && !apiKey.isBlank();
        log.info("[GeminiChat] Service initialized. Key present: {}", keyLoaded);
    }

    @Override
    @Transactional(readOnly = true)
    public String processMessage(String userMessage) {
        log.info("[GeminiChat] >>> Nhận tin nhắn: {}", userMessage);

        if (apiKey == null || apiKey.isBlank()) {
            return FALLBACK_MESSAGE;
        }

        try {
            GeminiRequest request = buildRequest(userMessage);
            GeminiResponse response = callGeminiApi(request);

            if (response == null) {
                return FALLBACK_MESSAGE;
            }

            // Kiểm tra xem Gemini có yêu cầu gọi function không
            GeminiResponse.FunctionCall functionCall = extractFunctionCall(response);
            
            if (functionCall != null && "search_product_links".equals(functionCall.name())) {
                log.info("[GeminiChat] ⚙️ AI yêu cầu gọi hàm: search_product_links với tham số: {}", functionCall.args());
                
                String searchQuery = (String) functionCall.args().get("search_query");
                Object maxResultsObj = functionCall.args().get("max_results");
                int maxResults = 3;
                if (maxResultsObj instanceof Number) {
                    maxResults = ((Number) maxResultsObj).intValue();
                }
                
                // Thực thi tìm kiếm trong DB
                List<Map<String, Object>> productResults = executeProductSearch(searchQuery, maxResults);
                
                // Cập nhật cuộc hội thoại: Thêm câu hỏi user -> Thêm Model FunctionCall -> Thêm User FunctionResponse
                List<GeminiRequest.Content> newContents = new ArrayList<>(request.contents());
                
                // Thêm FunctionCall từ Model vào lịch sử
                newContents.add(new GeminiRequest.Content("model", List.of(new GeminiRequest.Part(null, functionCall, null))));
                
                // Thêm FunctionResponse từ hệ thống (đóng vai user gửi lại kết quả)
                Map<String, Object> responseBody = Map.of("products", productResults);
                GeminiRequest.FunctionResponse fResponse = new GeminiRequest.FunctionResponse(functionCall.name(), responseBody);
                newContents.add(new GeminiRequest.Content("user", List.of(new GeminiRequest.Part(null, null, fResponse))));
                
                // Gọi API lần 2 để Gemini gen ra câu trả lời cuối cùng từ dữ liệu function
                GeminiRequest followUpRequest = new GeminiRequest(request.systemInstruction(), newContents, request.generationConfig(), request.tools());
                GeminiResponse finalResponse = callGeminiApi(followUpRequest);
                
                String finalReply = extractText(finalResponse);
                return finalReply;
            }

            // Nếu không gọi function thì trả về text bình thường
            String reply = extractText(response);
            return reply;

        } catch (Exception e) {
            log.error("[GeminiChat] ❌ Unexpected error: [{}] {}", e.getClass().getSimpleName(), e.getMessage(), e);
            return FALLBACK_MESSAGE;
        }
    }

    private List<Map<String, Object>> executeProductSearch(String keyword, int maxResults) {
        if (keyword == null || keyword.isBlank()) return List.of();
        
        List<Product> products = productRepository.suggest(keyword, PageRequest.of(0, maxResults));
        return products.stream().map(p -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", p.getName());
            
            // Lấy giá từ ProductDetail đầu tiên nếu có
            double price = 0;
            if (p.getProductDetails() != null && !p.getProductDetails().isEmpty()) {
                price = p.getProductDetails().get(0).getPrice();
            }
            map.put("price", price);
            map.put("url", "http://localhost:5173/product/" + p.getId());
            return map;
        }).collect(Collectors.toList());
    }

    private GeminiResponse callGeminiApi(GeminiRequest request) throws InterruptedException {
        int maxRetries = 3;
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                return webClient.post()
                        .uri("/v1beta/models/gemini-2.5-flash:generateContent?key={key}", apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(GeminiResponse.class)
                        .timeout(TIMEOUT)
                        .block();
            } catch (WebClientResponseException e) {
                int status = e.getStatusCode().value();
                if ((status == 429 || status == 503) && attempt < maxRetries) {
                    long waitMs = attempt * 2000L;
                    log.warn("[GeminiChat] ⏳ HTTP {} — retry {}/{} sau {}ms", status, attempt, maxRetries, waitMs);
                    Thread.sleep(waitMs);
                } else {
                    log.error("[GeminiChat] ❌ HTTP Error {} from Gemini API", e.getStatusCode());
                    return null;
                }
            }
        }
        return null;
    }

    private GeminiRequest buildRequest(String userMessage) {
        GeminiRequest.Part systemPart = new GeminiRequest.Part(SYSTEM_INSTRUCTION, null, null);
        GeminiRequest.SystemInstruction systemInstruction = new GeminiRequest.SystemInstruction(List.of(systemPart));

        GeminiRequest.Part userPart = new GeminiRequest.Part(userMessage, null, null);
        GeminiRequest.Content userContent = new GeminiRequest.Content("user", List.of(userPart));

        GeminiRequest.GenerationConfig config = new GeminiRequest.GenerationConfig(1024, 0.7, 0.9, 40);

        // Khai báo công cụ search_product_links
        Map<String, GeminiRequest.Schema> properties = new HashMap<>();
        properties.put("search_query", new GeminiRequest.Schema("STRING", "Từ khóa, tên sản phẩm hoặc danh mục cần tìm", null, null));
        properties.put("max_results", new GeminiRequest.Schema("INTEGER", "Số lượng sản phẩm tối đa (mặc định 3)", null, null));

        GeminiRequest.Schema parameters = new GeminiRequest.Schema("OBJECT", null, properties, List.of("search_query"));
        GeminiRequest.FunctionDeclaration funcDecl = new GeminiRequest.FunctionDeclaration("search_product_links", "Tìm kiếm sản phẩm trong database của cửa hàng và trả về tên, giá, link", parameters);
        GeminiRequest.Tool tool = new GeminiRequest.Tool(List.of(funcDecl));

        return new GeminiRequest(systemInstruction, List.of(userContent), config, List.of(tool));
    }

    private String extractText(GeminiResponse response) {
        if (response == null || response.candidates() == null || response.candidates().isEmpty()) return FALLBACK_MESSAGE;
        var candidate = response.candidates().get(0);
        if (candidate.content() == null || candidate.content().parts() == null || candidate.content().parts().isEmpty()) return FALLBACK_MESSAGE;
        
        String text = candidate.content().parts().get(0).text();
        return text != null ? text : FALLBACK_MESSAGE;
    }

    private GeminiResponse.FunctionCall extractFunctionCall(GeminiResponse response) {
        if (response == null || response.candidates() == null || response.candidates().isEmpty()) return null;
        var candidate = response.candidates().get(0);
        if (candidate.content() == null || candidate.content().parts() == null || candidate.content().parts().isEmpty()) return null;
        
        return candidate.content().parts().get(0).functionCall();
    }

    private String fallback() {
        return FALLBACK_MESSAGE;
    }

    // --- DTOs ---

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record GeminiRequest(
            @JsonProperty("system_instruction") SystemInstruction systemInstruction,
            List<Content> contents,
            @JsonProperty("generationConfig") GenerationConfig generationConfig,
            List<Tool> tools) {

        @JsonInclude(JsonInclude.Include.NON_NULL)
        record Part(String text, @JsonProperty("functionCall") GeminiResponse.FunctionCall functionCall, @JsonProperty("functionResponse") FunctionResponse functionResponse) {}

        record Content(String role, List<Part> parts) {}
        record SystemInstruction(List<Part> parts) {}
        record GenerationConfig(@JsonProperty("maxOutputTokens") int maxOutputTokens, double temperature, @JsonProperty("topP") double topP, @JsonProperty("topK") int topK) {}
        
        record Tool(List<FunctionDeclaration> functionDeclarations) {}
        record FunctionDeclaration(String name, String description, Schema parameters) {}
        
        @JsonInclude(JsonInclude.Include.NON_NULL)
        record Schema(String type, String description, Map<String, Schema> properties, List<String> required) {}
        
        record FunctionResponse(String name, Map<String, Object> response) {}
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record GeminiResponse(List<Candidate> candidates) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        record Candidate(Content content, String finishReason) {}
        @JsonIgnoreProperties(ignoreUnknown = true)
        record Content(String role, List<Part> parts) {}
        
        @JsonIgnoreProperties(ignoreUnknown = true)
        record Part(String text, @JsonProperty("functionCall") FunctionCall functionCall) {}
        
        @JsonIgnoreProperties(ignoreUnknown = true)
        record FunctionCall(String name, Map<String, Object> args) {}
    }
}
