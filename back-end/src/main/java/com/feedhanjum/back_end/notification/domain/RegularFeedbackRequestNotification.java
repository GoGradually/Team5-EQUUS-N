package com.feedhanjum.back_end.notification.domain;


import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.schedule.domain.Schedule;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DiscriminatorValue(NotificationType.REGULAR_FEEDBACK_REQUEST)
public class RegularFeedbackRequestNotification extends InAppNotification {
    private String scheduleName;
    private String teamName;
    private Long scheduleId;
    private Long teamId;

    public RegularFeedbackRequestNotification(Member receiver, Schedule schedule) {
        super(receiver.getId());
        this.scheduleName = schedule.getName();
        this.teamName = schedule.getTeam().getName();
        this.scheduleId = schedule.getId();
        this.teamId = schedule.getTeam().getId();
    }
}
