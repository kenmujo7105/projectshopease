package com.EcommerceShop.Shop.mapper;

import com.EcommerceShop.Shop.dto.info.UserCreationRequest;
import com.EcommerceShop.Shop.dto.request.UserUpdateRequest;
import com.EcommerceShop.Shop.entity.User;
import com.EcommerceShop.Shop.dto.response.UserResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    User toUser(UserCreationRequest request) ;

    UserResponse toUserResponse(User user) ;

    void updateUser(@MappingTarget User user , UserUpdateRequest request) ;
}
