package com.feedhanjum.back_end.schedule.repository;

import com.feedhanjum.back_end.schedule.domain.ScheduleMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ScheduleMemberRepository extends JpaRepository<ScheduleMember, Long> {

    Optional<ScheduleMember> findByMemberIdAndScheduleId(Long memberId, Long scheduleId);

    @Modifying(clearAutomatically = true)
    @Query("delete from ScheduleMember sm " +
            "where sm.member.id = :memberId " +
            "  and sm.schedule.id in (" +
            "       select s.id from Schedule s " +
            "       where s.team.id = :teamId " +
            "         and s.endTime > :now" +
            "  )")
    void deleteScheduleMembersByMemberIdAndTeamIdAfterNow(Long memberId, Long teamId, LocalDateTime now);
}