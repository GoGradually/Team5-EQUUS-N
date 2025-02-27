package com.feedhanjum.back_end.feedback.service.dto;

import com.feedhanjum.back_end.feedback.domain.Feedback;
import com.feedhanjum.back_end.feedback.domain.FeedbackType;
import com.feedhanjum.back_end.feedback.domain.ObjectiveFeedback;
import com.feedhanjum.back_end.feedback.domain.Sender;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.List;

public record ReceivedFeedbackDto(
        Long feedbackId,
        boolean isAnonymous,
        @Nullable SenderDto sender,
        List<String> objectiveFeedbacks,
        String subjectiveFeedback,
        String teamName,
        boolean liked,
        LocalDateTime createdAt
) {

    public ReceivedFeedbackDto(Long feedbackId, boolean isAnonymous, @Nullable SenderDto sender, List<String> objectiveFeedbacks, String subjectiveFeedback, String teamName, boolean liked, LocalDateTime createdAt) {
        this.feedbackId = feedbackId;
        this.isAnonymous = isAnonymous;
        this.objectiveFeedbacks = objectiveFeedbacks;
        this.subjectiveFeedback = subjectiveFeedback;
        this.teamName = teamName;
        this.liked = liked;
        this.createdAt = createdAt;
        if (isAnonymous)
            this.sender = null;
        else
            this.sender = sender;
    }

    public static ReceivedFeedbackDto from(Feedback feedback) {
        return new ReceivedFeedbackDto(
                feedback.getId(),
                feedback.getFeedbackType() == FeedbackType.ANONYMOUS,
                SenderDto.from(feedback.getSender()),
                feedback.getObjectiveFeedbacks().stream().map(ObjectiveFeedback::getDescription).toList(),
                feedback.getSubjectiveFeedback(),
                feedback.getTeam().getName(),
                feedback.isLiked(),
                feedback.getCreatedAt()
        );
    }


    public record SenderDto(String name, String backgroundColor, String image) {
        public static SenderDto from(Sender sender) {
            return new SenderDto(sender.getName(), sender.getProfileImage().getBackgroundColor(), sender.getProfileImage().getImage());
        }
    }

}
