package com.EcommerceShop.Shop.service;

import com.EcommerceShop.Shop.dto.request.AddressRequest;
import com.EcommerceShop.Shop.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {
    AddressResponse create(String userId, AddressRequest request) ;

    List<AddressResponse> getAll(String userId);

    AddressResponse update(String userId, Long addressId, AddressRequest request);

    void delete(String userId, Long addressId);
}
