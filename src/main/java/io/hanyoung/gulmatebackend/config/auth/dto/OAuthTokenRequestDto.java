package io.hanyoung.gulmatebackend.config.auth.dto;

import io.hanyoung.gulmatebackend.config.auth.OAuthAuthenticationToken;
import io.hanyoung.gulmatebackend.config.auth.OAuthProvider;
import io.hanyoung.gulmatebackend.config.auth.OAuthToken;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.Authentication;

@Getter
public class OAuthTokenRequestDto {

    private String accessToken;
    private OAuthProvider provider;

    @Builder
    public OAuthTokenRequestDto(String accessToken, OAuthProvider provider) {
        this.accessToken = accessToken;
        this.provider = provider;
    }

    public Authentication toAuthenticationToken() {
        return new OAuthAuthenticationToken(null,
                OAuthToken.builder()
                        .accessToken(accessToken)
                        .provider(provider)
                        .build());
    }
}
