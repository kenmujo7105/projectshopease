package com.EcommerceShop.Shop.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProvinceResponse {
    private Long ProvinceID ;
    private String ProvinceName ;
}
