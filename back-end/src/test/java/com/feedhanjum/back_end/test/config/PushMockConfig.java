package com.feedhanjum.back_end.test.config;

import com.feedhanjum.back_end.notification.config.WebPushProperty;
import nl.martijndwars.webpush.PushAsyncService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class PushMockConfig {

    @Bean
    public WebPushProperty webPushProperty() {
        return mock(WebPushProperty.class);
    }

    @Bean
    public PushAsyncService pushAsyncService() {
        return mock();
    }
}
