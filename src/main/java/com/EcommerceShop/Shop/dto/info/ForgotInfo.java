package com.EcommerceShop.Shop.dto.info;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ForgotInfo {
    private String email ;
    private boolean valid ;
}
