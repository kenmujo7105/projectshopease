package com.EcommerceShop.Shop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ghn")
@Data
public class GHNConfig {
    private String token ;
    private String shop ;
    private String baseUrl ;
}
