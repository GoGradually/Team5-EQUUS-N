package com.feedhanjum.back_end.auth.service;

import com.feedhanjum.back_end.auth.domain.PasswordResetToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class PasswordResetTokenService {
    private static final String TOKEN_PREFIX = "password_reset_token:";
    private final RedisTemplate<String, PasswordResetToken> redisTemplate;

    public void save(PasswordResetToken token) {
        redisTemplate.opsForValue().set(getTokenKey(token.getEmail(), token.getCode()), token, PasswordResetToken.EXPIRE_MINUTE, TimeUnit.MINUTES);
    }

    public Optional<PasswordResetToken> find(String email, String code) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(getTokenKey(email, code)));
    }

    public void delete(PasswordResetToken token) {
        redisTemplate.delete(getTokenKey(token.getEmail(), token.getCode()));
    }

    private String getTokenKey(String email, String code) {
        return TOKEN_PREFIX + email + "," + code;
    }
}
