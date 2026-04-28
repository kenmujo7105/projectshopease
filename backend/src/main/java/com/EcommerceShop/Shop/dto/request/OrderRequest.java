package com.EcommerceShop.Shop.dto.request;

import com.EcommerceShop.Shop.enums.PaymentMethod;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderRequest {
    private List<OrderItemRequest> items ;
    private AddressRequest shippingAddress;
    private PaymentMethod paymentMethod;
    private String note; // Optional
    // Getters, setters
}
