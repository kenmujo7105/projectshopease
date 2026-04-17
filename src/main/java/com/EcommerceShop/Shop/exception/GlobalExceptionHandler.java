package com.EcommerceShop.Shop.exception;

import com.EcommerceShop.Shop.enums.ErrorCode;
import com.EcommerceShop.Shop.dto.ApiResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponseWrapper<?>> handlingRuntimeException(RuntimeException e){
        log.info(Arrays.toString(e.getStackTrace())) ;
        log.info(e.getMessage());
        return  ResponseEntity.badRequest().body(
                ApiResponseWrapper.builder()
                    .code(ErrorCode.BAD_REQUEST.getCode())
                    .message(e.getMessage() ).build()
        );
    }
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponseWrapper<?>> handlingAccessDeniedException(AccessDeniedException e){
        return ResponseEntity.status(ErrorCode.UNAUTHORIZED.getCode())
                .body(ApiResponseWrapper.builder()
                        .code(ErrorCode.UNAUTHORIZED.getCode())
                        .message(e.getMessage()).build()) ;
    }
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponseWrapper<?>> handlingAppException(AppException exception){
        log.error(Arrays.toString(exception.getStackTrace()));
        return ResponseEntity.status(exception.getErrorCode().getHttpStatus())
                .body(ApiResponseWrapper.builder()
                        .code(exception.getErrorCode().getCode())
                        .message(exception.getMessage()).build()) ;
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseWrapper<?>> handleValidation(MethodArgumentNotValidException ex) {
        String error = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(ErrorCode.BAD_REQUEST.getCode())
                .body(ApiResponseWrapper.builder()
                        .code(ErrorCode.BAD_REQUEST.getCode())
                        .message(error).build()) ;
    }

}
