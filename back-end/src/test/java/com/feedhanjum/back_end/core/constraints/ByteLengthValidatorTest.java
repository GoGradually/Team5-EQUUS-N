package com.feedhanjum.back_end.core.constraints;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("ByteLengthValidator 테스트")
class ByteLengthValidatorTest {
    ValidatorFactory factory;
    Validator validator;


    @BeforeEach
    void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterEach
    void tearDown() {
        factory.close();
    }

    @Test
    @DisplayName("한글 문자열 2개까지 통과")
    void test1() {
        // given
        String input = "한글";
        TestRecord testRecord = new TestRecord(input);

        // when
        Set<ConstraintViolation<TestRecord>> result = validator.validate(testRecord);

        // then
        assertThat(result).isEmpty();

    }

    @Test
    @DisplayName("한글 문자열 3개부터 실패")
    void test2() {
        // given
        String input = "한글자";
        TestRecord testRecord = new TestRecord(input);

        // when
        Set<ConstraintViolation<TestRecord>> result = validator.validate(testRecord);

        // then
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("영어 문자 5개까지 통과")
    void test3() {
        // given
        String input = "abcde";
        TestRecord testRecord = new TestRecord(input);

        // when
        Set<ConstraintViolation<TestRecord>> result = validator.validate(testRecord);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("영어 문자 6개부터 실패")
    void test4() {
        // given
        String input = "abcdef";
        TestRecord testRecord = new TestRecord(input);

        // when
        Set<ConstraintViolation<TestRecord>> result = validator.validate(testRecord);

        // then
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("한글 1개, 영어 3개까지 통과")
    void test5() {
        // given
        String input = "한aBc";
        TestRecord testRecord = new TestRecord(input);

        // when
        Set<ConstraintViolation<TestRecord>> result = validator.validate(testRecord);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("한글 2개, 영어 2개부터 실패")
    void test6() {
        // given
        String input = "한글aB";
        TestRecord testRecord = new TestRecord(input);

        // when
        Set<ConstraintViolation<TestRecord>> result = validator.validate(testRecord);

        // then
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("영어 1개 실패")
    void test7() {
        // given
        String input = "a";
        TestRecord testRecord = new TestRecord(input);

        // when
        Set<ConstraintViolation<TestRecord>> result = validator.validate(testRecord);

        // then
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("한글 1개 통과")
    void test8() {
        // given
        String input = "한";
        TestRecord testRecord = new TestRecord(input);

        // when
        Set<ConstraintViolation<TestRecord>> result = validator.validate(testRecord);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("앞뒤공백 포함한 한글 2개 통과")
    void test9() {
        // given
        String input = "  \n 한글    ";
        TestRecord testRecord = new TestRecord(input);

        // when
        Set<ConstraintViolation<TestRecord>> result = validator.validate(testRecord);

        // then
        assertThat(result).isEmpty();
    }

    record TestRecord(@ByteLength(min = 2, max = 5, strip = true)
                      String value) {

    }
}