package com.EcommerceShop.Shop.service.ServiceImpl;

import com.EcommerceShop.Shop.service.AiChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * GroqChatServiceImpl – Integration with Groq API using OpenAI-compatible format.
 */
@Slf4j
@Service
public class GroqChatServiceImpl implements AiChatService {

    private static final String FALLBACK_MESSAGE = "⚠️ Hệ thống AI đang bận, vui lòng thử lại sau ít phút. Bạn cũng có thể liên hệ hotline: 1900-xxxx để được hỗ trợ trực tiếp!";

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String apiUrl;

    public GroqChatServiceImpl(@Value("${ai.groq.api-key}") String apiKey,
                               @Value("${ai.groq.url}") String apiUrl) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String processMessage(String userMessage) {
        log.info("[GroqChat] >>> Nhận tin nhắn: {}", userMessage);

        if (apiKey == null || apiKey.isBlank()) {
            return FALLBACK_MESSAGE;
        }

        try {
            // Build request body for Groq API (OpenAI format)
            Map<String, Object> requestBody = Map.of(
                "model", "llama-3.1-8b-instant",
                "messages", List.of(
                    Map.of("role", "system", "content", "Bạn là trợ lý AI chuyên nghiệp của cửa hàng công nghệ ShopEase. Trả lời bằng tiếng Việt về thiết bị công nghệ và chính sách cửa hàng."),
                    Map.of("role", "user", "content", userMessage)
                ),
                "max_tokens", 1024,
                "temperature", 0.7
            );

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String cleanApiKey = apiKey.replaceAll("^\"|\"$", "");
            headers.setBearerAuth(cleanApiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Make POST request
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // Extract response content
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    if (message != null) {
                        String content = (String) message.get("content");
                        return content != null ? content : FALLBACK_MESSAGE;
                    }
                }
            }

            return FALLBACK_MESSAGE;

        } catch (org.springframework.web.client.RestClientResponseException e) {
            log.error("[GroqChat] ❌ HTTP Error: [{}] {}", e.getStatusCode(), e.getResponseBodyAsString());
            return FALLBACK_MESSAGE;
        } catch (Exception e) {
            log.error("[GroqChat] ❌ Unexpected error: [{}] {}", e.getClass().getSimpleName(), e.getMessage(), e);
            return FALLBACK_MESSAGE;
        }
    }
}