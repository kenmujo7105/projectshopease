package com.EcommerceShop.Shop.exception;

import com.EcommerceShop.Shop.enums.ErrorCode;
import lombok.*;

@Getter
@Setter
public class AppException extends RuntimeException {
    private final ErrorCode errorCode ;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    // Constructor có thêm message → nếu null thì dùng message mặc định
    public AppException(ErrorCode errorCode, String customMessage) {
        super(customMessage != null ? customMessage : errorCode.getMessage());
        this.errorCode = errorCode;

    }
    
}
