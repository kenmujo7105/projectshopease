package com.EcommerceShop.Shop.controller;


import com.EcommerceShop.Shop.service.BrandService;
import com.EcommerceShop.Shop.dto.request.BrandRequest;
import com.EcommerceShop.Shop.dto.response.BrandResponse;
import com.EcommerceShop.Shop.dto.ApiResponseWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandController {
    BrandService brandService;

    @PostMapping
    ApiResponseWrapper<BrandResponse> createBrand(@RequestBody BrandRequest request){
        return ApiResponseWrapper.<BrandResponse>builder()
                .data(brandService.create(request)).build();
    }

    @GetMapping("/all")
    ApiResponseWrapper<List<BrandResponse>> getAllBrand(){
        return ApiResponseWrapper.<List<BrandResponse>>builder()
                .data(brandService.getListBrand()).build() ;
    }

    @PutMapping("/{brandId}")
    ApiResponseWrapper<BrandResponse> updateBrand(@PathVariable Long brandId, BrandRequest request){
        return ApiResponseWrapper.<BrandResponse>builder()
                .data(brandService.updateBrand(brandId,request)).build();
    }

    @DeleteMapping("/{brandId}")
    ApiResponseWrapper<?> deleteBrand(@PathVariable Long brandId){
        brandService.deleteBrand(brandId);
        return ApiResponseWrapper.builder()
                .code(200)
                .message("Brand has been deleted!").build();
    }
}
