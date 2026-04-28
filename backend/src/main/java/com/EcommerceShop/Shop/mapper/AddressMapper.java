package com.EcommerceShop.Shop.mapper;

import com.EcommerceShop.Shop.entity.Address;
import com.EcommerceShop.Shop.dto.request.AddressRequest;
import com.EcommerceShop.Shop.dto.response.AddressResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

public abstract class AddressMapper {
    @Mapping(source = "isDefault", target = "isDefault")
    public abstract Address toAddress(AddressRequest request) ;

    @Mapping(source = "isDefault", target = "isDefault")
    public abstract AddressResponse toAddressResponse(Address address) ;

    @Mapping(source = "isDefault", target = "isDefault")
    public abstract void update(@MappingTarget Address address, AddressRequest request) ;
}
