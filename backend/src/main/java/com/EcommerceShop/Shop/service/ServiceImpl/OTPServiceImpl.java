package com.EcommerceShop.Shop.service.ServiceImpl;

import com.EcommerceShop.Shop.dto.info.OTPInfo;
import com.EcommerceShop.Shop.enums.ErrorCode;
import com.EcommerceShop.Shop.enums.OTPType;
import com.EcommerceShop.Shop.exception.AppException;
import com.EcommerceShop.Shop.service.OTPService;
import com.EcommerceShop.Shop.service.V3Service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {
    private final EmailService emailService ;
    private final RedisTemplate<String, Object> redisTemplate ;
    @Override
    @Async
    public void saveAndSend(String id, String email, OTPType type) {
        // Xóa trong redis nếu đã từng có otp
        String key = "otp:" + id ;
        redisTemplate.delete(key) ;
        // Generate và save
        OTPInfo info = generateOTPInfo(type) ;
        redisTemplate.opsForValue().set(key, info, Duration.ofMinutes(15));
        // Send
        emailService.sendEmail(email, "MÃ OTP GỬI TỪ E-COMMERCE-SHOP", info.getToken());
        // Log
        log.info("Đã gửi mã otp đến email {} : {}",email,info.getToken());
    }

    @Override
    public boolean verify(String id, String otpCode, OTPType type) {
        String key = "otp:" + id ;
        OTPInfo otpInfo = (OTPInfo) redisTemplate.opsForValue().get(key) ;
        if(otpInfo == null) throw new AppException(ErrorCode.BAD_REQUEST);
        if(otpInfo.getCount() <= 0) throw new AppException(ErrorCode.VERIFY_COUNT_NOT_ENOUGH) ;
        if(otpInfo.getToken().equals(otpCode)){
            log.info("Xác thực mã OTP thành công!!!");
            redisTemplate.delete(key) ;
            return true ;
        }
        otpInfo.setCount(otpInfo.getCount() -1 ) ;
        redisTemplate.opsForValue().set(key, otpInfo);
        return false ;
    }

    private OTPInfo generateOTPInfo(OTPType type){
        SecureRandom secureRandom = new SecureRandom();
        int number = secureRandom.nextInt(1_000_000);
        String token = String.format("%06d", number);
        return OTPInfo.builder()
                .token(token)
                .type(type)
                .build();
    }
}
