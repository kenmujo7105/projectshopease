package com.EcommerceShop.Shop.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class FeedbackCreateRequest {
    private Integer rate ;

    private String comment ;

}
