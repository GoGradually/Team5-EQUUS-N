package com.feedhanjum.back_end.auth.passwordencoder;

public interface PasswordEncoder {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
