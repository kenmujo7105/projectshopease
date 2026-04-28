package com.EcommerceShop.Shop.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemCreateRequest {
    private Long productId ;
    private Long detailId ;
    private int num ;
}
