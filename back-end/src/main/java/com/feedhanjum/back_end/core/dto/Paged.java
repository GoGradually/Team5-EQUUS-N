package com.feedhanjum.back_end.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

public record Paged<T>(
        @Schema(description = "현재 페이지 번호")
        int page,
        @Schema(description = "다음 페이지가 있는지 여부")
        boolean hasNext,
        @Schema(description = "페이지 내용")
        List<T> content
) {

    public static <T> Paged<T> from(Page<T> page) {
        return new Paged<>(page.getNumber(), page.hasNext(), page.getContent());
    }
}
