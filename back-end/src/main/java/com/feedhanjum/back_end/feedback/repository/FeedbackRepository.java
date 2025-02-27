package com.feedhanjum.back_end.feedback.repository;

import com.feedhanjum.back_end.feedback.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
