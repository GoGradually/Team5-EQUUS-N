package com.feedhanjum.back_end.schedule.domain;

import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.team.domain.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SQLRestriction("deleted = false")
@SQLDelete(sql = "UPDATE schedule SET deleted = true WHERE schedule_id = ?")
@Table(name = "schedule")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Schedule {
    @Column(name = "deleted")
    private final boolean deleted = false;

    @Id
    @Column(name = "schedule_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Member owner;

    @OneToMany(mappedBy = "schedule")
    private final List<ScheduleMember> scheduleMembers = new ArrayList<>();

    public Schedule(String name, LocalDateTime startTime, LocalDateTime endTime, Team team, Member owner) {
        this.name = name;
        setTime(startTime, endTime);
        this.team = team;
        this.owner = owner;
    }

    public boolean isNameDifferent(String name) {
        return !name.equals(this.name);
    }

    public boolean isEnd() {
        return LocalDateTime.now().isAfter(endTime);
    }

    public void changeName(String name) {
        this.name = name;
    }

    public boolean isTimeDifferent(LocalDateTime startTime, LocalDateTime endTime) {
        return !this.startTime.isEqual(startTime) || !this.endTime.isEqual(endTime);
    }

    public boolean isStartTimeDifferent(LocalDateTime startTime) {
        return !this.startTime.isEqual(startTime);
    }

    public void setTime(LocalDateTime startTime, LocalDateTime endTime) {
        validate10MinuteInterval(startTime);
        validate10MinuteInterval(endTime);
        validateStartTimeIsBeforeEndTime(startTime, endTime);
        validateStartDateIsEqualToEndDate(startTime, endTime);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private void validateStartDateIsEqualToEndDate(LocalDateTime startTime, LocalDateTime endTime) {
        if (!startTime.toLocalDate().isEqual(endTime.toLocalDate())) {
            throw new IllegalArgumentException("시작 날짜와 종료 날짜는 동일해야 합니다.");
        }
    }

    private void validateStartTimeIsBeforeEndTime(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            throw new IllegalArgumentException("시작 시간은 종료 시간보다 이전이어야 합니다.");
        }
    }

    private void validate10MinuteInterval(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("일정의 시작시간과 종료시간은 빈 값이 아니어야 합니다.");
        }
        if (!(dateTime.getMinute() % 10 == 0
                && dateTime.getSecond() == 0
                && dateTime.getNano() == 0))
            throw new IllegalArgumentException("일정은 10분 단위로 설정해야 합니다.");
    }
}
