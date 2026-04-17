package com.EcommerceShop.Shop.dto.request;

import lombok.*;

@Getter
@Builder
public class LoginRequest {
    private String email;
    private String password ;
    private String role ;
}
