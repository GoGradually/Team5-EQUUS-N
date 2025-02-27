package com.feedhanjum.back_end.feedback.controller;

import com.feedhanjum.back_end.auth.infra.Login;
import com.feedhanjum.back_end.core.dto.Paged;
import com.feedhanjum.back_end.feedback.controller.dto.request.RetrospectWriteRequest;
import com.feedhanjum.back_end.feedback.controller.dto.request.RetrospectsQueryRequest;
import com.feedhanjum.back_end.feedback.controller.dto.response.RetrospectResponse;
import com.feedhanjum.back_end.feedback.domain.Retrospect;
import com.feedhanjum.back_end.feedback.service.RetrospectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/retrospect")
public class RetrospectController {
    private final RetrospectService retrospectService;


    @Operation(summary = "회고 조회하기", description = "회고를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회고 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "403", description = "본인이 아닌 경우", content = @Content)
    })
    @GetMapping(value = "/{writerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Paged<RetrospectResponse>> getRetrospects(@Login Long loginId,
                                                                    @PathVariable Long writerId,
                                                                    @ParameterObject @Valid RetrospectsQueryRequest request) {
        if (!Objects.equals(loginId, writerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Page<Retrospect> retrospects = retrospectService.getRetrospects(writerId, request.teamId(), request.page(), request.sortOrder());
        return ResponseEntity.ok(Paged.from(retrospects.map(RetrospectResponse::from)));
    }

    @Operation(summary = "회고 작성하기", description = "회고를 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회고 작성 성공", content = @Content),
            @ApiResponse(responseCode = "403", description = "본인이 아닌 경우", content = @Content)
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> writeRetrospect(@Login Long loginId,
                                                @Valid @RequestBody RetrospectWriteRequest request) {
        if (!Objects.equals(loginId, request.writerId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        retrospectService.writeRetrospect(request.title(), request.content(), request.writerId(), request.teamId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
