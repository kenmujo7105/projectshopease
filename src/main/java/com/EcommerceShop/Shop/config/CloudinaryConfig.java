package com.EcommerceShop.Shop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cloud")
@Data
public class CloudinaryConfig {
    String link ;
}
