package com.feedhanjum.back_end.core.constraints;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 바이트 길이 제약 조건을 지정하는 애노테이션.
 */
@Constraint(validatedBy = ByteLengthValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ByteLength {
    // 최소 길이
    int min() default 0;

    // 최대 길이
    int max() default Integer.MAX_VALUE;

    // 문자열 앞 뒤 공백 제거 여부
    boolean strip() default false;

    String message() default "byte length must be between {min} and {max}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
