package com.EcommerceShop.Shop.service;

/**
 * AiChatService – Interface for AI chat system using Strategy Pattern.
 *
 * Allows switching between different AI providers (Gemini, Groq, etc.) seamlessly.
 */
public interface AiChatService {

    /**
     * Process user message and return AI response.
     *
     * @param userMessage the user's message
     * @return the AI's reply as a string
     */
    String processMessage(String userMessage);
}
