package com.feedhanjum.back_end.team.domain;

import com.feedhanjum.back_end.feedback.domain.FeedbackType;
import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.team.exception.TeamLeaderMustExistException;
import com.feedhanjum.back_end.team.exception.TeamMembershipNotFoundException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Team {
    @Id
    @Column(name = "team_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private FeedbackType feedbackType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id", nullable = false)
    private Member leader;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<TeamMember> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<FrequentFeedbackRequest> frequentFeedbackRequests = new ArrayList<>();

    public Team(String name, Member leader, LocalDate startDate, LocalDate endDate, FeedbackType feedbackType) {
        validateDuration(startDate, endDate);
        this.feedbackType = feedbackType;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leader = leader;
        join(leader);
    }

    public void changeLeader(Member currentLeader, Member newLeader) {
        validateTeamLeader(currentLeader);
        if (!isTeamMember(newLeader))
            throw new TeamMembershipNotFoundException("팀 리더는 반드시 팀원이어야 합니다");
        this.leader = newLeader;
    }

    public void updateInfo(Member leader, String name, LocalDate startDate, LocalDate endDate, FeedbackType feedbackType) {
        validateTeamLeader(leader);
        validateDuration(startDate, endDate);
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.feedbackType = feedbackType;
    }

    // 팀 참가
    public void join(Member member) {
        if (isTeamMember(member))
            return;
        teamMembers.add(new TeamMember(this, member));
    }

    // 팀 탈퇴
    public void leave(Member member) {
        if (isTeamLeader(member))
            throw new TeamLeaderMustExistException("팀 리더는 팀을 나갈 수 없습니다");
        this.teamMembers.removeIf(teamMember -> member.equals(teamMember.getMember()));
    }

    // 팀원 강제 추방
    public void expel(Member leader, Member removeTarget) {
        validateTeamLeader(leader);
        this.leave(removeTarget);
    }

    public int memberCount() {
        return teamMembers.size();
    }

    public List<TeamMember> getTeamMembers() {
        return Collections.unmodifiableList(teamMembers);
    }

    public List<FrequentFeedbackRequest> getFrequentFeedbackRequests() {
        return Collections.unmodifiableList(frequentFeedbackRequests);
    }

    public void requestFeedback(Member sender, Member receiver, String requestedContent) {
        if (!(isTeamMember(sender) && isTeamMember(receiver)))
            throw new TeamMembershipNotFoundException("피드백 요청자와 수신자는 팀에 소속되어야 합니다");
        this.frequentFeedbackRequests.add(new FrequentFeedbackRequest(requestedContent, sender, this, receiver));
    }

    private boolean isTeamMember(Member member) {
        return teamMembers.stream().anyMatch(teamMember -> member.equals(teamMember.getMember()));
    }

    private boolean isTeamLeader(Member leader) {
        return leader.equals(leader);
    }

    private void validateTeamLeader(Member leader) {
        if (!isTeamLeader(leader))
            throw new SecurityException("팀장이 아닙니다");
    }

    private void validateDuration(LocalDate startDate, LocalDate endDate) {
        if (endDate != null && !startDate.isBefore(endDate)) {
            throw new IllegalArgumentException("프로젝트 시작 시간이 종료 시간보다 앞서야 합니다.");
        }
    }
}
