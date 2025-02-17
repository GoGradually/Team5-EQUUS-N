package com.feedhanjum.back_end.feedback.repository;

import com.feedhanjum.back_end.feedback.domain.FrequentFeedbackRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FrequentFeedbackRequestRepository extends JpaRepository<FrequentFeedbackRequest, Long> {
}