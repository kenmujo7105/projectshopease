package com.EcommerceShop.Shop.service.V3Service;

import com.EcommerceShop.Shop.config.GHNConfig;
import com.EcommerceShop.Shop.dto.response.FeeResponse;
import com.EcommerceShop.Shop.exception.AppException;
import com.EcommerceShop.Shop.enums.ErrorCode;
import com.EcommerceShop.Shop.dto.request.GetProvinceRequest;
import com.EcommerceShop.Shop.dto.request.GetWardRequest;
import com.EcommerceShop.Shop.dto.request.ShippingFeeRequest;
import com.EcommerceShop.Shop.dto.response.DistrictResponse;
import com.EcommerceShop.Shop.dto.response.ProvinceResponse;
import com.EcommerceShop.Shop.dto.response.WardResponse;
import com.EcommerceShop.Shop.dto.ApiResponseWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GHNService {
    String token ;
    RedisTemplate<String, Object> redisTemplate ;
    String shopId ;
    WebClient webClient ;

    public GHNService(WebClient.Builder webClient, GHNConfig ghnConfig, RedisTemplate<String, Object> redisTemplate) {
        this.token = ghnConfig.getToken() ;
        this.shopId = ghnConfig.getShop() ;
        this.redisTemplate = redisTemplate;
        this.webClient = webClient.baseUrl(ghnConfig.getBaseUrl()).build();
    }

    /**
     * Lấy danh sách các tỉnh từ API GHN, cache bằng redis.
     * @return List<ProvinceResponse>
     */
    public List<ProvinceResponse> getProvince(){
        // 1. Kiểm tra trong Redis
        String cacheKey = "province:list";
        Object obj = redisTemplate.opsForValue().get(cacheKey);
        List<ProvinceResponse> provinces = new ObjectMapper()
                .convertValue(obj, new TypeReference<List<ProvinceResponse>>() {});
        if (provinces != null) {
            return provinces;
        }
        // 2. Call Api từ Giao hàng nhanh
        ApiResponseWrapper<List<ProvinceResponse>> response = webClient.get()
                .uri("/master-data/province")
                .header("token", token)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(new ParameterizedTypeReference<ApiResponseWrapper<List<ProvinceResponse>>>() {
                }))
                .block() ;
        assert response != null;
        if( response.getCode() != 200){
            throw new AppException(ErrorCode.BAD_REQUEST,response.getMessage()) ;
        }
        // 3. Lưu vào redis
        redisTemplate.opsForValue().set(cacheKey, response.getData(), Duration.ofDays(365));
        return response.getData() ;
    }

    public List<DistrictResponse> getDistrict(Long provinceId){
        // 1. Kiểm tra trong redis
        String key = "district:" + provinceId.toString() + ":list" ;
        Object obj = redisTemplate.opsForValue().get(key) ;
        List<DistrictResponse> data = new ObjectMapper().convertValue(obj, new TypeReference<List<DistrictResponse>>(){}) ;
        if(data != null){
            return data;
        }

        // 2. Call API
        GetProvinceRequest body = GetProvinceRequest.builder()
                .province_id(provinceId).build();
        ApiResponseWrapper<List<DistrictResponse>>  response = webClient.post()
                .uri("/master-data/district")
                .header("token" ,token)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(
                        new ParameterizedTypeReference<ApiResponseWrapper<List<DistrictResponse>>>(){}
                ))
                .block() ;
        assert response != null;
        if(response.getCode() != 200){
            throw new AppException(ErrorCode.BAD_REQUEST,response.getMessage()) ;
        }
        // 3. Lưu cache vào redis
        redisTemplate.opsForValue().set(key,response.getData(),Duration.ofDays(30));
        return response.getData() ;
    }

    public List<WardResponse> getWard(Long districtId){
        // 1. Kiểm tra trong redis
        String key = "ward:" + districtId.toString() + ":list" ;
        Object obj = redisTemplate.opsForValue().get(key) ;
        List<WardResponse> data = new ObjectMapper().convertValue(obj, new TypeReference<List<WardResponse>>() {}) ;
        if(data != null) return data ;

        // 2. Call API
        GetWardRequest body = GetWardRequest.builder()
                .district_id(districtId).build();
        ApiResponseWrapper<List<WardResponse>>  response = webClient.post()
                .uri("/master-data/ward")
                .header("token" ,token)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(
                        new ParameterizedTypeReference<ApiResponseWrapper<List<WardResponse>>>(){}
                ))
                .block() ;
        assert response != null;
        if(response.getCode() != 200){
            throw new AppException(ErrorCode.BAD_REQUEST,response.getMessage()) ;
        }
        // 3. Lưu cache
        redisTemplate.opsForValue().set(key, response.getData() , Duration.ofDays(7));
        return response.getData() ;
    }

    public Long calculateFee(ShippingFeeRequest request){
        // 1. Check redis
        String key = "fee:" + request.getTo_district_id().toString() + ":" + request.getTo_ward_code();
        Object obj = redisTemplate.opsForValue().get(key) ;
        Long fee = new ObjectMapper().convertValue(obj, new TypeReference<Long>() {}) ;
        if(fee != null) return fee ;

        // 2. Call API
        setDefault(request);
        ApiResponseWrapper<FeeResponse> response = webClient.post()
                .uri("/v2/shipping-order/fee")
                .header("token",token)
                .header("ShopId", shopId)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(new ParameterizedTypeReference<ApiResponseWrapper<FeeResponse>>() {
                }))
                .block() ;
        assert response != null ;
        if (response.getCode() != 200) throw new AppException(ErrorCode.BAD_REQUEST, response.getMessage()) ;
        // 3. Lưu redis
        redisTemplate.opsForValue().set(key, response.getData().getTotal(), Duration.ofDays(10));
        return response.getData().getTotal();
    }
    private void setDefault(ShippingFeeRequest request){
        request.setHeight(15);
        request.setWidth(20);
        request.setLength(30);
        request.setWeight(1000);
        request.setService_type_id(2);
    }
}
