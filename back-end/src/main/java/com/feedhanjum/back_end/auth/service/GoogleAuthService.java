package com.feedhanjum.back_end.auth.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.feedhanjum.back_end.auth.config.property.GoogleAuthProperties;
import com.feedhanjum.back_end.auth.exception.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Profile({"dev", "prod"})
@Service
public class GoogleAuthService {
    private final GoogleAuthProperties googleAuthProperties;
    private final RestClient restClient;

    private static final String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
    private static final String tokenUrl = "https://oauth2.googleapis.com/token";

    public String getGoogleLoginUrl() {
        return UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/auth")
                .queryParam("client_id", googleAuthProperties.getClientId())
                .queryParam("redirect_uri", googleAuthProperties.getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", "profile email")
                .toUriString();
    }

    public GoogleUserInfoResponse getUserInfo(String googleCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", googleCode);
        params.add("client_id", googleAuthProperties.getClientId());
        params.add("client_secret", googleAuthProperties.getClientSecret());
        params.add("redirect_uri", googleAuthProperties.getRedirectUri());
        params.add("grant_type", "authorization_code");

        GoogleCodeResponse codeResponse = restClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        (request, response) -> {
                            throw new InvalidCredentialsException("구글 로그인 정보가 잘못되었습니다");
                        }
                )
                .body(GoogleCodeResponse.class);

        String accessToken = codeResponse.accessToken();


        GoogleUserInfoResponse userInfo = restClient.get()
                .uri(userInfoUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        (request, response) -> {
                            throw new InvalidCredentialsException("구글 로그인 정보가 잘못되었습니다");
                        }
                )
                .body(GoogleUserInfoResponse.class);
        log.info("Google OAuth result: {}", userInfo);
        return userInfo;
    }


    record GoogleCodeResponse(
            @JsonProperty("access_token")
            String accessToken
    ) {
    }

    public record GoogleUserInfoResponse(
            @JsonProperty("email")
            String email,
            @JsonProperty("name")
            String name
    ) {

    }
}
