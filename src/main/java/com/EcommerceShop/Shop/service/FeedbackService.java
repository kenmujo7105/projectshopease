package com.EcommerceShop.Shop.service;

import com.EcommerceShop.Shop.dto.request.FeedbackCreateRequest;
import com.EcommerceShop.Shop.dto.request.FeedbackUpdateRequest;
import com.EcommerceShop.Shop.dto.response.FeedbackDTO;
import com.EcommerceShop.Shop.dto.response.FeedbackFullInfoResponse;
import com.EcommerceShop.Shop.entity.Feedback;
import com.EcommerceShop.Shop.entity.OrderItem;
import com.EcommerceShop.Shop.entity.Product;
import com.EcommerceShop.Shop.entity.User;
import com.EcommerceShop.Shop.enums.ErrorCode;
import com.EcommerceShop.Shop.exception.AppException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public interface FeedbackService {

     FeedbackFullInfoResponse createFeedback(Long productId, FeedbackCreateRequest request);

     List<FeedbackDTO> getListFeedbackByProduct(Long productId, Pageable pageable);

     FeedbackFullInfoResponse update(String feedbackId, FeedbackUpdateRequest request);

     void deleteFeedback(String feedbackId);
}
