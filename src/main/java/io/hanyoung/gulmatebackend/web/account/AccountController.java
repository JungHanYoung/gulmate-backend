package io.hanyoung.gulmatebackend.web.account;

import io.hanyoung.gulmatebackend.config.auth.OAuthAuthenticationToken;
import io.hanyoung.gulmatebackend.config.web.AuthUser;
import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.account.AccountRepository;
import io.hanyoung.gulmatebackend.web.account.dto.AccountResponseDto;
import io.hanyoung.gulmatebackend.web.account.dto.AccountUpdateRequestDto;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Gulmate account API")
@RestController
@RequestMapping("/api/v1")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @ApiOperation(value = "account", notes = "인증된 사용자에게 프로필 정보를 제공")
    @ApiResponses({
            @ApiResponse(code = 200, message = "사용자 정보가 성공적으로 제공되었습니다.", response = AccountResponseDto.class),
            @ApiResponse(code = 403, message = "사용자 인증 후 이용해야합니다."),
            @ApiResponse(code = 500, message = "서버 에러 발생"),
    })
    @GetMapping("/me")
    public ResponseEntity<?> me() {
        OAuthAuthenticationToken authentication = (OAuthAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        Account account = accountService.findById(authentication.getAccountId());

        return ResponseEntity.ok(new AccountResponseDto(account));
    }

}
