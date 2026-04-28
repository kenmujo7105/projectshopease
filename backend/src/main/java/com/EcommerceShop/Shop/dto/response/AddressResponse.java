package com.EcommerceShop.Shop.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddressResponse {
    private Long id ;
    private String province ;
    private Long provinceId ;
    private String district ;
    private Long districtId ;
    private String ward ;
    private Long wardId ;
    private String info ;
    private String phone ;
    private String receiverName ;
    private boolean isDefault ;
    public boolean isIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
