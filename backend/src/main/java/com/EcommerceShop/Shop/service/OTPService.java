package com.EcommerceShop.Shop.service;

import com.EcommerceShop.Shop.enums.OTPType;

public interface OTPService {
    void saveAndSend(String id, String email ,OTPType type) ;

    boolean verify(String id, String otpCode, OTPType type) ;

}
