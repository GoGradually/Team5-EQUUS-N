package com.feedhanjum.back_end.auth.exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Order(1)
@RestControllerAdvice
public class AuthControllerAdvice {

    /**
     * 이메일이 존재하는 경우
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> emailAlreadyExists(EmailAlreadyExistsException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "EMAIL_ALREADY_EXISTS");
        errorResponse.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * 아이디 혹은 비밀번호가 일치하지 않아, 자격 증명에 실패했을 경우
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> invalidCredentials(InvalidCredentialsException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "INVALID_CREDENTIALS");
        errorResponse.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * 사용자가 로그인 상태가 아닐 경우
     */
    @ExceptionHandler(LoginStateRequiredException.class)
    public ResponseEntity<Map<String, String>> loginStateRequired(LoginStateRequiredException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "LOGIN_STATE_REQUIRED");
        errorResponse.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * 회원가입 토큰 검증이 실패한 경우
     */
    @ExceptionHandler(SignupTokenNotValidException.class)
    public ResponseEntity<Map<String, String>> signupTokenNotValid(SignupTokenNotValidException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "SIGNUP_TOKEN_NOT_VALID");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * 회원가입 토큰 인증을 하지 않고 회원가입을 시도한 경우
     */
    @ExceptionHandler(SignupTokenVerifyRequiredException.class)
    public ResponseEntity<Map<String, String>> signupTokenNotValid(SignupTokenVerifyRequiredException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "SIGNUP_TOKEN_VERIFY_REQUIRED");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * 비밀번호 초기화 토큰 검증에 실패한 경우
     */
    @ExceptionHandler(PasswordResetTokenNotValidException.class)
    public ResponseEntity<Map<String, String>> passwordTokenNotValid(PasswordResetTokenNotValidException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "PASSWORD_RESET_TOKEN_NOT_VALID");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * 비밀번호 초기화 토큰 인증을 하지 않고 비밀번호 초기화를 시도한 경우
     */
    @ExceptionHandler(PasswordResetTokenVerifyRequiredException.class)
    public ResponseEntity<Map<String, String>> passwordTokenNotValid(PasswordResetTokenVerifyRequiredException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "PASSWORD_RESET_TOKEN_VERIFY_REQUIRED");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * 이메일로 가입하지 않은 사용자가 비밀번호 변경을 시도한 경우
     */
    @ExceptionHandler(PasswordChangeNotAllowedException.class)
    public ResponseEntity<Map<String, String>> passwordChangeNotAllowed(PasswordChangeNotAllowedException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "PASSWORD_CHANGE_NOT_ALLOWED");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
