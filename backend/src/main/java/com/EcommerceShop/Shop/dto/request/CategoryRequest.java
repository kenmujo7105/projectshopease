package com.EcommerceShop.Shop.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
public class CategoryRequest {
    private String name ;
    private String description  ;
    private String urlImage ;
}
