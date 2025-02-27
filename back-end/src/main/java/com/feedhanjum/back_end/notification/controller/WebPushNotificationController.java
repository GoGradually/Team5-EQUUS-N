package com.feedhanjum.back_end.notification.controller;

import com.feedhanjum.back_end.auth.infra.Login;
import com.feedhanjum.back_end.notification.controller.dto.SubscribePushNotificationRequest;
import com.feedhanjum.back_end.notification.controller.dto.response.ApplicationServerKeyResponse;
import com.feedhanjum.back_end.notification.service.WebPushService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/push-notification/")
public class WebPushNotificationController {

    private final WebPushService webPushService;

    @Operation(summary = "푸시 알림 구독")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "알림 구독 성공"),
    })
    @PostMapping(value = "/subscribe", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> subscribe(@Login Long receiverId, @RequestBody SubscribePushNotificationRequest request) {
        webPushService.subscribe(receiverId, request.subscription());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Application Server Key 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application Server Key 조회 성공"),
    })
    @GetMapping(value = "/application-server-key", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApplicationServerKeyResponse> getApplicationServerKey() {
        String applicationServerKey = webPushService.getApplicationServerKey();
        return ResponseEntity.ok(new ApplicationServerKeyResponse(applicationServerKey));
    }
}
