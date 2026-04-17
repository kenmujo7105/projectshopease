package com.EcommerceShop.Shop.dto.info;

import com.EcommerceShop.Shop.enums.OTPType;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OTPInfo {
    @Builder.Default
    private Integer count = 5 ;

    private String token ;

    @Builder.Default
    private boolean valid  = false;

    private OTPType type ;
}
