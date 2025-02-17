package com.feedhanjum.back_end.notification.repository;

import com.feedhanjum.back_end.notification.domain.InAppNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InAppNotificationRepository extends JpaRepository<InAppNotification, Long> {

    Optional<InAppNotification> findByReceiverIdAndType(Long receiverId, String type);
}
