package com.feedhanjum.back_end.schedule.domain;

import com.feedhanjum.back_end.member.domain.Member;
import com.feedhanjum.back_end.team.domain.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class ScheduleTest {

    @Test
    @DisplayName("이름이 동일한 경우와 다른 경우를 비교한다")
    void isNameDifferent_이름비교() {
        // given
        Team team = mock(Team.class);
        Member owner = mock(Member.class);
        LocalDateTime startTime = LocalDateTime.of(2025, 2, 4, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 2, 4, 10, 0);
        Schedule schedule = new Schedule("haha", startTime, endTime, team, owner);

        // when
        boolean resultSame = schedule.isNameDifferent("haha");
        boolean resultDifferent = schedule.isNameDifferent("hoho");

        // then
        assertThat(resultSame).isFalse();
        assertThat(resultDifferent).isTrue();
    }

    @Test
    @DisplayName("스케줄이 종료된 경우 true 반환한다")
    void isEnd_종료() {
        // given
        Team team = mock(Team.class);
        Member owner = mock(Member.class);
        LocalDateTime startTime = LocalDateTime.of(2025, 2, 4, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 2, 4, 10, 0);
        Schedule schedule = new Schedule("haha", startTime, endTime, team, owner);
        LocalDateTime fixedNow = LocalDateTime.of(2025, 2, 4, 10, 30);

        // when
        try (MockedStatic<LocalDateTime> mocked = Mockito.mockStatic(LocalDateTime.class)) {
            mocked.when(LocalDateTime::now).thenReturn(fixedNow);
            boolean result = schedule.isEnd();

            // then
            assertThat(result).isTrue();
        }
    }

    @Test
    @DisplayName("스케줄이 아직 종료되지 않은 경우 false 반환한다")
    void isEnd_미종료() {
        // given
        Team team = mock(Team.class);
        Member owner = mock(Member.class);
        LocalDateTime startTime = LocalDateTime.of(2025, 2, 4, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 2, 4, 10, 0);
        Schedule schedule = new Schedule("haha", startTime, endTime, team, owner);
        LocalDateTime fixedNow = LocalDateTime.of(2025, 2, 4, 9, 30);

        // when
        try (MockedStatic<LocalDateTime> mocked = Mockito.mockStatic(LocalDateTime.class)) {
            mocked.when(LocalDateTime::now).thenReturn(fixedNow);
            boolean result = schedule.isEnd();

            // then
            assertThat(result).isFalse();
        }
    }

    @Test
    @DisplayName("스케줄의 이름을 변경할 수 있다")
    void changeName_이름변경() {
        // given
        Team team = mock(Team.class);
        Member owner = mock(Member.class);
        LocalDateTime startTime = LocalDateTime.of(2025, 2, 4, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 2, 4, 10, 0);
        Schedule schedule = new Schedule("haha", startTime, endTime, team, owner);

        // when
        schedule.changeName("hoho");

        // then
        assertThat(schedule.getName()).isEqualTo("hoho");
    }

    @Test
    @DisplayName("스케줄의 시작 시간과 종료 시간이 동일한지 여부를 비교한다")
    void isTimeDifferent_시간비교() {
        // given
        Team team = mock(Team.class);
        Member owner = mock(Member.class);
        LocalDateTime startTime = LocalDateTime.of(2025, 2, 4, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 2, 4, 10, 0);
        Schedule schedule = new Schedule("haha", startTime, endTime, team, owner);

        // when
        boolean sameTime = schedule.isTimeDifferent(startTime, endTime);
        boolean diffStart = schedule.isTimeDifferent(startTime.plusMinutes(1), endTime);
        boolean diffEnd = schedule.isTimeDifferent(startTime, endTime.plusMinutes(1));

        // then
        assertThat(sameTime).isFalse();
        assertThat(diffStart).isTrue();
        assertThat(diffEnd).isTrue();
    }

    @Test
    @DisplayName("스케줄의 시작 시간이 동일한지 여부를 비교한다")
    void isStartTimeDifferent_시작시간비교() {
        // given
        Team team = mock(Team.class);
        Member owner = mock(Member.class);
        LocalDateTime startTime = LocalDateTime.of(2025, 2, 4, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 2, 4, 10, 0);
        Schedule schedule = new Schedule("haha", startTime, endTime, team, owner);

        // when
        boolean sameStart = schedule.isStartTimeDifferent(startTime);
        boolean diffStart = schedule.isStartTimeDifferent(startTime.plusMinutes(1));

        // then
        assertThat(sameStart).isFalse();
        assertThat(diffStart).isTrue();
    }

    @Test
    @DisplayName("스케줄의 시간을 올바르게 변경할 수 있다")
    void setTime_유효() {
        // given
        Team team = mock(Team.class);
        Member owner = mock(Member.class);
        LocalDateTime originalStart = LocalDateTime.of(2025, 2, 4, 9, 0);
        LocalDateTime originalEnd = LocalDateTime.of(2025, 2, 4, 10, 0);
        Schedule schedule = new Schedule("haha", originalStart, originalEnd, team, owner);
        LocalDateTime newStart = LocalDateTime.of(2025, 2, 4, 11, 0);
        LocalDateTime newEnd = LocalDateTime.of(2025, 2, 4, 12, 0);

        // when
        schedule.setTime(newStart, newEnd);

        // then
        assertThat(schedule.getStartTime()).isEqualTo(newStart);
        assertThat(schedule.getEndTime()).isEqualTo(newEnd);
    }

    @Test
    @DisplayName("스케줄의 시작시간이나 종료시간이 10분 단위가 아니면 예외가 발생한다")
    void setTime_10분단위() {
        // given
        Team team = mock(Team.class);
        Member owner = mock(Member.class);
        LocalDateTime validStart = LocalDateTime.of(2025, 2, 4, 9, 0);
        LocalDateTime validEnd = LocalDateTime.of(2025, 2, 4, 10, 0);
        Schedule schedule = new Schedule("haha", validStart, validEnd, team, owner);
        LocalDateTime invalidStart = LocalDateTime.of(2025, 2, 4, 11, 10, 5);
        LocalDateTime validNewEnd = LocalDateTime.of(2025, 2, 4, 12, 0);

        // when
        // then
        assertThatThrownBy(() -> schedule.setTime(invalidStart, validNewEnd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("일정은 10분 단위로 설정해야 합니다.");

        // given
        LocalDateTime validNewStart = LocalDateTime.of(2025, 2, 4, 11, 0);
        LocalDateTime invalidEnd = LocalDateTime.of(2025, 2, 4, 12, 5, 0);

        // when
        // then
        assertThatThrownBy(() -> schedule.setTime(validNewStart, invalidEnd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("일정은 10분 단위로 설정해야 합니다.");
    }

    @Test
    @DisplayName("스케줄의 시작 시간이 종료 시간과 같거나 늦으면 예외가 발생한다")
    void setTime_시간순서() {
        // given
        Team team = mock(Team.class);
        Member owner = mock(Member.class);
        LocalDateTime validStart = LocalDateTime.of(2025, 2, 4, 9, 0);
        LocalDateTime validEnd = LocalDateTime.of(2025, 2, 4, 10, 0);
        Schedule schedule = new Schedule("haha", validStart, validEnd, team, owner);
        LocalDateTime newStart = LocalDateTime.of(2025, 2, 4, 11, 0);
        LocalDateTime newEnd = LocalDateTime.of(2025, 2, 4, 10, 0);

        // when
        // then
        assertThatThrownBy(() -> schedule.setTime(newStart, newEnd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작 시간은 종료 시간보다 이전이어야 합니다.");

        // given
        LocalDateTime equalTime = LocalDateTime.of(2025, 2, 4, 11, 0);

        // when
        // then
        assertThatThrownBy(() -> schedule.setTime(equalTime, equalTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작 시간은 종료 시간보다 이전이어야 합니다.");
    }

    @Test
    @DisplayName("스케줄의 시작 날짜와 종료 날짜가 다르면 예외가 발생한다")
    void setTime_날짜불일치() {
        // given
        Team team = mock(Team.class);
        Member owner = mock(Member.class);
        LocalDateTime validStart = LocalDateTime.of(2025, 2, 4, 9, 0);
        LocalDateTime validEnd = LocalDateTime.of(2025, 2, 4, 10, 0);
        Schedule schedule = new Schedule("haha", validStart, validEnd, team, owner);
        LocalDateTime newStart = LocalDateTime.of(2025, 2, 4, 11, 0);
        LocalDateTime newEnd = LocalDateTime.of(2025, 2, 5, 12, 0);

        // when
        // then
        assertThatThrownBy(() -> schedule.setTime(newStart, newEnd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작 날짜와 종료 날짜는 동일해야 합니다.");
    }
}
