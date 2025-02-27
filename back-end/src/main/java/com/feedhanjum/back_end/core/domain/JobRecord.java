package com.feedhanjum.back_end.core.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class JobRecord {
    @Id
    @Enumerated(EnumType.STRING)
    private JobName name;

    private LocalDateTime previousFinishTime;

    public enum JobName {
        SCHEDULE, UNREAD_NOTIFICATIONS
    }

    public JobRecord(JobName name) {
        this(name, LocalDateTime.of(1970, 1, 1, 0, 0, 0));
    }

    public JobRecord(JobName name, LocalDateTime previousFinishTime) {
        this.name = name;
        this.previousFinishTime = previousFinishTime;
    }

    public void updatePreviousFinishTime(LocalDateTime newFinishTime) {
        this.previousFinishTime = newFinishTime;
    }
}
