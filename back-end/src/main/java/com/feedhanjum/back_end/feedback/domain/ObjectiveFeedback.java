package com.feedhanjum.back_end.feedback.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


public enum ObjectiveFeedback {
    LOGICAL(FeedbackFeeling.POSITIVE, FeedbackCategory.EFFORT, "논리적으로 말해요"),
    CREATIVE(FeedbackFeeling.POSITIVE, FeedbackCategory.EFFORT, "창의적이에요"),
    FAST_WORKER(FeedbackFeeling.POSITIVE, FeedbackCategory.EFFORT, "일처리가 빨라요"),
    TOOL_SAVVY(FeedbackFeeling.POSITIVE, FeedbackCategory.EFFORT, "툴 활용에 능숙해요"),

    LISTENER(FeedbackFeeling.POSITIVE, FeedbackCategory.COMMUNICATION, "다른 사람의 의견을 경청해요"),
    LEADERSHIP(FeedbackFeeling.POSITIVE, FeedbackCategory.COMMUNICATION, "리더십이 돋보여요"),
    HELPFUL(FeedbackFeeling.POSITIVE, FeedbackCategory.COMMUNICATION, "많은 도움을 줘요"),
    COLLABORATIVE(FeedbackFeeling.POSITIVE, FeedbackCategory.COMMUNICATION, "협력을 잘해요"),
    DETAIL_ORIENTED(FeedbackFeeling.POSITIVE, FeedbackCategory.COMMUNICATION, "꼼꼼해요"),

    RESPONSIBLE(FeedbackFeeling.POSITIVE, FeedbackCategory.ATTITUDE_AND_POSTURE, "책임감이 있어요"),
    PROACTIVE(FeedbackFeeling.POSITIVE, FeedbackCategory.ATTITUDE_AND_POSTURE, "적극적이에요"),
    POSITIVE(FeedbackFeeling.POSITIVE, FeedbackCategory.ATTITUDE_AND_POSTURE, "긍정적이에요"),
    DILIGENT(FeedbackFeeling.POSITIVE, FeedbackCategory.ATTITUDE_AND_POSTURE, "성실해요"),
    HARDWORKING(FeedbackFeeling.POSITIVE, FeedbackCategory.ATTITUDE_AND_POSTURE, "노력이 느껴져요"),

    LOGICAL_REQUIRED(FeedbackFeeling.CONSTRUCTIVE, FeedbackCategory.EFFORT, "논리적으로 말해주세요"),
    CONCISE_REQUIRED(FeedbackFeeling.CONSTRUCTIVE, FeedbackCategory.EFFORT, "요점 위주로 얘기해주세요"),
    DETAILED_REQUIRED(FeedbackFeeling.CONSTRUCTIVE, FeedbackCategory.EFFORT, "구체적으로 말해주세요"),
    THOROUGH_REQUIRED(FeedbackFeeling.CONSTRUCTIVE, FeedbackCategory.EFFORT, "조금 더 꼼꼼히 준비해주세요"),
    TOOL_INEXPERIENCED(FeedbackFeeling.CONSTRUCTIVE, FeedbackCategory.EFFORT, "툴 활용에 미숙해요"),

    LISTEN_REQUIRED(FeedbackFeeling.CONSTRUCTIVE, FeedbackCategory.COMMUNICATION, "다른 사람의 의견을 경청해주세요"),
    INITIATIVE_REQUIRED(FeedbackFeeling.CONSTRUCTIVE, FeedbackCategory.COMMUNICATION, "주도적으로 참여해주세요"),
    POLITE_REQUIRED(FeedbackFeeling.CONSTRUCTIVE, FeedbackCategory.COMMUNICATION, "상대방을 배려해 부드럽게 말씀해주세요"),

    RESPONSIBLE_REQUIRED(FeedbackFeeling.CONSTRUCTIVE, FeedbackCategory.ATTITUDE_AND_POSTURE, "책임감을 발휘해주세요"),
    PROACTIVE_REQUIRED(FeedbackFeeling.CONSTRUCTIVE, FeedbackCategory.ATTITUDE_AND_POSTURE, "적극적으로 참여해주세요"),
    RATIONAL_REQUIRED(FeedbackFeeling.CONSTRUCTIVE, FeedbackCategory.ATTITUDE_AND_POSTURE, "감정적인 표현을 지양해주세요"),
    FOCUS_REQUIRED(FeedbackFeeling.CONSTRUCTIVE, FeedbackCategory.ATTITUDE_AND_POSTURE, "조금 더 집중해주세요"),
    PUNCTUAL_REQUIRED(FeedbackFeeling.CONSTRUCTIVE, FeedbackCategory.ATTITUDE_AND_POSTURE, "시간 약속을 지켜주세요");

    private static final Map<String, ObjectiveFeedback> FEEDBACK_MAP;

    static {
        FEEDBACK_MAP = new HashMap<>();
        for (ObjectiveFeedback feedback : values()) {
            FEEDBACK_MAP.put(feedback.description, feedback);
        }
    }


    @Getter
    private final FeedbackFeeling feeling;

    @Getter
    private final FeedbackCategory category;

    @Getter(onMethod_ = @JsonValue)
    private final String description;

    ObjectiveFeedback(FeedbackFeeling feeling, FeedbackCategory category, String description) {
        this.feeling = feeling;
        this.category = category;
        this.description = description;
    }

    @JsonCreator
    public static ObjectiveFeedback fromDescription(String content) {
        ObjectiveFeedback objectiveFeedback = FEEDBACK_MAP.get(content);
        if (objectiveFeedback == null) {
            throw new IllegalArgumentException("Invalid description: " + content);
        }
        return objectiveFeedback;
    }

}
