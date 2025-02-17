package com.feedhanjum.back_end.notification.domain;

import com.feedhanjum.back_end.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Entity
public abstract class InAppNotification {
    @Id
    @Column(name = "notification_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    protected Long receiverId;

    @Column(nullable = false)
    protected LocalDateTime createdAt;

    @Column(nullable = false)
    protected boolean isRead;

    @Column(name = "type", nullable = false, updatable = false, insertable = false)
    protected String type;

    protected InAppNotification(Long receiverId) {
        this.id = null;
        this.receiverId = receiverId;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }

    public void read(Member notificationReceiver) {
        if (!isReceiver(notificationReceiver)) {
            throw new SecurityException("알림을 읽을 권한이 없습니다");
        }
        this.isRead = true;
    }

    private boolean isReceiver(Member member) {
        return getReceiverId().equals(member.getId());
    }
}
