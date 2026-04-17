package com.EcommerceShop.Shop.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ShippingFeeRequest {
    private String to_ward_code ;
    private Long to_district_id ;
    private int weight = 1000 ;
    private int length = 30 ;
    private int width = 20 ;
    private int height = 15 ;
    private int service_type_id = 2 ;
}
