package com.EcommerceShop.Shop.service;

import com.EcommerceShop.Shop.dto.response.CartResponse;
import com.EcommerceShop.Shop.entity.Cart;
import com.EcommerceShop.Shop.entity.User;
import com.EcommerceShop.Shop.enums.ErrorCode;
import com.EcommerceShop.Shop.exception.AppException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;

public interface CartService {
    Cart create();

    CartResponse getCartByUser(String username);
}
