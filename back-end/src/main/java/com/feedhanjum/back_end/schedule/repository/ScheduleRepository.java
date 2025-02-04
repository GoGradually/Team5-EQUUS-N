package com.feedhanjum.back_end.schedule.repository;

import com.feedhanjum.back_end.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("select s from Schedule s join fetch s.scheduleMembers m join fetch m.member where s.id = :id")
    Optional<Schedule> findByIdWithMembers(Long id);

    Optional<Schedule> findByStartTime(LocalDateTime startTime);
}