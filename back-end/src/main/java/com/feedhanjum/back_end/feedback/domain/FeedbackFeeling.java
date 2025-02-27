package com.feedhanjum.back_end.feedback.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public enum FeedbackFeeling {
    POSITIVE("칭찬해요"),
    CONSTRUCTIVE("아쉬워요");

    @Getter(onMethod_ = @JsonValue)
    private final String description;

    FeedbackFeeling(String description) {
        this.description = description;
    }

    public boolean isValidObjectiveFeedback(ObjectiveFeedback objectiveFeedback) {
        return this == objectiveFeedback.getFeeling();
    }

    // 카테고리별 허용된 객관식 피드백 목록 반환
    public List<ObjectiveFeedback> getObjectiveFeedbacks() {
        return Arrays.stream(ObjectiveFeedback.values()).filter(objectiveFeedback -> objectiveFeedback.getFeeling() == this)
                .toList();
    }


    @JsonCreator
    public static FeedbackFeeling fromDescription(String description) {
        for (FeedbackFeeling feeling : values()) {
            if (feeling.description.equals(description)) {
                return feeling;
            }
        }
        throw new IllegalArgumentException("Invalid description: " + description);
    }
}
