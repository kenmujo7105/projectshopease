package com.EcommerceShop.Shop.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordChangeRequest {
    private String id ;
    private String password ;
}
