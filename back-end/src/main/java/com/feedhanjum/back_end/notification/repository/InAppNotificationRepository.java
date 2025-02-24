package com.feedhanjum.back_end.notification.repository;

import com.feedhanjum.back_end.notification.domain.InAppNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InAppNotificationRepository extends JpaRepository<InAppNotification, Long> {
    List<InAppNotification> findAllByReceiverIdAndType(Long receiverId, String type);
}
