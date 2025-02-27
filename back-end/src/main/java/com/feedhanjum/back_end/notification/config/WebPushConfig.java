package com.feedhanjum.back_end.notification.config;

import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.PushAsyncService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.security.GeneralSecurityException;
import java.security.Security;

@Slf4j
@Profile({"dev", "prod"})
@Configuration
public class WebPushConfig {

    @Bean
    public PushAsyncService pushAsyncService(WebPushProperty webPushProperty) throws GeneralSecurityException {
        log.info("Initializing PushAsyncService. public key: {}, private key: {}, subject: {}",
                webPushProperty.getVapid().getPublicKey(), webPushProperty.getVapid().getPrivateKey(), webPushProperty.getSubject());
        Security.addProvider(new BouncyCastleProvider());
        return new PushAsyncService(webPushProperty.getVapid().getPublicKey(), webPushProperty.getVapid().getPrivateKey(), webPushProperty.getSubject());
    }

}
