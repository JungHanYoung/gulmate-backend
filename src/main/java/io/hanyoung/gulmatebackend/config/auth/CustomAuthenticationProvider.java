package io.hanyoung.gulmatebackend.config.auth;

import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.account.AccountRepository;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AccountRepository accountRepository;
    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        restTemplate = new RestTemplate();
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        OAuthAuthenticationToken customAuth = (OAuthAuthenticationToken) authentication;

        OAuthToken credentials = customAuth.getCredentials();

        System.out.println("accessToken : " + credentials.getAccessToken());
        System.out.println("provider : " + credentials.getProvider());
        OAuthProvider provider = credentials.getProvider();
        RequestEntity<?> request;
        String email, name, photoUrl;
        ResponseEntity<Map> response = null;
        switch (provider) {
            case GOOGLE:
                request = RequestEntity
                        .get(URI.create("https://www.googleapis.com/oauth2/v3/userinfo?oauth_token=" + credentials.getAccessToken()))
                        .build();
                response = restTemplate.exchange(request, Map.class);
                break;
            case FACEBOOK:
                request = RequestEntity
                        .get(URI.create("https://graph.facebook.com/me?fields=id,name,email,picture&access_token=" + credentials.getAccessToken()))
                        .build();
                response = restTemplate.exchange(request, Map.class);
                break;
            default:
                break;
        }

        if(response.getBody() != null && response.getStatusCode() == HttpStatus.OK) {
            OAuthResponseDto dto = OAuthResponseDto.of(response, provider);
            Account account = saveOrUpdate(dto);
            return new OAuthAuthenticationToken(account.getId(), null, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        }

        return null;
    }

    private Account saveOrUpdate(OAuthResponseDto dto) {
        Account account = accountRepository.findByEmail(dto.email)
                .orElse(Account.builder()
                                .email(dto.email)
                                .name(dto.name)
                                .photoUrl(dto.photoUrl)
                                .build());
        return accountRepository.save(account);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuthAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Getter
    private static class OAuthResponseDto {
        private String email;
        private String name;
        private String photoUrl;

        public static OAuthResponseDto of(ResponseEntity<Map> response, OAuthProvider provider) {
            assert response != null;
            assert provider != null;
            if(provider == OAuthProvider.FACEBOOK) {
                return ofFacebook(response);

            } else if(provider == OAuthProvider.GOOGLE) {
                return ofGoogle(response);
            }
            throw new IllegalArgumentException("Error: 요청으로 들어온 provider를 지원하지 않습니다");
        }

        private static OAuthResponseDto ofFacebook(ResponseEntity<Map> response) {
            Map<String, Object> body = response.getBody();
            assert body != null;
            String email = (String) body.get("email");
            String name = (String) body.get("name");
            String photoUrl = (String) ((Map<String, Object>)((Map<String, Object>)response.getBody().get("picture")).get("data")).get("url");

            return OAuthResponseDto.builder()
                    .email(email)
                    .name(name)
                    .photoUrl(photoUrl)
                    .build();
        }

        private static OAuthResponseDto ofGoogle(ResponseEntity<Map> response) {
            Map body = response.getBody();
            assert body != null;
            String email = (String) body.get("email");
            String name = (String) body.get("name");
            String photoUrl = (String) body.get("picture");

            return OAuthResponseDto.builder()
                    .email(email)
                    .name(name)
                    .photoUrl(photoUrl)
                    .build();
        }

        @Builder
        public OAuthResponseDto(String email, String name, String photoUrl) {
            this.email = email;
            this.name = name;
            this.photoUrl = photoUrl;
        }
    }
}
