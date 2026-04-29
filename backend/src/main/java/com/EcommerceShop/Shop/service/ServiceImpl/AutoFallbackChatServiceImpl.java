package com.EcommerceShop.Shop.service.ServiceImpl;

import com.EcommerceShop.Shop.service.AiChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * AutoFallbackChatServiceImpl - Strategy Manager
 * 
 * Tự động chuyển đổi giữa các nhà cung cấp AI khi có lỗi hoặc quá tải:
 * Thứ tự: Gemini -> Groq -> Mock
 */
@Slf4j
@Primary
@Service
public class AutoFallbackChatServiceImpl implements AiChatService {

    private final AiChatService geminiService;
    private final AiChatService groqService;
    private final AiChatService mockService;

    public AutoFallbackChatServiceImpl(
            @Qualifier("geminiChatServiceImpl") AiChatService geminiService,
            @Qualifier("groqChatServiceImpl") AiChatService groqService,
            @Qualifier("mockChatServiceImpl") AiChatService mockService) {
        this.geminiService = geminiService;
        this.groqService = groqService;
        this.mockService = mockService;
    }

    @Override
    public String processMessage(String userMessage) {
        log.info("[AutoFallback] Bắt đầu gọi Gemini...");
        String reply = geminiService.processMessage(userMessage);

        if (isFallback(reply)) {
            log.warn("[AutoFallback] ⚠️ Gemini báo bận/lỗi. Đang chuyển sang Groq...");
            reply = groqService.processMessage(userMessage);
            
            if (isFallback(reply)) {
                log.warn("[AutoFallback] ⚠️ Groq cũng báo bận/lỗi. Đang chuyển sang Mock (trả lời sẵn)...");
                reply = mockService.processMessage(userMessage);
            } else {
                // Đánh dấu để người dùng biết là câu trả lời từ Groq
                log.info("[AutoFallback] ✅ Groq xử lý thành công!");
                if (!reply.contains("Hệ thống AI đang bận")) {
                     reply = reply + "\n\n*(Phản hồi từ Groq AI)*";
                }
            }
        } else {
            log.info("[AutoFallback] ✅ Gemini xử lý thành công!");
        }
        
        return reply;
    }

    private boolean isFallback(String reply) {
        if (reply == null) return true;
        // Kiểm tra xem phản hồi có phải là câu thông báo lỗi mặc định không
        return reply.contains("Hệ thống AI đang bận") || reply.contains("Cấu hình");
    }
}
