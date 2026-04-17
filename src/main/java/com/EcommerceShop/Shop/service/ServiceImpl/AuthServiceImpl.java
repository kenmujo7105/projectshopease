package com.EcommerceShop.Shop.service.ServiceImpl;

import com.EcommerceShop.Shop.dto.info.ForgotInfo;
import com.EcommerceShop.Shop.dto.info.UserCreationRequest;
import com.EcommerceShop.Shop.dto.request.*;
import com.EcommerceShop.Shop.dto.response.UserResponse;
import com.EcommerceShop.Shop.entity.BlacklistToken;
import com.EcommerceShop.Shop.enums.OTPType;
import com.EcommerceShop.Shop.repository.BlacklistTokenRepository;
import com.EcommerceShop.Shop.dto.response.AuthenticateResponse;
import com.EcommerceShop.Shop.exception.AppException;
import com.EcommerceShop.Shop.enums.ErrorCode;
import com.EcommerceShop.Shop.entity.User;
import com.EcommerceShop.Shop.repository.UserRepository;
import com.EcommerceShop.Shop.service.AuthService;
import com.EcommerceShop.Shop.service.OTPService;
import com.EcommerceShop.Shop.service.UserService;
import com.EcommerceShop.Shop.service.V3Service.EmailService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;


import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    UserRepository userRepository ;
    PasswordEncoder passwordEncoder ;
    BlacklistTokenRepository blacklistTokenRepository ;
    UserService userService ;
    RedisTemplate<String, Object> redisTemplate ;
    EmailService emailService ;
    OTPService otpService ;
    @NonFinal
    @Value("${jwt.access_key}")
    String access_key ;
    @NonFinal
    @Value("${jwt.refresh_key}")
    String refresh_key ;

    public AuthenticateResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)) ;

        boolean authenticate = passwordEncoder.matches(request.getPassword(), user.getPassword()) ;

        if(!authenticate) throw new AppException(ErrorCode.UNAUTHENTICATED) ;

        return AuthenticateResponse.builder()
                .accessToken(generateToken(user,access_key,60, ChronoUnit.MINUTES))
                .refreshToken(generateToken(user,refresh_key,7,ChronoUnit.DAYS)).build();
    }

    public AuthenticateResponse refresh(RefreshAccessTokenRequest request) throws ParseException {
        if(blacklistTokenRepository.findById(request.getToken()).isPresent()){
            throw new AppException(ErrorCode.INVALID_TOKEN) ;
        }
        Pair<SignedJWT, Date> response ;
        try {
            response  =  verifyToken(request.getToken(), true) ;
        } catch (ParseException | JOSEException e){
            throw new AppException(ErrorCode.UNAUTHENTICATED) ;
        }
        User user = userRepository.findByEmail(response.getLeft().getJWTClaimsSet().getSubject()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)) ;
        String access_token = generateToken(user,access_key,60,ChronoUnit.MINUTES) ;
        return AuthenticateResponse.builder().accessToken(access_token).build() ;
    }


    public boolean authenticate(String token) {
        boolean valid = true ;
        try{
            verifyToken(token, false) ;
        }
        catch (Exception e){
            valid = false;
        }
        return valid ;
    }

    public void logout(LogoutRequest request) {
        Pair<SignedJWT,Date> x ;
        try{
            x = verifyToken(request.getToken(), true) ;
        }
        catch (Exception e){
            throw new AppException(ErrorCode.INVALID_TOKEN) ;
        }
        blacklistTokenRepository.save(BlacklistToken.builder()
                .token(request.getToken())
                .expired_date(x.getRight()).build()) ;
    }

    @Override
    public UserResponse getProfile() {
        String userId = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaim("id") ;
        return userService.getUser(userId) ;
    }

    @Override
    public String register(UserCreationRequest request) {
        String  uuid = String.valueOf(UUID.randomUUID());
        String key = "register:" + uuid ;
        otpService.saveAndSend(uuid, request.getEmail() , OTPType.REGISTER);
        redisTemplate.opsForValue().set(key, request,Duration.ofHours(2));
        return uuid ;
    }

    @Override
    public UserResponse verifyAndCreateUser(VerifyOtpRequest verifyOtp) {
        if(otpService.verify(verifyOtp.getUuid(),verifyOtp.getOtp(),OTPType.REGISTER)) {
            String key = "register:" + verifyOtp.getUuid();
            UserCreationRequest request = (UserCreationRequest) redisTemplate.opsForValue().get(key);
            if (request == null) throw new AppException(ErrorCode.REGISTER_SESSION_NOT_FOUND);
            return userService.createUser(request);
        }
        else {
            throw new AppException(ErrorCode.OTP_INVALID) ;
        }
    }

    @Override
    public void reSendOtp(OTPResendRequest resendRequest, OTPType type) {
        String uuid = resendRequest.getId() ;
        String key = "register:" + uuid ;
        UserCreationRequest request = (UserCreationRequest) redisTemplate.opsForValue().get(key) ;
        if(request == null) throw new AppException(ErrorCode.REGISTER_SESSION_NOT_FOUND) ;
        else otpService.saveAndSend(uuid,request.getEmail(), type);
    }

    /**
     * @param email
     * @return
     */
    @Override
    public String forgotPassword(ForgotPasswordRequest request) {
        String email = request.getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)) ;
        String uuid = String.valueOf(UUID.randomUUID());
        otpService.saveAndSend(uuid, email, OTPType.FORGOT_PASSWORD);
        String key = "forgot_pass:" + uuid ;
        ForgotInfo info = ForgotInfo.builder()
                .email(email)
                .valid(false).build();
        redisTemplate.opsForValue().set(key, info, Duration.ofMinutes(15));
        return uuid ;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public boolean verify(VerifyOtpRequest request, OTPType type) {
        String id  = request.getUuid() ;
        String otpCode = request.getOtp();
        if(otpService.verify(id,otpCode, type)){
            String key = "forgot_pass:" + id ;
            ForgotInfo info = (ForgotInfo) redisTemplate.opsForValue().get(key) ;
            if(info == null) throw new AppException(ErrorCode.HET_THOI_GIAN_HIEU_LUC) ;
            info.setValid(true);
            redisTemplate.opsForValue().set(key, info, Duration.ofMinutes(15));
            return true ;
        }
        throw new AppException(ErrorCode.OTP_INVALID) ;
    }

    /**
     * @param id
     * @param password
     */
    @Override
    public void forgotChange(PasswordChangeRequest request) {
        String id = request.getId() ;
        String password = request.getPassword() ;
        String key = "forgot_pass:" + id ;
        ForgotInfo info = (ForgotInfo) redisTemplate.opsForValue().get(key) ;
        if(info == null) throw new AppException(ErrorCode.HET_THOI_GIAN_HIEU_LUC);
        if(info.isValid()){
            User user = userRepository.findByEmail(info.getEmail()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)) ;
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user) ;
        }
        else{
            throw new AppException(ErrorCode.NOT_VERIFY);
        }
    }


    private Pair<SignedJWT,Date> verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = (!isRefresh) ? new MACVerifier(access_key.getBytes()) : new MACVerifier(refresh_key.getBytes()) ;

        SignedJWT signedJWT = SignedJWT.parse(token) ;

        var verify = signedJWT.verify(jwsVerifier) ;
        Date date = signedJWT.getJWTClaimsSet().getExpirationTime() ;
        if(!(verify && date.after(new Date()))){
            throw new AppException(ErrorCode.UNAUTHENTICATED) ;
        }
        return Pair.of(signedJWT,date);
    }

    private String generateToken(User user, String key , int time, ChronoUnit chronoUnit){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512) ;
        JWTClaimsSet jwtClaimsSet1 = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().toString())
                .claim("id", user.getId())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(time, chronoUnit).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .build() ;
        Payload payload = new Payload(jwtClaimsSet1.toJSONObject()) ;

        JWSObject jwsObject = new JWSObject(jwsHeader, payload) ;
        try{
            jwsObject.sign(new MACSigner(key.getBytes())) ;
        }
        catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return jwsObject.serialize() ;
    }


}
