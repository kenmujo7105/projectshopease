package com.EcommerceShop.Shop.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemRequest {
    private Long detailId ;
    private Integer num ;
    private String imageUrl ;
    private String name ;
}
