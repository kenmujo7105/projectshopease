package com.EcommerceShop.Shop.dto.request;

import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class VerifyOtpRequest {
    private String otp ;
    private String uuid ;
}
