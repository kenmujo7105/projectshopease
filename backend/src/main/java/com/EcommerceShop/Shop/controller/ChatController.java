package com.EcommerceShop.Shop.controller;

import com.EcommerceShop.Shop.dto.ApiResponseWrapper;
import com.EcommerceShop.Shop.dto.request.ChatRequest;
import com.EcommerceShop.Shop.dto.response.ChatResponse;
import com.EcommerceShop.Shop.service.AiChatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {

    AiChatService aiChatService;

    @PostMapping
    ApiResponseWrapper<ChatResponse> chat(@RequestBody ChatRequest request) {
        String reply = aiChatService.processMessage(request.getMessage());
        ChatResponse response = ChatResponse.builder()
                .reply(reply)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")))
                .build();
        return ApiResponseWrapper.<ChatResponse>builder()
                .data(response)
                .build();
    }
}
