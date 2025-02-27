package com.feedhanjum.back_end.notification.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Profile({"dev", "prod"})
@ConfigurationProperties(prefix = "webpush")
public class WebPushProperty {
    private Vapid vapid;

    private String subject;

    @Getter
    @Setter
    public static class Vapid {
        private String publicKey;
        private String privateKey;
    }
}
