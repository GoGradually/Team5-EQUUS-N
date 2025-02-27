package com.feedhanjum.back_end.test.config;

import com.feedhanjum.back_end.auth.config.property.GoogleAuthProperties;
import com.feedhanjum.back_end.auth.service.GoogleAuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class GoogleAuthMockConfig {
    @Bean
    public GoogleAuthProperties mockGoogleAuthProperty() {
        return mock();
    }

    @Bean
    public GoogleAuthService mockGoogleAuthService() {
        return mock();
    }
}
