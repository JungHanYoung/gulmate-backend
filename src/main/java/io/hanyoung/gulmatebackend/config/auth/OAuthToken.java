package io.hanyoung.gulmatebackend.config.auth;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
public class OAuthToken {

    private String accessToken;
    private OAuthProvider provider;

    @Builder
    public OAuthToken(String accessToken, OAuthProvider provider) {
        this.accessToken = accessToken;
        this.provider = provider;
    }
}
