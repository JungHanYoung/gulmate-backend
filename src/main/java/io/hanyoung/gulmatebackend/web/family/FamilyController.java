package io.hanyoung.gulmatebackend.web.family;

import io.hanyoung.gulmatebackend.config.auth.OAuthAuthenticationToken;
import io.hanyoung.gulmatebackend.config.web.AuthUser;
import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.account.AccountRepository;
import io.hanyoung.gulmatebackend.domain.family.Family;
import io.hanyoung.gulmatebackend.domain.family.FamilyRepository;
import io.hanyoung.gulmatebackend.domain.family.FamilyType;
import io.hanyoung.gulmatebackend.web.dto.FamilyJoinRequestDto;
import io.hanyoung.gulmatebackend.web.dto.FamilySaveRequestDto;
import io.hanyoung.gulmatebackend.web.family.FamilyTypeEnumEditor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@RestController
public class FamilyController {

    private final FamilyRepository familyRepository;
    private final AccountRepository accountRepository;


    @GetMapping("/api/v1/family/me")
    public ResponseEntity<?> getMyFamily(@AuthUser Account account) {
//        OAuthAuthenticationToken auth = (OAuthAuthenticationToken) authentication;
//
//        Account account = accountRepository.findById(auth.getAccountId())
//                .orElseThrow(() -> new IllegalStateException("No User exists"));

        Family family = account.getFamily();

        return family != null
                ? ResponseEntity.ok(family)
                : ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
    }

    @PostMapping("/api/v1/family")
    public ResponseEntity<?> createFamily(
            @AuthUser Account account,
            @RequestBody FamilySaveRequestDto requestDto) {
//        OAuthAuthenticationToken auth = (OAuthAuthenticationToken) authentication;
//        Account account = accountRepository.findById(auth.getPrincipal())
//                .orElseThrow(() -> new IllegalArgumentException("No account is exist"));
        String generated;
        do {
            generated = generateRandomString();
        } while (familyRepository.existsByInviteKey(generated));

        Family savedFamily = familyRepository.save(
                Family.builder()
                        .familyName(requestDto.getFamilyName())
                        .familyType(requestDto.getFamilyType())
                        .inviteKey(generated)
                        .build()
        );

        savedFamily.addAccount(account);
        account.setFamily(savedFamily);
        accountRepository.save(account);

        return ResponseEntity.ok(savedFamily);

    }

    @PutMapping("/api/v1/family/join")
    public ResponseEntity<?> joinFamily(
            @AuthUser Account account,
            @RequestBody FamilyJoinRequestDto requestDto) {

        Optional<Family> byInviteKey = familyRepository.findByInviteKey(requestDto.getInviteKey());

        Family family = byInviteKey.orElseThrow(() -> new IllegalArgumentException("Invaild invite key"));
        account.setFamily(family);
        accountRepository.save(account);

        System.out.println(account);


        return ResponseEntity.ok().build();
    }

    private String generateRandomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(FamilyType.class, new FamilyTypeEnumEditor());
    }


}
