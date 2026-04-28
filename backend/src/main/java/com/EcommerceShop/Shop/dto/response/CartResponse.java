package com.EcommerceShop.Shop.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartResponse {
    String id ;
    String user_id ;
    double totalPrice ;
    int totalQuantity ;
    List<CartItemResponse> cartItems ;
}
