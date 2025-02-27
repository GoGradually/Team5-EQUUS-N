package com.feedhanjum.back_end.feedback.repository;

import com.feedhanjum.back_end.feedback.domain.QRegularFeedbackRequest;
import com.feedhanjum.back_end.feedback.domain.RegularFeedbackRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class RegularFeedbackRequestQueryRepository {
    private final QRegularFeedbackRequest regularFeedbackRequest = QRegularFeedbackRequest.regularFeedbackRequest;
    private final JPAQueryFactory queryFactory;


    public List<RegularFeedbackRequest> getRegularFeedbackRequests(Long receiverId, Long scheduleId) {
        return queryFactory.selectFrom(regularFeedbackRequest)
                .join(regularFeedbackRequest.requester).fetchJoin()
                .join(regularFeedbackRequest.scheduleMember).fetchJoin()
                .join(regularFeedbackRequest.scheduleMember.schedule).fetchJoin()
                .where(regularFeedbackRequest.scheduleMember.member.id.eq(receiverId)
                        .and(regularFeedbackRequest.scheduleMember.schedule.id.eq(scheduleId)))
                .fetch();
    }

    public Long getRegularFeedbackRequestCount(Long receiverId, Long scheduleId) {
        return queryFactory.select(regularFeedbackRequest.count())
                .from(regularFeedbackRequest)
                .join(regularFeedbackRequest.scheduleMember)
                .where(regularFeedbackRequest.scheduleMember.member.id.eq(receiverId)
                        .and(regularFeedbackRequest.scheduleMember.schedule.id.eq(scheduleId)))
                .fetchOne();
    }
}
