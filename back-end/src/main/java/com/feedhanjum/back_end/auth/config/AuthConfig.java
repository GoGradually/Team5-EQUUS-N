package com.feedhanjum.back_end.auth.config;

import com.feedhanjum.back_end.auth.passwordencoder.BCryptPasswordEncoder;
import com.feedhanjum.back_end.auth.passwordencoder.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
