package com.feedhanjum.back_end.schedule.event;

public record RegularFeedbackRequestCreatedEvent(Long receiverId, Long scheduleId) {
}
