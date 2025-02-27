package com.feedhanjum.back_end.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.member.repository.MemberRepository;
import com.feedhanjum.back_end.notification.config.WebPushProperty;
import com.feedhanjum.back_end.notification.domain.WebPushSubscription;
import com.feedhanjum.back_end.notification.repository.WebPushSubscriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushAsyncService;
import nl.martijndwars.webpush.Subscription;
import org.asynchttpclient.Response;
import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebPushService {
    private final WebPushSubscriptionRepository subscriptionRepository;
    private final MemberRepository memberRepository;
    private final PushAsyncService pushService;
    private final WebPushProperty webPushProperty;
    private final ObjectMapper mapper;

    /**
     * @throws EntityNotFoundException 해당 회원을 찾을 수 없을 때
     */
    @Transactional
    public void subscribe(Long subscriberId, Subscription subscription) {
        Member member = memberRepository.findById(subscriberId)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원을 찾을 수 없습니다."));

        WebPushSubscription webPushSubscription = subscriptionRepository.findBySubscription_Endpoint(subscription.endpoint)
                .orElseGet(() -> new WebPushSubscription(member, subscription));
        webPushSubscription.updateSubscriber(member);

        subscriptionRepository.save(webPushSubscription);
        log.info("Web push subscription saved: {} {}", member.getName(), subscription);
    }


    public String getApplicationServerKey() {
        return webPushProperty.getVapid().getPublicKey();
    }


    @Transactional(readOnly = true)
    public void sendPushMessage(Long subscriberId, Object message) {
        Member subscriber = memberRepository.findById(subscriberId)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원을 찾을 수 없습니다."));
        List<WebPushSubscription> subscriptions = subscriptionRepository.findAllBySubscriber(subscriber);
        for (WebPushSubscription subscription : subscriptions) {
            sendPushMessage(subscription.getSubscription(), message)
                    .thenAcceptAsync(response -> {
                        log.info("Push message sent to {}: {}", subscriber.getName(), response);
                        if (response.getStatusCode() == 410) {
                            // 구독 정보 만료
                            log.info("Subscription deletded. expired: {}", subscription);
                            subscriptionRepository.delete(subscription);
                        }
                    })
                    .exceptionally(e -> {
                        log.info("Failed to send push message to {}", subscriber.getName(), e);
                        return null;
                    });
        }
    }

    private CompletableFuture<Response> sendPushMessage(Subscription subscription, Object message) {
        try {
            Notification notification = new Notification(subscription, mapper.writeValueAsString(message));
            return pushService.send(notification);
        } catch (IOException | JoseException | GeneralSecurityException e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
