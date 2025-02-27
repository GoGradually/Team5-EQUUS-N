package com.feedhanjum.back_end.core.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ByteLengthValidator implements ConstraintValidator<ByteLength, String> {
    private int min;
    private int max;
    private boolean strip;

    @Override
    public void initialize(ByteLength constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
        strip = constraintAnnotation.strip();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null 값은 다른 어노테이션에서 처리될 수 있음
        }
        if (strip) {
            value = value.strip();
        }
        int byteLength = calculateByteLength(value);
        return min <= byteLength && byteLength <= max;
    }

    private int calculateByteLength(String value) {
        int byteLength = 0;
        for (char c : value.toCharArray()) {
            if (c <= 127) {
                byteLength += 1; // 아스키 문자는 1바이트
            } else {
                byteLength += 2; // 그 외 유니코드 문자는 2바이트
            }
        }
        return byteLength;
    }
}
