package com.feedhanjum.back_end.feedback.domain;

import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.team.domain.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Feedback {
    public static final int MIN_OBJECTIVE_FEEDBACK_SIZE = 1;
    public static final int MAX_OBJECTIVE_FEEDBACK_SIZE = 5;
    public static final int MIN_SUBJECTIVE_FEEDBACK_BYTE = 0;
    public static final int MAX_SUBJECTIVE_FEEDBACK_BYTE = 400;

    @Id
    @Column(name = "feedback_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FeedbackType feedbackType;

    @Enumerated(EnumType.STRING)
    private FeedbackFeeling feedbackFeeling;

    private String subjectiveFeedback;

    private boolean liked = false;

    private LocalDateTime createdAt;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "sender_id")),
            @AttributeOverride(name = "name", column = @Column(name = "sender_name")),
    })
    private Sender sender;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "receiver_id")),
            @AttributeOverride(name = "name", column = @Column(name = "receiver_name")),
    })
    private Receiver receiver;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "team_id")),
            @AttributeOverride(name = "name", column = @Column(name = "team_name")),
    })
    private AssociatedTeam team;

    // 객관식 피드백
    @Column(name = "objective_feedbacks", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private final Set<ObjectiveFeedback> objectiveFeedbacks = new HashSet<>();

    /**
     * @throws IllegalArgumentException 피드백 기분에 맞지 객관식 피드백이 있을 경우, 또는 객관식 피드백이 1개 이상 5개 이하가 아닐 경우
     */
    @Builder
    public Feedback(FeedbackType feedbackType, FeedbackFeeling feedbackFeeling, List<ObjectiveFeedback> objectiveFeedbacks, String subjectiveFeedback, Member sender, Member receiver, Team team) {
        this.feedbackType = feedbackType;
        this.subjectiveFeedback = subjectiveFeedback;
        this.feedbackFeeling = feedbackFeeling;
        this.objectiveFeedbacks.addAll(objectiveFeedbacks);
        this.sender = Sender.of(sender);
        this.receiver = Receiver.of(receiver);
        this.team = AssociatedTeam.of(team);
        this.createdAt = LocalDateTime.now();
        validateObjectiveFeedbacks();
    }

    public void like(Member member) {
        if (!isReceiver(member))
            throw new SecurityException("수신자만 피드백을 좋아요 할 수 있습니다.");
        this.liked = true;

    }

    public void unlike(Member member) {
        if (!isReceiver(member))
            throw new SecurityException("수신자만 피드백 좋아요를 취소할 수 있습니다.");

        this.liked = false;
    }

    private boolean isReceiver(Member member) {
        return receiver.getId().equals(member.getId());
    }

    private void validateObjectiveFeedbacks() {
        if (!(MIN_OBJECTIVE_FEEDBACK_SIZE <= objectiveFeedbacks.size()
                && objectiveFeedbacks.size() <= MAX_OBJECTIVE_FEEDBACK_SIZE)) {
            throw new IllegalArgumentException("객관식 피드백은 " + MIN_OBJECTIVE_FEEDBACK_SIZE + "개 이상 " + MAX_OBJECTIVE_FEEDBACK_SIZE + "개 이하만 가능합니다.");
        }
        for (ObjectiveFeedback objectiveFeedback : objectiveFeedbacks) {
            if (!feedbackFeeling.isValidObjectiveFeedback(objectiveFeedback)) {
                throw new IllegalArgumentException("피드백 카테고리와 일치하지 않는 객관식 피드백입니다: " + objectiveFeedback);
            }
        }
    }

}
