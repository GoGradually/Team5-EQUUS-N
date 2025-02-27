package com.feedhanjum.back_end.feedback.domain;

import com.feedhanjum.back_end.team.domain.Team;
import jakarta.persistence.Embeddable;
import lombok.*;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class AssociatedTeam {
    private Long id;

    private String name;

    public static AssociatedTeam of(Team team) {
        return new AssociatedTeam(team.getId(), team.getName());
    }
}
