package com.EcommerceShop.Shop.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PaymentMethod {
    CASH,
    VNPAY,
    BANK


    ;
    @JsonCreator
    public static PaymentMethod fromValue(String value) {
        for (PaymentMethod pm : PaymentMethod.values()) {
            if (pm.toString().equals(value.toUpperCase())) {
                return pm;
            }
        }
        throw new IllegalArgumentException("Invalid payment method: " + value);
    }
}
