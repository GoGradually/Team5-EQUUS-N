package com.feedhanjum.back_end.auth.config.property;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "cors")
@Getter
public class CorsProperties {
    private final List<String> allowedOrigins = new ArrayList<>();
}