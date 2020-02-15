package io.hanyoung.gulmatebackend.config.auth;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class OAuthAuthenticationToken extends AbstractAuthenticationToken {

    private Long accountId;
    private OAuthToken credentials;


    public OAuthAuthenticationToken(Long accountId, OAuthToken credentials) {
        super(null);
        super.setAuthenticated(false);
        this.accountId = accountId;
        this.credentials = credentials;
    }

    OAuthAuthenticationToken(Long accountId, OAuthToken credentials, Collection<GrantedAuthority> authorities) {
        super(authorities);
        super.setAuthenticated(true);
        this.accountId = accountId;
        this.credentials = credentials;
    }

    @Override
    public OAuthToken getCredentials() {
        return this.credentials;
    }

    @Override
    public Long getPrincipal() {
        return this.accountId;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }

}
