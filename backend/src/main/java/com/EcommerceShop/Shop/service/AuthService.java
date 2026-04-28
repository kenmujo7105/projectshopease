package com.EcommerceShop.Shop.service;

import com.EcommerceShop.Shop.dto.info.UserCreationRequest;
import com.EcommerceShop.Shop.dto.request.*;
import com.EcommerceShop.Shop.dto.response.AuthenticateResponse;
import com.EcommerceShop.Shop.dto.response.UserResponse;
import com.EcommerceShop.Shop.enums.OTPType;


import java.text.ParseException;


public interface AuthService {
    AuthenticateResponse login(LoginRequest request) ;

    AuthenticateResponse refresh(RefreshAccessTokenRequest request) throws ParseException ;

    boolean authenticate(String token) ;

    void logout(LogoutRequest request) ;

    UserResponse getProfile() ;

    String register(UserCreationRequest request) ;

    UserResponse verifyAndCreateUser(VerifyOtpRequest verifyOtp) ;

    void reSendOtp(OTPResendRequest request, OTPType type) ;

    String forgotPassword(ForgotPasswordRequest request);

    boolean verify(VerifyOtpRequest request, OTPType type) ;

    void forgotChange(PasswordChangeRequest request) ;
}
