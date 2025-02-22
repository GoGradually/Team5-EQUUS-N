package com.feedhanjum.back_end.feedback.domain;

import jakarta.annotation.Nullable;
import lombok.Getter;

import java.util.*;

public class FeedbackReport {
    public static final Integer REQUIRED_FEEDBACK_COUNT = 10;

    @Getter
    private int feedbackCount;

    @Getter
    private final int requiredFeedbackCount = REQUIRED_FEEDBACK_COUNT;

    private final Map<FeedbackCategory, CategoryCount> overviews;
    private final Map<ObjectiveFeedback, KeywordCount> allKeywords;


    private FeedbackReport() {
        this.feedbackCount = 0;
        this.overviews = new EnumMap<>(FeedbackCategory.class);
        for (FeedbackCategory category : FeedbackCategory.values()) {
            overviews.put(category, new CategoryCount(category));
        }

        this.allKeywords = new EnumMap<>(ObjectiveFeedback.class);
    }


    private void applyFeedback(Feedback feedback) {
        feedbackCount += 1;
        for (ObjectiveFeedback objectiveFeedback : feedback.getObjectiveFeedbacks()) {
            applyObjectiveFeedback(objectiveFeedback);
        }
    }

    @Nullable
    public List<CategoryCount> getOverviews() {
        if (!isFeedbackCountEnough())
            return null;
        return new ArrayList<>(this.overviews.values());
    }

    @Nullable
    public List<KeywordCount> getTopKeywords() {
        if (!isFeedbackCountEnough())
            return null;
        List<KeywordCount> topKeywords = new ArrayList<>();
        List<KeywordCount> sortedAllKeywords = getAllKeywords();
        sortedAllKeywords.stream().filter(keywordCount -> keywordCount.feeling == FeedbackFeeling.POSITIVE).findFirst()
                .ifPresent(topKeywords::add);
        sortedAllKeywords.stream().filter(keywordCount -> keywordCount.feeling == FeedbackFeeling.CONSTRUCTIVE).findFirst()
                .ifPresent(topKeywords::add);
        return topKeywords;
    }

    // '전체보기'에 들어갈 객관식 피드백 통계정보를 정렬해서 제공
    @Nullable
    public List<KeywordCount> getAllKeywords() {
        if (!isFeedbackCountEnough())
            return null;
        List<KeywordCount> result = new ArrayList<>(allKeywords.values());
        result.sort(KeywordCount::compareTo);
        return result;
    }

    private boolean isFeedbackCountEnough() {
        return feedbackCount >= requiredFeedbackCount;
    }

    private void applyObjectiveFeedback(ObjectiveFeedback objectiveFeedback) {
        FeedbackFeeling feeling = objectiveFeedback.getFeeling();
        FeedbackCategory category = objectiveFeedback.getCategory();
        allKeywords.compute(objectiveFeedback, (key, value) -> {
            if (value == null) {
                value = new KeywordCount(objectiveFeedback);
            }
            value.increaseCount();
            return value;
        });
        CategoryCount value = Objects.requireNonNullElseGet(overviews.get(category), () -> new CategoryCount(category));
        switch (feeling) {
            case POSITIVE -> value.increaseGoodCount();
            case CONSTRUCTIVE -> value.increaseBadCount();
        }
        overviews.put(category, value);
    }

    public static FeedbackReport fromFeedbacks(List<Feedback> feedbacks) {
        FeedbackReport report = new FeedbackReport();
        for (Feedback feedback : feedbacks) {
            report.applyFeedback(feedback);
        }
        return report;
    }

    @Getter
    public static class KeywordCount implements Comparable<KeywordCount> {
        private final ObjectiveFeedback keyword;
        private final FeedbackFeeling feeling;
        // 칭찬해요는 양수, 아쉬워요는 음수값을 가짐
        private Integer count;

        public KeywordCount(ObjectiveFeedback keyword) {
            this.keyword = keyword;
            this.feeling = keyword.getFeeling();
            this.count = 0;
        }

        public void increaseCount() {
            switch (feeling) {
                case POSITIVE -> count += 1;
                case CONSTRUCTIVE -> count -= 1;
            }
        }

        @Override
        public int compareTo(KeywordCount o) {
            // count 값으로 비교
            int order = -(Math.abs(this.count) - Math.abs(o.count));
            if (order != 0) {
                return order;
            }
            // '칭찬해요' 를 '아쉬워요' 보다 더 앞으로 오도록 취급
            if (this.feeling == FeedbackFeeling.POSITIVE && o.feeling == FeedbackFeeling.CONSTRUCTIVE) {
                return -1;
            } else if (this.feeling == FeedbackFeeling.CONSTRUCTIVE && o.feeling == FeedbackFeeling.POSITIVE) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    @Getter
    public static class CategoryCount {
        private FeedbackCategory category;
        // 양수
        private Integer goodCount;
        // 음수
        private Integer badCount;

        public CategoryCount(FeedbackCategory category) {
            this.category = category;
            this.goodCount = 0;
            this.badCount = 0;
        }

        public void increaseGoodCount() {
            goodCount += 1;
        }

        public void increaseBadCount() {
            badCount -= 1;
        }
    }


}
