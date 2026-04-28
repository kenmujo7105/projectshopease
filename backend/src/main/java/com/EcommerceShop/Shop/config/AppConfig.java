package com.EcommerceShop.Shop.config;

import com.EcommerceShop.Shop.entity.User;
import com.EcommerceShop.Shop.enums.Role;
import com.EcommerceShop.Shop.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//! Từ kênh Youtube devteria
@Slf4j
@Configuration
public class AppConfig {
    @Value("${admin.email}")
    String adminEmail  ;
    @Value("${admin.password}")
    String adminPassword;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            userRepository.findByEmail(adminEmail).ifPresentOrElse(
                admin -> {
                    admin.setPassword(passwordEncoder().encode(adminPassword));
                    userRepository.save(admin);
                },
                () -> {
                    User admin = User.builder()
                            .email(adminEmail)
                            .password(passwordEncoder().encode(adminPassword))
                            .role(Role.ADMIN)
                            .build();
                    userRepository.save(admin);
                }
            );
        };
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10) ;
    }

}
