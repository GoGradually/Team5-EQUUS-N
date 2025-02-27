package com.feedhanjum.back_end.feedback.domain;

import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.schedule.domain.ScheduleMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RegularFeedbackRequest {
    @Id
    @Column(name = "regular_feedback_request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private Member requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_member_id")
    private ScheduleMember scheduleMember;

    public RegularFeedbackRequest(LocalDateTime createdAt, ScheduleMember scheduleMember, Member requester) {
        this.createdAt = createdAt;
        this.requester = requester;
        setScheduleMember(scheduleMember);
    }

    private void setScheduleMember(ScheduleMember scheduleMember) {
        if (this.scheduleMember != null) {
            this.scheduleMember.getRegularFeedbackRequests().remove(this);
        }
        this.scheduleMember = scheduleMember;
        if (scheduleMember != null && !scheduleMember.getRegularFeedbackRequests().contains(this)) {
            scheduleMember.getRegularFeedbackRequests().add(this);
        }
    }
}
