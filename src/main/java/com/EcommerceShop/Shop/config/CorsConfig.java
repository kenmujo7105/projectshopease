package com.EcommerceShop.Shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // ✅ Cho phép tất cả origin (tạm thời cho dev)
        config.setAllowedOrigins(List.of("http://localhost:5500", "http://localhost:5173"));
        config.setAllowCredentials(true);
        // ✅ Cho phép tất cả method (GET, POST, PUT, DELETE,...)
        config.setAllowedMethods(List.of("*"));

        // ✅ Cho phép tất cả headers từ frontend
        config.setAllowedHeaders(List.of("*"));

        // 🚫 Không set allowCredentials vì đang dùng wildcard origin
        // Nếu sau này bạn muốn hỗ trợ cookie -> cần cụ thể hóa origin

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
