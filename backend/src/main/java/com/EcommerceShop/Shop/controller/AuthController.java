package com.EcommerceShop.Shop.controller;

import com.EcommerceShop.Shop.dto.info.UserCreationRequest;
import com.EcommerceShop.Shop.dto.request.*;
import com.EcommerceShop.Shop.dto.response.UserResponse;
import com.EcommerceShop.Shop.enums.OTPType;
import com.EcommerceShop.Shop.service.AuthService;
import com.EcommerceShop.Shop.dto.ApiResponseWrapper;
import com.EcommerceShop.Shop.dto.response.AuthenticateResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;

    @PostMapping("/register")
    public ApiResponseWrapper<String> register(
            @RequestBody UserCreationRequest request
    ){
        return ApiResponseWrapper.<String>builder()
                .message("Đã gửi OTP vào email đăng kí")
                .data(authService.register(request)).build();
    }
    @PostMapping("/register/verify-otp")
    public ApiResponseWrapper<UserResponse> verifyOtp(
            @RequestBody VerifyOtpRequest verifyOtp
    ){
        return ApiResponseWrapper.<UserResponse>builder()
                .data(authService.verifyAndCreateUser(verifyOtp)).build() ;
    }

    @PostMapping("/register/resend-otp")
    public ApiResponseWrapper<?> resendOtp(
            @RequestBody OTPResendRequest request
    ){
        authService.reSendOtp(request, OTPType.REGISTER);
        return ApiResponseWrapper.builder()
                .message("Đã gửi OTP").build();
    }

    @PostMapping("/login")
    public ApiResponseWrapper<AuthenticateResponse> login(@RequestBody LoginRequest request){
        return ApiResponseWrapper.<AuthenticateResponse>builder()
                .data(authService.login(request)).build();
    }

    @GetMapping("/profile")
    public ApiResponseWrapper<UserResponse> getProfile(){
        return ApiResponseWrapper.<UserResponse>builder()
                .data(authService.getProfile()).build() ;
    }

    @PostMapping("/refresh-token")
    public ApiResponseWrapper<AuthenticateResponse> refresh(@RequestBody RefreshAccessTokenRequest request) throws ParseException {
        return ApiResponseWrapper.<AuthenticateResponse>builder()
                .data(authService.refresh(request)).build();
    }

    @PostMapping("/logout")
    public ApiResponseWrapper<Void> logout(@RequestBody LogoutRequest request){
        authService.logout(request);
        return ApiResponseWrapper.<Void>builder().build();
    }

    @PostMapping("/forgot-password")
    public ApiResponseWrapper<String> forgotPassword(
            // email,
            @RequestBody ForgotPasswordRequest request
    ){
        return ApiResponseWrapper.<String>builder()
                .data(authService.forgotPassword(request)).build();
    }

    @PostMapping("/forgot-password/verify-otp")
    public ApiResponseWrapper<Boolean> forgotVerify(
            @RequestBody VerifyOtpRequest request
    ){

        return ApiResponseWrapper.<Boolean>builder()
                .data(authService.verify(request, OTPType.FORGOT_PASSWORD))
                .build();
    }

    @PostMapping("/forgot-password/change")
    public ApiResponseWrapper<?> changePassword(
            @RequestBody PasswordChangeRequest request
    ){
        authService.forgotChange(request);
        return ApiResponseWrapper.builder().build();
    }

    @PostMapping("/forgot-password/resend-otp")
    public ApiResponseWrapper<?> resendForgotOtp(
            @RequestBody OTPResendRequest resendRequest
    ){
        authService.reSendOtp(resendRequest, OTPType.FORGOT_PASSWORD);
        return ApiResponseWrapper.builder()
                .message("Đã gửi OTP").build();
    }
}
