package com.EcommerceShop.Shop.controller;

import com.EcommerceShop.Shop.dto.request.UpdatePasswordRequest;
import com.EcommerceShop.Shop.dto.info.UserCreationRequest;
import com.EcommerceShop.Shop.dto.request.UserUpdateRequest;
import com.EcommerceShop.Shop.dto.ApiResponseWrapper;
import com.EcommerceShop.Shop.dto.response.UserResponse;
import com.EcommerceShop.Shop.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService ;

    @PostMapping
    ApiResponseWrapper<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request){
        return ApiResponseWrapper.<UserResponse>builder()
                .data(userService.createUser(request))
                .build();
    }

    @GetMapping
    ApiResponseWrapper<List<UserResponse>> getAllUser(){
        return ApiResponseWrapper.<List<UserResponse>>builder()
                .data(userService.getAllUsers()).build();
    }

    @GetMapping("/{userId}")
    ApiResponseWrapper<UserResponse> getUser(@PathVariable String userId){
        return ApiResponseWrapper.<UserResponse>builder()
                .data(userService.getUser(userId)).build();
    }


    @PutMapping("/{userId}")
    ApiResponseWrapper<UserResponse> updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateRequest request){
        return ApiResponseWrapper.<UserResponse>builder()
                .data(userService.updateUser(userId, request))
                .build();
    }

    @PutMapping("/{userId}/password")
    ApiResponseWrapper<?> updatePassword(@PathVariable String userId, @RequestBody UpdatePasswordRequest password){
        userService.updatePassword(userId, password);
        return ApiResponseWrapper.builder()
                .code(200)
                .message("Password update successful!").build();
    }
    // Chưa test
    @DeleteMapping("/{userId}")
    ApiResponseWrapper<?> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return ApiResponseWrapper.builder()
                .code(200)
                .message(String.format("User %s have been deleted", userId)).build();
    }


}
