package com.feedhanjum.back_end.team.repository;

import com.feedhanjum.back_end.team.domain.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.feedhanjum.back_end.team.domain.QTeam.team;
import static com.feedhanjum.back_end.team.domain.QTeamMember.teamMember;

@Repository
@RequiredArgsConstructor
public class TeamQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<Team> findTeamByMemberId(Long memberId) {
        return jpaQueryFactory.select(team)
                .from(team)
                .join(teamMember).on(teamMember.team.id.eq(team.id)).fetchJoin()
                .where(teamMember.member.id.eq(memberId))
                .fetch();
    }
}
