package com.feedhanjum.back_end.core.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringEventPublisherImpl implements EventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringEventPublisherImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publishEvent(Object event) {
        applicationEventPublisher.publishEvent(event);
    }
}
