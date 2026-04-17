package com.EcommerceShop.Shop.controller;


import com.EcommerceShop.Shop.service.AddressService;
import com.EcommerceShop.Shop.dto.request.AddressRequest;
import com.EcommerceShop.Shop.dto.response.AddressResponse;
import com.EcommerceShop.Shop.dto.ApiResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/{userId}/address")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AddressController {
    AddressService addressService;

    @PostMapping
    ApiResponseWrapper<AddressResponse> createAddress(@PathVariable String userId, @RequestBody AddressRequest request){
        return ApiResponseWrapper.<AddressResponse>builder()
                .data(addressService.create(userId,request))
                .build();
    }
    @GetMapping
    ApiResponseWrapper<List<AddressResponse>> getAddresses(@PathVariable String userId){
        return ApiResponseWrapper.<List<AddressResponse>>builder()
                .data(addressService.getAll(userId)).build();
    }

    @PutMapping("/{addressId}")
    ApiResponseWrapper<AddressResponse> updateAddress(@PathVariable String userId, @PathVariable Long addressId, @RequestBody AddressRequest request){
        return ApiResponseWrapper.<AddressResponse>builder()
                .data(addressService.update(userId,addressId,request)).build();
    }

    @DeleteMapping("/{addressId}")
    ApiResponseWrapper<?> deleteAddress(@PathVariable String userId, @PathVariable Long addressId){
        addressService.delete(userId, addressId);

        return ApiResponseWrapper.builder()
                .code(200)
                .message("Address has been deleted!").build();
    }
}
