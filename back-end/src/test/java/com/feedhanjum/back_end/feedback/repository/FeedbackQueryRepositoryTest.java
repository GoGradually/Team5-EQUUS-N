package com.feedhanjum.back_end.feedback.repository;

import com.feedhanjum.back_end.core.config.QuerydslConfig;
import com.feedhanjum.back_end.feedback.domain.Feedback;
import com.feedhanjum.back_end.feedback.domain.FeedbackFeeling;
import com.feedhanjum.back_end.feedback.domain.FeedbackType;
import com.feedhanjum.back_end.member.domain.FeedbackPreference;
import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.member.domain.ProfileImage;
import com.feedhanjum.back_end.member.repository.MemberRepository;
import com.feedhanjum.back_end.team.domain.Team;
import com.feedhanjum.back_end.team.repository.TeamRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static com.feedhanjum.back_end.test.util.DomainTestUtils.assertEqualReceiver;
import static com.feedhanjum.back_end.test.util.DomainTestUtils.assertEqualSender;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class FeedbackQueryRepositoryTest {

    @TestConfiguration
    @Import(QuerydslConfig.class)
    static class Config {
        @Bean
        public FeedbackQueryRepository feedbackQueryRepository(JPAQueryFactory jpaQueryFactory) {
            return new FeedbackQueryRepository(jpaQueryFactory);
        }

        @Bean
        public PersistenceUnitUtil persistenceUnitUtil(EntityManager entityManager) {
            return entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        }

    }

    @Autowired
    private FeedbackQueryRepository feedbackQueryRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TeamRepository teamRepository;


    Feedback createFeedback(Member sender, Member receiver, Team team, boolean like) {
        Feedback feedback = Feedback.builder()
                .sender(sender)
                .receiver(receiver)
                .team(team)
                .feedbackType(FeedbackType.ANONYMOUS)
                .feedbackFeeling(FeedbackFeeling.POSITIVE)
                .objectiveFeedbacks(FeedbackFeeling.POSITIVE.getObjectiveFeedbacks().subList(0, 2))
                .subjectiveFeedback(team.toString() + ", " + sender.toString() + "->" + receiver.toString())
                .build();
        if (like) {
            feedback.like(receiver);
        }
        return feedback;
    }


    @Nested
    @DisplayName("findReceivedFeedbacks 메소드 테스트")
    class FindReceivedFeedbacks {

        private Member member1;
        private Member member2;
        private Team team1;
        private Team team2;

        @BeforeEach
        void setUp() {
            List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);
            member1 = new Member("member1", "email1@email.com", new ProfileImage("bg1", "profile1"), feedbackPreferences);
            member2 = new Member("member2", "email2@email.com", new ProfileImage("bg1", "profile1"), feedbackPreferences);
            memberRepository.saveAll(List.of(member1, member2));

            team1 = new Team("team1", member1, LocalDateTime.now().minusDays(1).toLocalDate(), LocalDateTime.now().plusDays(1).toLocalDate(), FeedbackType.ANONYMOUS, LocalDate.now());
            team2 = new Team("team2", member2, LocalDateTime.now().minusDays(1).toLocalDate(), LocalDateTime.now().plusDays(1).toLocalDate(), FeedbackType.ANONYMOUS, LocalDate.now());
            teamRepository.saveAll(List.of(team1, team2));
        }

        @Test
        @DisplayName("모든 피드백 최신순 조회 성공")
        void test1() {
            // given
            Member sender = member1;
            Member receiver = member2;
            for (int i = 0; i < 5; i++) {
                Feedback feedback;
                boolean liked = i % 2 == 0;
                feedback = createFeedback(sender, receiver, team1, liked);
                feedbackRepository.save(feedback);
                feedback = createFeedback(sender, receiver, team2, liked);
                feedbackRepository.save(feedback);
            }

            for (int i = 0; i < 5; i++) {
                feedbackRepository.save(createFeedback(receiver, sender, team1, false));
            }

            int page = 0;
            int pageSize = 4;
            Sort.Direction order = Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page, pageSize);

            // when
            Page<Feedback> result = feedbackQueryRepository.findReceivedFeedbacks(receiver.getId(), null, false, pageable, order);

            // then
            assertThat(result.getContent()).hasSize(pageSize);
            assertThat(result.hasNext()).isTrue();
            assertThat(result.getNumber()).isEqualTo(page);
            assertThat(result.getTotalElements()).isEqualTo(10);
            List<Feedback> feedbacks = result.getContent();
            assertThat(feedbacks).hasSize(pageSize);
            assertThat(feedbacks).extracting(Feedback::getId).isSortedAccordingTo(Comparator.reverseOrder());
            assertThat(feedbacks).extracting(Feedback::getReceiver).allSatisfy(r -> assertEqualReceiver(receiver, r));
            assertThat(feedbacks).extracting(f -> f.getTeam().getName()).containsExactly("team2", "team1", "team2", "team1");
            assertThat(feedbacks).extracting(Feedback::isLiked).containsExactly(true, true, false, false);
        }

        @Test
        @DisplayName("팀 id로 필터링 조회 성공")
        void test2() {
            // given
            Member sender = member1;
            Member receiver = member2;
            for (int i = 0; i < 5; i++) {
                Feedback feedback;
                boolean liked = i % 2 == 0;
                feedback = createFeedback(sender, receiver, team1, liked);
                feedbackRepository.save(feedback);
                feedback = createFeedback(sender, receiver, team2, liked);
                feedbackRepository.save(feedback);
            }

            for (int i = 0; i < 5; i++) {
                feedbackRepository.save(createFeedback(receiver, sender, team1, false));
            }

            int page = 0;
            int pageSize = 4;
            Sort.Direction order = Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page, pageSize);

            // when
            Page<Feedback> result = feedbackQueryRepository.findReceivedFeedbacks(receiver.getId(), team1.getId(), false, pageable, order);

            // then
            assertThat(result.getContent()).hasSize(pageSize);

            assertThat(result.hasNext()).isTrue();
            assertThat(result.getNumber()).isEqualTo(page);
            assertThat(result.getTotalElements()).isEqualTo(5);
            List<Feedback> feedbacks = result.getContent();
            assertThat(feedbacks).hasSize(pageSize);
            assertThat(feedbacks).extracting(Feedback::getId).isSortedAccordingTo(Comparator.reverseOrder());
            assertThat(feedbacks).extracting(Feedback::getReceiver).allSatisfy(r -> assertEqualReceiver(receiver, r));
            assertThat(feedbacks).extracting(f -> f.getTeam().getName()).containsOnly("team1");
            assertThat(feedbacks).extracting(Feedback::isLiked).containsExactly(true, false, true, false);
        }

        @Test
        @DisplayName("좋아요 여부로 필터링 조회 성공")
        void test3() {
            // given
            Member sender = member1;
            Member receiver = member2;

            feedbackRepository.save(createFeedback(sender, receiver, team1, true));
            feedbackRepository.save(createFeedback(sender, receiver, team2, true));
            feedbackRepository.save(createFeedback(sender, receiver, team1, false));
            feedbackRepository.save(createFeedback(sender, receiver, team1, false));
            feedbackRepository.save(createFeedback(sender, receiver, team2, true));
            feedbackRepository.save(createFeedback(sender, receiver, team2, true));


            for (int i = 0; i < 5; i++) {
                feedbackRepository.save(createFeedback(receiver, sender, team1, false));
            }

            int page = 0;
            int pageSize = 3;
            Sort.Direction order = Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, pageSize);

            // when
            Page<Feedback> result = feedbackQueryRepository.findReceivedFeedbacks(receiver.getId(), null, true, pageable, order);

            // then
            assertThat(result.getContent()).hasSize(pageSize);
            assertThat(result.hasNext()).isTrue();
            assertThat(result.getNumber()).isEqualTo(page);
            assertThat(result.getTotalElements()).isEqualTo(4);
            List<Feedback> feedbacks = result.getContent();
            assertThat(feedbacks).hasSize(pageSize);
            assertThat(feedbacks).extracting(Feedback::getId).isSortedAccordingTo(Comparator.naturalOrder());
            assertThat(feedbacks).extracting(Feedback::getReceiver).allSatisfy(r -> assertEqualReceiver(receiver, r));
            assertThat(feedbacks).extracting(f -> f.getTeam().getName()).containsExactly("team1", "team2", "team2");
            assertThat(feedbacks).extracting(Feedback::isLiked).containsOnly(true);
        }

        @Test
        @DisplayName("과거순 정렬 조회 성공")
        void test4() {
            // given
            Member sender = member1;
            Member receiver = member2;
            for (int i = 0; i < 5; i++) {
                Feedback feedback;
                boolean liked = i % 2 == 0;
                feedback = createFeedback(sender, receiver, team1, liked);
                feedbackRepository.save(feedback);
                feedback = createFeedback(sender, receiver, team2, liked);
                feedbackRepository.save(feedback);
            }

            for (int i = 0; i < 5; i++) {
                feedbackRepository.save(createFeedback(receiver, sender, team1, false));
            }

            int page = 0;
            int pageSize = 4;
            Sort.Direction order = Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, pageSize);

            // when
            Page<Feedback> result = feedbackQueryRepository.findReceivedFeedbacks(receiver.getId(), null, false, pageable, order);

            // then
            assertThat(result.getContent()).hasSize(pageSize);
            assertThat(result.hasNext()).isTrue();
            assertThat(result.getNumber()).isEqualTo(page);
            assertThat(result.getTotalElements()).isEqualTo(10);
            List<Feedback> feedbacks = result.getContent();
            assertThat(feedbacks).hasSize(pageSize);
            assertThat(feedbacks).extracting(Feedback::getId).isSortedAccordingTo(Comparator.naturalOrder());
            assertThat(feedbacks).extracting(Feedback::getReceiver).allSatisfy(r -> assertEqualReceiver(receiver, r));
            assertThat(feedbacks).extracting(f -> f.getTeam().getName()).containsExactly("team1", "team2", "team1", "team2");
            assertThat(feedbacks).extracting(Feedback::isLiked).containsExactly(true, true, false, false);
        }

    }

    @Nested
    @DisplayName("findSentFeedbacks 메소드 테스트")
    class FindSentFeedbacks {

        private Member member1;
        private Member member2;
        private Team team1;
        private Team team2;

        @BeforeEach
        void setUp() {
            List<FeedbackPreference> feedbackPreferences = List.of(FeedbackPreference.PROGRESSIVE, FeedbackPreference.COMPLEMENTING);
            member1 = new Member("member1", "email1@email.com", new ProfileImage("bg1", "profile1"), feedbackPreferences);
            member2 = new Member("member2", "email2@email.com", new ProfileImage("bg1", "profile1"), feedbackPreferences);
            memberRepository.saveAll(List.of(member1, member2));

            team1 = new Team("team1", member1, LocalDateTime.now().minusDays(1).toLocalDate(), LocalDateTime.now().plusDays(1).toLocalDate(), FeedbackType.ANONYMOUS, LocalDate.now());
            team2 = new Team("team2", member2, LocalDateTime.now().minusDays(1).toLocalDate(), LocalDateTime.now().plusDays(1).toLocalDate(), FeedbackType.ANONYMOUS, LocalDate.now());
            teamRepository.saveAll(List.of(team1, team2));
        }

        @Test
        @DisplayName("모든 피드백 최신순 조회 성공")
        void test1() {
            // given
            Member sender = member1;
            Member receiver = member2;
            for (int i = 0; i < 5; i++) {
                Feedback feedback;
                boolean liked = i % 2 == 0;
                feedback = createFeedback(sender, receiver, team1, liked);
                feedbackRepository.save(feedback);
                feedback = createFeedback(sender, receiver, team2, liked);
                feedbackRepository.save(feedback);
            }

            for (int i = 0; i < 5; i++) {
                feedbackRepository.save(createFeedback(receiver, sender, team1, false));
            }

            int page = 0;
            int pageSize = 4;
            Sort.Direction order = Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page, pageSize);

            // when
            Page<Feedback> result = feedbackQueryRepository.findSentFeedbacks(sender.getId(), null, false, pageable, order);

            // then
            assertThat(result.getContent()).hasSize(pageSize);
            assertThat(result.hasNext()).isTrue();
            assertThat(result.getNumber()).isEqualTo(page);
            assertThat(result.getTotalElements()).isEqualTo(10);
            List<Feedback> feedbacks = result.getContent();
            assertThat(feedbacks).hasSize(pageSize);
            assertThat(feedbacks).extracting(Feedback::getId).isSortedAccordingTo(Comparator.reverseOrder());
            assertThat(feedbacks).extracting(Feedback::getSender).allSatisfy(s -> assertEqualSender(sender, s));
            assertThat(feedbacks).extracting(f -> f.getTeam().getName()).containsExactly("team2", "team1", "team2", "team1");
            assertThat(feedbacks).extracting(Feedback::isLiked).containsExactly(true, true, false, false);
        }

        @Test
        @DisplayName("팀 id로 필터링 조회 성공")
        void test2() {
            // given
            Member sender = member1;
            Member receiver = member2;
            for (int i = 0; i < 5; i++) {
                Feedback feedback;
                boolean liked = i % 2 == 0;
                feedback = createFeedback(sender, receiver, team1, liked);
                feedbackRepository.save(feedback);
                feedback = createFeedback(sender, receiver, team2, liked);
                feedbackRepository.save(feedback);
            }

            for (int i = 0; i < 5; i++) {
                feedbackRepository.save(createFeedback(receiver, sender, team1, false));
            }

            int page = 0;
            int pageSize = 4;
            Sort.Direction order = Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page, pageSize);

            // when
            Page<Feedback> result = feedbackQueryRepository.findSentFeedbacks(sender.getId(), team1.getId(), false, pageable, order);

            // then
            assertThat(result.getContent()).hasSize(pageSize);

            assertThat(result.hasNext()).isTrue();
            assertThat(result.getNumber()).isEqualTo(page);
            assertThat(result.getTotalElements()).isEqualTo(5);
            List<Feedback> feedbacks = result.getContent();
            assertThat(feedbacks).hasSize(pageSize);
            assertThat(feedbacks).extracting(Feedback::getId).isSortedAccordingTo(Comparator.reverseOrder());
            assertThat(feedbacks).extracting(Feedback::getSender).allSatisfy(s -> assertEqualSender(sender, s));
            assertThat(feedbacks).extracting(f -> f.getTeam().getName()).containsOnly("team1");
            assertThat(feedbacks).extracting(Feedback::isLiked).containsExactly(true, false, true, false);
        }

        @Test
        @DisplayName("좋아요 여부로 필터링 조회 성공")
        void test3() {
            // given
            Member sender = member1;
            Member receiver = member2;

            feedbackRepository.save(createFeedback(sender, receiver, team1, true));
            feedbackRepository.save(createFeedback(sender, receiver, team2, true));
            feedbackRepository.save(createFeedback(sender, receiver, team1, false));
            feedbackRepository.save(createFeedback(sender, receiver, team1, false));
            feedbackRepository.save(createFeedback(sender, receiver, team2, true));
            feedbackRepository.save(createFeedback(sender, receiver, team2, true));


            for (int i = 0; i < 5; i++) {
                feedbackRepository.save(createFeedback(receiver, sender, team1, false));
            }

            int page = 0;
            int pageSize = 3;
            Sort.Direction order = Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, pageSize);

            // when
            Page<Feedback> result = feedbackQueryRepository.findSentFeedbacks(sender.getId(), null, true, pageable, order);

            // then
            assertThat(result.getContent()).hasSize(pageSize);
            assertThat(result.hasNext()).isTrue();
            assertThat(result.getNumber()).isEqualTo(page);
            assertThat(result.getTotalElements()).isEqualTo(4);
            List<Feedback> feedbacks = result.getContent();
            assertThat(feedbacks).hasSize(pageSize);
            assertThat(feedbacks).extracting(Feedback::getId).isSortedAccordingTo(Comparator.naturalOrder());
            assertThat(feedbacks).extracting(Feedback::getSender).allSatisfy(s -> assertEqualSender(sender, s));
            assertThat(feedbacks).extracting(f -> f.getTeam().getName()).containsExactly("team1", "team2", "team2");
            assertThat(feedbacks).extracting(Feedback::isLiked).containsOnly(true);
        }

        @Test
        @DisplayName("과거순 정렬 조회 성공")
        void test4() {
            // given
            Member sender = member1;
            Member receiver = member2;
            for (int i = 0; i < 5; i++) {
                Feedback feedback;
                boolean liked = i % 2 == 0;
                feedback = createFeedback(sender, receiver, team1, liked);
                feedbackRepository.save(feedback);
                feedback = createFeedback(sender, receiver, team2, liked);
                feedbackRepository.save(feedback);
            }

            for (int i = 0; i < 5; i++) {
                feedbackRepository.save(createFeedback(receiver, sender, team1, false));
            }

            int page = 0;
            int pageSize = 4;
            Sort.Direction order = Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, pageSize);

            // when
            Page<Feedback> result = feedbackQueryRepository.findSentFeedbacks(sender.getId(), null, false, pageable, order);

            // then
            assertThat(result.getContent()).hasSize(pageSize);
            assertThat(result.hasNext()).isTrue();
            assertThat(result.getNumber()).isEqualTo(page);
            assertThat(result.getTotalElements()).isEqualTo(10);
            List<Feedback> feedbacks = result.getContent();
            assertThat(feedbacks).hasSize(pageSize);
            assertThat(feedbacks).extracting(Feedback::getId).isSortedAccordingTo(Comparator.naturalOrder());
            assertThat(feedbacks).extracting(Feedback::getSender).allSatisfy(s -> assertEqualSender(sender, s));
            assertThat(feedbacks).extracting(f -> f.getTeam().getName()).containsExactly("team1", "team2", "team1", "team2");
            assertThat(feedbacks).extracting(Feedback::isLiked).containsExactly(true, true, false, false);
        }

    }


}