package com.EcommerceShop.Shop.service.ServiceImpl;

import com.EcommerceShop.Shop.dto.response.ChatResponse;
import com.EcommerceShop.Shop.service.AiChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * MockChatServiceImpl – Phản hồi tự động dựa trên từ khóa.
 *
 * Để nâng cấp lên LLM thật:
 *   1. Tạo class mới (ví dụ: GeminiChatServiceImpl) implement ChatService
 *   2. Đánh dấu @Primary trên class mới, hoặc
 *   3. Dùng @ConditionalOnProperty(name="chat.provider", havingValue="gemini")
 *      và thêm chat.provider=gemini vào application.yml
 */
@Slf4j
@Service
public class MockChatServiceImpl implements AiChatService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

    /**
     * Bản đồ từ khóa → phản hồi thông minh.
     * Mỗi entry: list từ khóa trigger → câu trả lời tiếng Việt.
     */
    private static final List<Map.Entry<List<String>, String>> KEYWORD_RESPONSES = List.of(

        Map.entry(List.of("xin chào", "hello", "hi ", "chào", "hey"),
            "Xin chào! 👋 Tôi là trợ lý ảo của ShopEase. Tôi có thể giúp bạn tìm sản phẩm, theo dõi đơn hàng, hoặc trả lời câu hỏi. Bạn cần hỗ trợ gì ạ?"),

        Map.entry(List.of("giá", "bao nhiêu", "price", "cost"),
            "💰 Bạn có thể xem giá chi tiết của từng sản phẩm trên trang sản phẩm. ShopEase cam kết giá cạnh tranh nhất thị trường! Bạn đang quan tâm đến sản phẩm nào?"),

        Map.entry(List.of("giao hàng", "ship", "vận chuyển", "delivery", "freeship"),
            "🚚 ShopEase hỗ trợ giao hàng toàn quốc:\n• Nội thành HCM/HN: 1-2 ngày\n• Tỉnh thành khác: 3-5 ngày\n• Miễn phí vận chuyển cho đơn từ 300.000₫"),

        Map.entry(List.of("đổi trả", "trả hàng", "hoàn", "return", "refund"),
            "🔄 Chính sách đổi trả ShopEase:\n• Đổi trả miễn phí trong 7 ngày\n• Sản phẩm còn nguyên tem, hộp\n• Hoàn tiền trong 3-5 ngày làm việc\nBạn cần đổi trả sản phẩm nào?"),

        Map.entry(List.of("đơn hàng", "order", "tracking", "theo dõi"),
            "📦 Bạn có thể theo dõi đơn hàng bằng cách:\n1. Đăng nhập tài khoản\n2. Vào mục \"Đơn hàng\" trong menu\n3. Xem trạng thái chi tiết từng đơn\nBạn cần kiểm tra đơn hàng nào?"),

        Map.entry(List.of("thanh toán", "payment", "trả tiền", "cod", "momo"),
            "💳 ShopEase hỗ trợ nhiều phương thức thanh toán:\n• COD (thanh toán khi nhận hàng)\n• Chuyển khoản ngân hàng\n• Ví MoMo, ZaloPay\n• Thẻ Visa/Mastercard"),

        Map.entry(List.of("iphone", "macbook", "apple", "airpod"),
            "🍎 ShopEase là đại lý ủy quyền Apple! Chúng tôi có đầy đủ dòng sản phẩm iPhone, MacBook, iPad và AirPods với bảo hành chính hãng 12 tháng. Bạn quan tâm đến sản phẩm nào?"),

        Map.entry(List.of("samsung", "galaxy"),
            "📱 Samsung Galaxy tại ShopEase đều là hàng chính hãng với giá tốt nhất! Từ dòng flagship S24 Ultra đến tầm trung A55. Bạn muốn xem dòng nào?"),

        Map.entry(List.of("laptop", "máy tính"),
            "💻 ShopEase có đa dạng laptop từ nhiều thương hiệu: MacBook, Dell XPS, ASUS ROG, Lenovo ThinkPad... Bạn cần laptop cho công việc, học tập hay gaming?"),

        Map.entry(List.of("gaming", "game", "razer", "rog"),
            "🎮 Gear gaming tại ShopEase: chuột Razer, bàn phím Logitech G Pro, tai nghe Sony... Tất cả đều chính hãng! Bạn đang tìm thiết bị gaming nào?"),

        Map.entry(List.of("khuyến mãi", "giảm giá", "sale", "voucher", "mã"),
            "🎉 Theo dõi trang chủ ShopEase để cập nhật khuyến mãi mới nhất! Hiện tại có nhiều deal hot cho điện thoại và phụ kiện. Bạn muốn xem danh mục nào?"),

        Map.entry(List.of("bảo hành", "warranty", "hỏng", "lỗi"),
            "🛡️ Tất cả sản phẩm tại ShopEase đều có bảo hành chính hãng:\n• Điện thoại: 12 tháng\n• Laptop: 12-24 tháng\n• Phụ kiện: 6-12 tháng\nBạn cần hỗ trợ bảo hành sản phẩm nào?"),

        Map.entry(List.of("cảm ơn", "thanks", "thank"),
            "Không có gì ạ! 😊 Rất vui được hỗ trợ bạn. Nếu cần thêm gì, cứ nhắn cho tôi nhé!"),

        Map.entry(List.of("tạm biệt", "bye", "goodbye"),
            "Tạm biệt bạn! 👋 Chúc bạn mua sắm vui vẻ tại ShopEase. Hẹn gặp lại!")
    );

    private static final String DEFAULT_RESPONSE =
            "Cảm ơn bạn đã nhắn tin! 😊 Tôi là trợ lý ảo của ShopEase. " +
            "Hiện tại tôi có thể hỗ trợ bạn về:\n" +
            "• 🔍 Tìm kiếm sản phẩm\n" +
            "• 📦 Theo dõi đơn hàng\n" +
            "• 🚚 Thông tin giao hàng\n" +
            "• 🔄 Chính sách đổi trả\n" +
            "• 💳 Phương thức thanh toán\n\n" +
            "Hệ thống AI đang được nâng cấp để phục vụ bạn tốt hơn!";

    @Override
    public String processMessage(String userMessage) {
        log.debug("[ChatBot] Nhận tin nhắn: {}", userMessage);

        String lowerMsg = userMessage.toLowerCase().trim();
        String reply = KEYWORD_RESPONSES.stream()
                .filter(entry -> entry.getKey().stream().anyMatch(lowerMsg::contains))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(DEFAULT_RESPONSE);

        return reply;
    }
}
