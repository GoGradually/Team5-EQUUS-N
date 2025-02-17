package com.feedhanjum.back_end.feedback.repository;

import com.feedhanjum.back_end.feedback.domain.FrequentFeedbackRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FrequentFeedbackRequestRepository extends JpaRepository<FrequentFeedbackRequest, Long> {
    @Modifying(clearAutomatically = true)
    @Query("delete from FrequentFeedbackRequest ffr " +
            "where ffr.sender.id = :senderId " +
            "and ffr.team.id = :teamId")
    void deleteAllBySenderIdAndTeamId(Long senderId, Long teamId);

    @Modifying(clearAutomatically = true)
    @Query("delete from FrequentFeedbackRequest ffr " +
            "where ffr.receiver.id = :receiverId " +
            "and ffr.team.id = :teamId")
    void deleteAllByReceiverIdAndTeamId(Long receiverId, Long teamId);
}