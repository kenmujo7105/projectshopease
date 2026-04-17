package com.EcommerceShop.Shop.service;

import com.EcommerceShop.Shop.dto.request.UpdatePasswordRequest;
import com.EcommerceShop.Shop.dto.info.UserCreationRequest;
import com.EcommerceShop.Shop.dto.request.UserUpdateRequest;
import com.EcommerceShop.Shop.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreationRequest request) ;

    UserResponse updateUser(String userId, UserUpdateRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUser(String userId) ;

    void deleteUser(String userId) ;

    void updatePassword(String userId , UpdatePasswordRequest request) ;
}
