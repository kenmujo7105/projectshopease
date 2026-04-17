package com.EcommerceShop.Shop.enums;

import org.springframework.data.jpa.repository.JpaRepository;

public enum OwnerType {
//    USER,
    PRODUCT,
    CATEGORY,
    BRAND

    ;
    public boolean validate(Long ownerId, JpaRepository<?,Long> jpaRepository){
        return  jpaRepository.findById(ownerId).isPresent();
    }
}
