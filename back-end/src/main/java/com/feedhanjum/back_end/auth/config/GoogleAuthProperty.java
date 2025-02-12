package com.feedhanjum.back_end.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Profile({"dev", "prod"})
@ConfigurationProperties("google")
public class GoogleAuthProperty {

    private String clientId;

    private String clientSecret;

    private String redirectUri;
}
