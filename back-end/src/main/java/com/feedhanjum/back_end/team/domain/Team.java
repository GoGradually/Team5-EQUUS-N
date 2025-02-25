package com.feedhanjum.back_end.team.domain;

import com.feedhanjum.back_end.feedback.domain.FeedbackType;
import com.feedhanjum.back_end.feedback.domain.FrequentFeedbackRequest;
import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.team.exception.TeamEndedException;
import com.feedhanjum.back_end.team.exception.TeamLeaderMustExistException;
import com.feedhanjum.back_end.team.exception.TeamMembershipNotFoundException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@SQLRestriction("deleted = false")
@SQLDelete(sql = "UPDATE team SET deleted = true WHERE team_id = ?")
@Table(name = "team")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
public class Team {
    @Column(name = "deleted")
    private final boolean deleted = false;

    @Id
    @Column(name = "team_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public Team(String name, Member leader, LocalDate startDate, LocalDate endDate, FeedbackType feedbackType, LocalDate now) {
        validateDuration(startDate, endDate, now);
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

    public void updateInfo(Member leader, String name, LocalDate startDate, LocalDate endDate, FeedbackType feedbackType, LocalDate now) {
        validateTeamLeader(leader);
        validateDuration(startDate, endDate, now);
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
        validateTeamMember(member);
        if (isTeamLeader(member) && !onlyLeaderLeft())
            throw new TeamLeaderMustExistException("팀 리더는 팀을 나갈 수 없습니다");
        else if (!isTeamLeader(member) || onlyLeaderLeft())
            this.teamMembers.removeIf(teamMember -> member.equals(teamMember.getMember()));
    }

    // 팀원 강제 추방
    public void expel(Member leader, Member removeTarget) {
        validateTeamMember(removeTarget);
        validateTeamLeader(leader);
        this.leave(removeTarget);
    }

    public int memberCount() {
        return teamMembers.size();
    }

    // 수시 피드백 요청
    public void requestFeedback(Member requestSender, Member requestReceiver, String requestedContent) {
        validateTeamMember(requestSender);
        validateTeamMember(requestReceiver);
        this.frequentFeedbackRequests.removeIf(request -> requestSender.equals(request.getSender()) && requestReceiver.equals(request.getReceiver()));
        this.frequentFeedbackRequests.add(new FrequentFeedbackRequest(requestedContent, requestSender, this, requestReceiver));
    }

    // 모든 수시 피드백 요청 거절
    public void rejectFeedbackRequests(Member requestReceiver) {
        validateTeamMember(requestReceiver);
        this.frequentFeedbackRequests.removeIf(request -> requestReceiver.equals(request.getReceiver()));
    }

    // 특정 수시 피드백 요청 거절
    public void removeFeedbackRequest(Member requestSender, Member requestReceiver) {
        validateTeamMember(requestSender);
        validateTeamMember(requestReceiver);
        this.frequentFeedbackRequests.removeIf(request -> requestSender.equals(request.getSender()) && requestReceiver.equals(request.getReceiver()));
    }

    public List<FrequentFeedbackRequest> getFeedbackRequests(Member receiver) {
        validateTeamMember(receiver);
        return this.frequentFeedbackRequests.stream()
                .filter(request -> receiver.equals(request.getReceiver()))
                .toList();
    }

    public List<TeamMember> getTeamMembers() {
        return Collections.unmodifiableList(teamMembers);
    }

    public List<FrequentFeedbackRequest> getFrequentFeedbackRequests() {
        return Collections.unmodifiableList(frequentFeedbackRequests);
    }


    public boolean isTeamMember(Member member) {
        return teamMembers.stream().anyMatch(teamMember -> member.equals(teamMember.getMember()));
    }

    public TeamJoinToken createJoinToken(Member member, LocalDateTime now) {
        if (now.isAfter(endDate.plusDays(1).atStartOfDay()))
            throw new TeamEndedException("팀이 이미 종료되었습니다");
        validateTeamMember(member);
        return TeamJoinToken.createToken(this);
    }

    private boolean isTeamLeader(Member leader) {
        return this.leader.equals(leader);
    }

    private void validateTeamLeader(Member leader) {
        if (!isTeamLeader(leader))
            throw new SecurityException("팀장이 아닙니다");
    }

    private void validateTeamMember(Member member) {
        if (!isTeamMember(member))
            throw new TeamMembershipNotFoundException("팀원이 아닙니다");
    }

    private void validateDuration(LocalDate startDate, LocalDate endDate, LocalDate now) {
        if (endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("프로젝트 시작 시간이 종료 시간보다 앞서야 합니다.");
        }
        if (endDate != null && endDate.isBefore(now)) {
            throw new IllegalArgumentException("프로젝트 종료 날짜는 오늘 이후여야 합니다.");
        }
    }

    private boolean onlyLeaderLeft() {
        return teamMembers.size() == 1;
    }
}
