package com.EcommerceShop.Shop.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class ProductParamRequest {
    private List<String> category  ;
    private List<String> brand ;

}
