package com.feedhanjum.back_end.schedule.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Embeddable
@NoArgsConstructor
@Getter
public class Todo {
    @Size(min = 1, max = 50)
    private String content;

    public Todo(String content) {
        this.content = content;
    }
}
