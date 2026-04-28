package com.EcommerceShop.Shop.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {
    private Long id ;
    private String name;
    private String description;
    private String imageUrl;
    private Double averageRate;
    private Long numRate ;
    private List<String> category ;
    private String brand ;
    private List<ProductDetailResponse> productDetail ;
    private double minPrice ;
}
