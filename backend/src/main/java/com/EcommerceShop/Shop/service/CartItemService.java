package com.EcommerceShop.Shop.service;

import com.EcommerceShop.Shop.dto.request.CartItemCreateRequest;
import com.EcommerceShop.Shop.dto.request.CartItemUpdateRequest;
import com.EcommerceShop.Shop.dto.response.CartItemResponse;
import com.EcommerceShop.Shop.entity.*;
import com.EcommerceShop.Shop.enums.ErrorCode;
import com.EcommerceShop.Shop.exception.AppException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

public interface CartItemService {
    CartItemResponse create(CartItemCreateRequest request);

    void deleteCartItem(Long detailId);

    CartItemResponse update(Long id, CartItemUpdateRequest request);

    List<CartItemResponse> getList();
}
