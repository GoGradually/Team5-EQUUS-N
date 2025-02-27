package com.feedhanjum.back_end.feedback.repository;

import com.feedhanjum.back_end.feedback.domain.QRetrospect;
import com.feedhanjum.back_end.feedback.domain.Retrospect;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Repository
public class RetrospectQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final QRetrospect retrospect = QRetrospect.retrospect;
    private final NumberPath<Long> sortProperty = retrospect.id;


    public Page<Retrospect> findRetrospects(Long writerId, @Nullable Long teamId, Pageable pageable, Sort.Direction sortOrder) {
        Objects.requireNonNull(writerId);
        Objects.requireNonNull(pageable);
        Objects.requireNonNull(sortOrder);

        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(retrospect.writer.id.eq(writerId));
        if (teamId != null) {
            predicate.and(retrospect.team.id.eq(teamId));
        }

        List<Retrospect> result = queryFactory
                .selectFrom(retrospect)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sortOrder == Sort.Direction.ASC ? sortProperty.asc() : sortProperty.desc())
                .fetch();
        Long total = queryFactory.select(retrospect.count())
                .from(retrospect)
                .where(predicate)
                .fetchOne();
        if (total == null) {
            total = (long) result.size();
        }
        return new PageImpl<>(result, pageable, total);
    }
}
