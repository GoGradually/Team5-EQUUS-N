package com.feedhanjum.back_end.feedback.service.dto;

import com.feedhanjum.back_end.feedback.domain.Feedback;
import com.feedhanjum.back_end.feedback.domain.FeedbackType;
import com.feedhanjum.back_end.feedback.domain.ObjectiveFeedback;
import com.feedhanjum.back_end.feedback.domain.Receiver;

import java.time.LocalDateTime;
import java.util.List;

public record SentFeedbackDto(
        Long feedbackId,
        boolean isAnonymous,
        ReceiverDto receiver,
        List<String> objectiveFeedbacks,
        String subjectiveFeedback,
        String teamName,
        boolean liked,
        LocalDateTime createdAt
) {


    public static SentFeedbackDto from(Feedback feedback) {
        return new SentFeedbackDto(
                feedback.getId(),
                feedback.getFeedbackType() == FeedbackType.ANONYMOUS,
                ReceiverDto.from(feedback.getReceiver()),
                feedback.getObjectiveFeedbacks().stream().map(ObjectiveFeedback::getDescription).toList(),
                feedback.getSubjectiveFeedback(),
                feedback.getTeam().getName(),
                feedback.isLiked(),
                feedback.getCreatedAt()
        );
    }


    public record ReceiverDto(String name, String backgroundColor, String image) {
        public static ReceiverDto from(Receiver receiver) {
            return new ReceiverDto(receiver.getName(), receiver.getProfileImage().getBackgroundColor(), receiver.getProfileImage().getImage());
        }
    }

}
