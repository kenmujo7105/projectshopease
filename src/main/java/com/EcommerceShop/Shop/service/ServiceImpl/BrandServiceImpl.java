package com.EcommerceShop.Shop.service.ServiceImpl;


import com.EcommerceShop.Shop.repository.BrandRepository;
import com.EcommerceShop.Shop.dto.request.BrandRequest;
import com.EcommerceShop.Shop.dto.response.BrandResponse;
import com.EcommerceShop.Shop.entity.Brand;
import com.EcommerceShop.Shop.exception.AppException;
import com.EcommerceShop.Shop.enums.ErrorCode;
import com.EcommerceShop.Shop.service.BrandService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandServiceImpl implements BrandService {
    BrandRepository brandRepository ;

    @PreAuthorize("hasRole('ADMIN')")
    public BrandResponse create(BrandRequest request) {
        Brand brand = Brand.builder()
                .name(request.getName())
                .description(request.getDescription())
                .logoUrl(request.getLogoUrl())
                .build();
        brand = brandRepository.save(brand) ;
        return BrandResponse.builder()
                .id(brand.getId())
                .description(brand.getDescription())
                .logoUrl(brand.getLogoUrl())
                .name(brand.getName())
                .build();
    }


    public List<BrandResponse> getListBrand() {
        return brandRepository.findAll().stream().map(brand -> BrandResponse.builder()
                .name(brand.getName())
                .id(brand.getId())
                .logoUrl(brand.getLogoUrl())
                .description(brand.getDescription()).build()).toList() ;
    }

    @CacheEvict(value = "products", allEntries = true)
    public BrandResponse updateBrand(Long brandId, BrandRequest request){
        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND)) ;
        if (request.getDescription() != null) brand.setDescription(request.getDescription());
        if (request.getLogoUrl() != null) brand.setLogoUrl(request.getLogoUrl());
        if (request.getName() != null) brand.setName(request.getName());
        return BrandResponse.builder()
                .description(brand.getDescription())
                .id(brand.getId())
                .name(brand.getName())
                .logoUrl(brand.getLogoUrl())
                .build() ;
    }

    @CacheEvict(value = "products", allEntries = true)
    public void deleteBrand(Long brandId){
        Brand brand = brandRepository.findById(brandId).orElseThrow(()-> new AppException(ErrorCode.BRAND_NOT_FOUND)) ;
        brandRepository.delete(brand);
    }
}
