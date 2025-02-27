package com.feedhanjum.back_end.feedback.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum FeedbackCategory {
    EFFORT("노력"),
    ATTITUDE_AND_POSTURE("태도와 자세"),
    COMMUNICATION("협업 능력");

    @Getter(onMethod_ = @JsonValue)
    private final String description;

    FeedbackCategory(String description) {
        this.description = description;
    }
}
