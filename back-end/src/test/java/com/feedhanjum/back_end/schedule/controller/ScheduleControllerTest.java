package com.feedhanjum.back_end.schedule.controller;

import com.feedhanjum.back_end.schedule.controller.dto.ScheduleRequest;
import com.feedhanjum.back_end.schedule.domain.Todo;
import com.feedhanjum.back_end.schedule.service.ScheduleService;
import com.feedhanjum.back_end.schedule.service.dto.ScheduleRequestDto;
import com.feedhanjum.back_end.teamplanorchestration.service.TeamPlanOrchestrationService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class ScheduleControllerTest {

    @InjectMocks
    private ScheduleController scheduleController;

    @Mock
    private TeamPlanOrchestrationService teamPlanOrchestrationService;

    @Mock
    private ScheduleService scheduleService;
    @Test
    @DisplayName("일정 생성 컨트롤러 성공 테스트")
    void scheduleController_createSchedule_성공() {
        // given
        Long memberId = 1L;
        Long teamId = 2L;
        Todo hehe = new Todo("hehe");
        ScheduleRequest request = new ScheduleRequest("haha", LocalDateTime.now(), LocalDateTime.now().plusDays(10), List.of(hehe));
        ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto(request);
        doNothing().when(teamPlanOrchestrationService).createSchedule(memberId, teamId, scheduleRequestDto);

        // when
        ResponseEntity<Void> result = scheduleController.createSchedule(memberId, teamId, request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(teamPlanOrchestrationService).createSchedule(memberId, teamId, new ScheduleRequestDto(request));
    }
    @Test
    @DisplayName("스케줄 수정 요청 성공")
    void updateSchedule_성공() {
        // given
        Long memberId = 1L;
        Long teamId = 2L;
        Long scheduleId = 3L;
        LocalDateTime now = LocalDateTime.now();
        Todo hehe = new Todo("hehe");
        ScheduleRequest request = new ScheduleRequest("haha", now.plusHours(1), now.plusHours(2), List.of(hehe));

        // when
        ResponseEntity<Void> response = scheduleController.updateSchedule(memberId, teamId, scheduleId, request);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("스케줄 수정 요청 실패: 할 일이 너무 김")
    void updateSchedule_할일TooLong() {
        // given
        Long memberId = 1L;
        Long teamId = 2L;
        Long scheduleId = 3L;
        LocalDateTime now = LocalDateTime.now();
        Todo hehe = new Todo("012345678901234567890123456789012345678901234567890123456789");
        ScheduleRequest request = new ScheduleRequest("haha", now.plusHours(1), now.plusHours(2), List.of(hehe));

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        // when
        Assertions.assertThatThrownBy(() -> validator.validate(request, ScheduleRequest.class))
                .isInstanceOf(Exception.class);
    }
}