package com.EcommerceShop.Shop.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ForgotPasswordRequest {
    private String email ;
}
