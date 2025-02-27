package com.feedhanjum.back_end.notification.controller;

import com.feedhanjum.back_end.auth.infra.Login;
import com.feedhanjum.back_end.notification.controller.dto.request.MultipleNotificationReadRequest;
import com.feedhanjum.back_end.notification.controller.dto.response.InAppNotificationDto;
import com.feedhanjum.back_end.notification.service.InAppNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class InAppNotificationController {
    private final InAppNotificationService inAppNotificationService;

    @Operation(summary = "알림 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 조회 성공"),
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InAppNotificationDto>> getNotification(@Login Long receiverId) {
        List<InAppNotificationDto> notifications = inAppNotificationService.getInAppNotifications(receiverId);
        return ResponseEntity.ok(notifications);
    }


    @Operation(summary = "여러 알림 일괄 읽음 처리")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "알림 읽음 처리 성공"),
    })
    @PostMapping(value = "/mark-as-read", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> readNotifications(@Login Long receiverId, @Valid @RequestBody MultipleNotificationReadRequest readRequest) {
        inAppNotificationService.readInAppNotifications(receiverId, readRequest.notificationIds());
        return ResponseEntity.noContent().build();
    }


}
