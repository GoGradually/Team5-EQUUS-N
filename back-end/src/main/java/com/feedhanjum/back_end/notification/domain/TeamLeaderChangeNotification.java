package com.feedhanjum.back_end.notification.domain;

import com.feedhanjum.back_end.team.domain.Team;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DiscriminatorValue(NotificationType.TEAM_LEADER_CHANGE)
public class TeamLeaderChangeNotification extends InAppNotification {
    private String teamName;
    private Long teamId;

    public TeamLeaderChangeNotification(Team team) {
        super(team.getLeader().getId());
        this.teamName = team.getName();
        this.teamId = team.getId();
    }
}
