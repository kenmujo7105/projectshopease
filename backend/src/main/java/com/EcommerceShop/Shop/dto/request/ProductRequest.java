package com.EcommerceShop.Shop.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Builder
public class ProductRequest {
    private String name;
    private String description;
    private String imageUrl;
    private String brand ;
    private List<ProductDetailRequest> productDetails ;
    private List<String> productCategory ;
}
