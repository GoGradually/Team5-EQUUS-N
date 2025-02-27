package com.feedhanjum.back_end.notification.repository;

import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.notification.domain.WebPushSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WebPushSubscriptionRepository extends JpaRepository<WebPushSubscription, Long> {
    List<WebPushSubscription> findAllBySubscriber(Member subscriber);

    Optional<WebPushSubscription> findBySubscription_Endpoint(String subscriptionEndpoint);
}