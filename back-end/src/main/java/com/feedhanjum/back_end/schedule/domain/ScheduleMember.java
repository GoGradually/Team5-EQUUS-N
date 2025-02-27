package com.feedhanjum.back_end.schedule.domain;

import com.feedhanjum.back_end.feedback.domain.RegularFeedbackRequest;
import com.feedhanjum.back_end.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ScheduleMember {
    @Id
    @Column(name = "schedule_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "scheduleMember")
    private final List<RegularFeedbackRequest> regularFeedbackRequests = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "todos",
            joinColumns = @JoinColumn(name = "schedule_member_id")
    )

    private final List<Todo> todos = new ArrayList<>();

    public ScheduleMember(Schedule schedule, Member member) {
        this.member = member;
        setSchedule(schedule);
    }

    public void setTodos(List<Todo> todos){
        this.todos.clear();
        if(todos != null){
            this.todos.addAll(todos);
        }
    }

    private void setSchedule(Schedule schedule) {
        if (this.schedule != null) {
            this.schedule.getScheduleMembers().remove(this);
        }
        this.schedule = schedule;
        if (schedule != null && !schedule.getScheduleMembers().contains(this)) {
            schedule.getScheduleMembers().add(this);
        }
    }

}
