package com.feedhanjum.back_end.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.mock;

@Configuration
public class MailMockConfig {
    @Bean
    public JavaMailSender mailSender() {
        return mock();
    }
}
