package io.hanyoung.gulmatebackend.web.account;

import io.hanyoung.gulmatebackend.config.web.AuthUser;
import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.account.AccountRepository;
import io.hanyoung.gulmatebackend.web.account.dto.AccountResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/api/v1/me")
    public ResponseEntity<?> me(@AuthUser Account account) {

        return ResponseEntity.ok(new AccountResponseDto(account));
    }

}
