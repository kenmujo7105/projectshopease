package com.EcommerceShop.Shop.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressRequest {
    private String province ;
    private Long provinceId ;
    private String district ;
    private Long districtId ;
    private String ward ;
    private Long wardId ;
    private String info ;
    private String phone ;
    private String receiverName ;

    public boolean isIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    private Boolean isDefault ;
}
