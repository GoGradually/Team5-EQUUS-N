package com.feedhanjum.back_end.notification.domain;

import com.feedhanjum.back_end.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.martijndwars.webpush.Subscription;

@Access(AccessType.FIELD)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class WebPushSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private EmbeddedSubscription subscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id")
    private Member subscriber;


    public void updateSubscriber(Member subscriber) {
        this.subscriber = subscriber;
    }

    public WebPushSubscription(Member subscriber, Subscription subscription) {
        this.subscriber = subscriber;
        this.subscription = EmbeddedSubscription.fromSubscription(subscription);
    }

    public Subscription getSubscription() {
        return subscription.toSubscription();
    }

    // Subscription 객체 Embedded를 위한 중간 매핑 클래스
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    @Embeddable
    public static class EmbeddedSubscription {
        public String endpoint;
        @Embedded
        public EmbeddedKeys keys;

        public static EmbeddedSubscription fromSubscription(Subscription subscription) {
            return new EmbeddedSubscription(subscription.endpoint, EmbeddedKeys.fromKeys(subscription.keys));
        }

        public Subscription toSubscription() {
            return new Subscription(endpoint, keys.toKeys());
        }

        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor
        @Getter
        @Embeddable
        public static class EmbeddedKeys {
            public String p256dh;
            public String auth;

            public static EmbeddedKeys fromKeys(Subscription.Keys keys) {
                return new EmbeddedKeys(keys.p256dh, keys.auth);
            }

            public Subscription.Keys toKeys() {
                return new Subscription.Keys(p256dh, auth);
            }
        }
    }
}
