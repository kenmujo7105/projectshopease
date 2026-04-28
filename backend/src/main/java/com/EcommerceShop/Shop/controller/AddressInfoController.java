package com.EcommerceShop.Shop.controller;

import com.EcommerceShop.Shop.service.V3Service.GHNService;
import com.EcommerceShop.Shop.dto.response.DistrictResponse;
import com.EcommerceShop.Shop.dto.response.ProvinceResponse;
import com.EcommerceShop.Shop.dto.response.WardResponse;
import com.EcommerceShop.Shop.dto.ApiResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/address")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class AddressInfoController {
    GHNService ghnService ;

    @GetMapping("/province")
    public ApiResponseWrapper<List<ProvinceResponse>> getProvince(){
        return ApiResponseWrapper.<List<ProvinceResponse>>builder()
                .data(ghnService.getProvince()).build();
    }

    @GetMapping("/{provinceId}/district")
    public ApiResponseWrapper<List<DistrictResponse>> getDistrict(@PathVariable Long provinceId){
        return ApiResponseWrapper.<List<DistrictResponse>>builder().data(ghnService.getDistrict(provinceId)).build() ;
    }

    @GetMapping("{districtId}/ward")
    public ApiResponseWrapper<List<WardResponse>> getWard(@PathVariable Long districtId){
        return ApiResponseWrapper.<List<WardResponse>>builder()
                .data(ghnService.getWard(districtId)).build();
    }
}
