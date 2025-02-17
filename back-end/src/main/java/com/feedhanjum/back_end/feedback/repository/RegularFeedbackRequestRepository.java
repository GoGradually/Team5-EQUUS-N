package com.feedhanjum.back_end.feedback.repository;

import com.feedhanjum.back_end.feedback.domain.RegularFeedbackRequest;
import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.schedule.domain.ScheduleMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RegularFeedbackRequestRepository extends JpaRepository<RegularFeedbackRequest, Long> {

    Optional<RegularFeedbackRequest> findByRequesterAndScheduleMember(Member requester, ScheduleMember scheduleMember);

    void deleteAllByScheduleMember(ScheduleMember scheduleMember);

    @Modifying(clearAutomatically = true)
    @Query("delete from RegularFeedbackRequest rfr " +
            "where rfr.requester.id = :requesterId " +
            "and rfr.scheduleMember.schedule.team.id = :teamId")
    void deleteAllByRequesterIdAndTeamId(Long requesterId, Long teamId);

    @Modifying(clearAutomatically = true)
    @Query("delete from RegularFeedbackRequest rfr " +
            "where rfr.scheduleMember.member.id = :receiverId " +
            "and rfr.scheduleMember.schedule.team.id = :teamId")
    void deleteAllByReceiverIdAndTeamId(Long receiverId, Long teamId);
}