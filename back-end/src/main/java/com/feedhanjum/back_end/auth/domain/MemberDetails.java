package com.feedhanjum.back_end.auth.domain;

import com.feedhanjum.back_end.auth.exception.InvalidCredentialsException;
import com.feedhanjum.back_end.auth.exception.PasswordChangeNotAllowedException;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class MemberDetails {
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 20;
    public static final int MIN_NAME_BYTE = 1;
    public static final int MAX_NAME_BYTE = 10;

    @Id
    @Column(name = "member_details_id")
    private Long id;

    private String email;

    @Nullable
    private String password;

    @Enumerated(EnumType.STRING)
    private Type accountType;

    // 이메일 유저
    private MemberDetails(Long id, String email, String password) {
        this.id = id;
        this.accountType = Type.EMAIL;
        this.email = email;
        this.password = password;
    }

    // 구글 유저
    private MemberDetails(Long id, String email) {
        this.id = id;
        this.email = email;
        this.accountType = Type.GOOGLE;
    }

    public void changePassword(String newPassword) {
        if (!isEmailAccount())
            throw new PasswordChangeNotAllowedException("이메일로 가입했을 경우만 비밀번호를 변경할 수 있습니다");
        password = newPassword;
    }

    private boolean isEmailAccount() {
        return accountType == Type.EMAIL;

    }

    private boolean isGoogleAccount() {
        return accountType == Type.GOOGLE;
    }

    public void validateGoogleAccount() {
        if (!isGoogleAccount())
            throw new InvalidCredentialsException("잘못된 요청입니다");
    }

    public void validateEmailAccount() {
        if (!isEmailAccount())
            throw new InvalidCredentialsException("잘못된 요청입니다");

    }

    public static MemberDetails createEmailUser(Long id, String email, String password) {
        return new MemberDetails(id, email, password);
    }

    public static MemberDetails createGoogleUser(Long id, String email) {
        return new MemberDetails(id, email);
    }


    public enum Type {
        EMAIL, GOOGLE
    }
}
