package io.hanyoung.gulmatebackend.web.account;

import io.hanyoung.gulmatebackend.config.web.AuthUser;
import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.account.AccountRepository;
import io.hanyoung.gulmatebackend.web.account.dto.AccountResponseDto;
import io.hanyoung.gulmatebackend.web.account.dto.AccountUpdateRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthUser Account account) {

        return ResponseEntity.ok(new AccountResponseDto(account));
    }

}
