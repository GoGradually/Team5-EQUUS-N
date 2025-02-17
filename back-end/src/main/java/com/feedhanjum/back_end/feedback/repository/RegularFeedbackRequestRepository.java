package com.feedhanjum.back_end.feedback.repository;

import com.feedhanjum.back_end.feedback.domain.RegularFeedbackRequest;
import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.schedule.domain.ScheduleMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegularFeedbackRequestRepository extends JpaRepository<RegularFeedbackRequest, Long> {

    Optional<RegularFeedbackRequest> findByRequesterAndScheduleMember(Member requester, ScheduleMember scheduleMember);

    void deleteAllByScheduleMember(ScheduleMember scheduleMember);
}