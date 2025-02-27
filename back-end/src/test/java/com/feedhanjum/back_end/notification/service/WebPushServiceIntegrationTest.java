package com.feedhanjum.back_end.notification.service;

import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.member.repository.MemberRepository;
import com.feedhanjum.back_end.notification.domain.WebPushSubscription;
import com.feedhanjum.back_end.notification.repository.WebPushSubscriptionRepository;
import nl.martijndwars.webpush.Subscription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.feedhanjum.back_end.test.util.DomainTestUtils.createMemberWithoutId;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class WebPushServiceIntegrationTest {
    @Autowired
    private WebPushSubscriptionRepository webPushSubscriptionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WebPushService webPushService;

    @Test
    @DisplayName("endpoint 중복 시 기존 subscription을 업데이트")
    void test1() {
        // given
        Member previousSubscriber = memberRepository.save(createMemberWithoutId("previousSubscriber"));

        Member newSubscriber = memberRepository.save(createMemberWithoutId("newSubscriber"));

        Subscription subscription = new Subscription("https://push-server.com/member1", new Subscription.Keys("key", "auth"));

        webPushSubscriptionRepository.save(new WebPushSubscription(previousSubscriber, subscription));

        // when
        webPushService.subscribe(newSubscriber.getId(), subscription);

        //then
        assertThat(webPushSubscriptionRepository.findAllBySubscriber(previousSubscriber))
                .isEmpty();
        assertThat(webPushSubscriptionRepository.findAllBySubscriber(newSubscriber))
                .hasSize(1)
                .first()
                .satisfies(webPushSubscription -> {
                    assertThat(webPushSubscription.getSubscription().endpoint).isEqualTo(subscription.endpoint);
                });
    }

}