package io.hanyoung.gulmatebackend.config.auth;

import io.hanyoung.gulmatebackend.config.auth.dto.OAuthTokenRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class OAuthAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/api/v1/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody OAuthTokenRequestDto requestDto) {

        OAuthAuthenticationToken authenticate = (OAuthAuthenticationToken)authenticationManager.authenticate(requestDto.toAuthenticationToken());

        if(authenticate.getClass().isAssignableFrom(OAuthAuthenticationToken.class)) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", authenticate.getPrincipal());
            String token = jwtUtils.generateToken(claims);
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(OAuthProvider.class, new OAuthProviderEnumTypeEditor());
    }

}
