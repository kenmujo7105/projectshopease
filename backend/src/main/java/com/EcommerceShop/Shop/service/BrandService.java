package com.EcommerceShop.Shop.service;

import com.EcommerceShop.Shop.dto.request.BrandRequest;
import com.EcommerceShop.Shop.dto.response.BrandResponse;
import com.EcommerceShop.Shop.entity.Brand;
import com.EcommerceShop.Shop.enums.ErrorCode;
import com.EcommerceShop.Shop.exception.AppException;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;

public interface BrandService {
    BrandResponse create(BrandRequest request) ;

    List<BrandResponse> getListBrand() ;

    BrandResponse updateBrand(Long brandId, BrandRequest request);

    void deleteBrand(Long brandId);
}
