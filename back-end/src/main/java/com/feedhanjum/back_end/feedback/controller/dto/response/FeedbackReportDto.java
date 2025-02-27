package com.feedhanjum.back_end.feedback.controller.dto.response;

import com.feedhanjum.back_end.feedback.domain.FeedbackCategory;
import com.feedhanjum.back_end.feedback.domain.FeedbackFeeling;
import com.feedhanjum.back_end.feedback.domain.FeedbackReport;
import com.feedhanjum.back_end.feedback.domain.ObjectiveFeedback;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "피드백 리포트")
public record FeedbackReportDto(
        @Schema(description = "받은 모든 피드백 개수")
        @PositiveOrZero
        int feedbackCount,

        @Schema(description = "피드백 리포트를 보기 위해 받아야 하는 최소 피드백 개수")
        @Positive
        int requiredFeedbackCount,

        @Schema(description = "allKeywords 중 '칭찬해요', '아쉬워요' 별 가장 높은 값")
        @Size(min = 1, max = 2)
        @Nullable
        List<KeywordCountDto> topKeywords,

        @Schema(description = "카테고리별 받은 객관식 피드백 정보")
        @Size(min = 1, max = 3)
        @Nullable
        List<CategoryCountDto> overviews,

        @Schema(description = "받은 모든 객관식 피드백 개수 정보")
        @Nullable
        List<KeywordCountDto> allKeywords
) {

    public static FeedbackReportDto from(FeedbackReport report) {
        return builder()
                .feedbackCount(report.getFeedbackCount())
                .requiredFeedbackCount(report.getRequiredFeedbackCount())
                .topKeywords(KeywordCountDto.from(report.getTopKeywords()))
                .overviews(CategoryCountDto.from(report.getOverviews()))
                .allKeywords(KeywordCountDto.from(report.getAllKeywords()))
                .build();

    }


    record KeywordCountDto(
            ObjectiveFeedback keyword,
            FeedbackFeeling feeling,
            @Schema(description = "받은 개수. '칭찬해요'의 경우 양수, '아쉬워요'의 경우 음수")
            Integer count
    ) {
        public static List<KeywordCountDto> from(@Nullable List<FeedbackReport.KeywordCount> keywordCounts) {
            if (keywordCounts == null)
                return null;

            return keywordCounts.stream().map(keywordCount ->
                    new KeywordCountDto(
                            keywordCount.getKeyword(),
                            keywordCount.getFeeling(),
                            keywordCount.getCount())
            ).toList();
        }
    }

    record CategoryCountDto(
            FeedbackCategory category,
            @Schema(description = "'칭찬해요'에 속하는 피드백을 받은 개수. 양수")
            @PositiveOrZero
            Integer goodCount,
            @Schema(description = "'아쉬워요'에 속하는 피드백을 받은 개수. 음수")
            @NegativeOrZero
            Integer badCount
    ) {
        public static List<CategoryCountDto> from(@Nullable List<FeedbackReport.CategoryCount> categoryCounts) {
            if (categoryCounts == null)
                return null;
            return categoryCounts.stream().map(categoryCount ->
                    new CategoryCountDto(
                            categoryCount.getCategory(),
                            categoryCount.getGoodCount(),
                            categoryCount.getBadCount())
            ).toList();
        }
    }
}
