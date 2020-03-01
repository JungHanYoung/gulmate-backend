package io.hanyoung.gulmatebackend.web.family;

import io.hanyoung.gulmatebackend.config.web.AuthUser;
import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.family.Family;
import io.hanyoung.gulmatebackend.domain.family.FamilyType;
import io.hanyoung.gulmatebackend.web.family.dto.FamilyJoinRequestDto;
import io.hanyoung.gulmatebackend.web.family.dto.FamilySaveRequestDto;
import io.hanyoung.gulmatebackend.web.family.dto.FamilyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class FamilyController {

    private final FamilyService familyService;

    @GetMapping("/api/v1/family/me")
    public ResponseEntity<?> getMyFamily(@AuthUser Account account) {

        Family family = account.getFamily();

        return family != null
                ? ResponseEntity.ok(new FamilyResponseDto(family))
                : ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
    }

    @PostMapping("/api/v1/family")
    public ResponseEntity<?> createFamily(
            @AuthUser Account account,
            @RequestBody FamilySaveRequestDto requestDto) {

        FamilyResponseDto responseDto = familyService.createFamily(account, requestDto);

        return ResponseEntity.ok(responseDto);

    }

    @PostMapping("/api/v1/family/join")
    public ResponseEntity<?> joinFamily(
            @AuthUser Account account,
            @RequestBody FamilyJoinRequestDto requestDto) {

        FamilyResponseDto responseDto = familyService.joinFamily(account, requestDto.getInviteKey());

        return ResponseEntity.ok(responseDto);
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(FamilyType.class, new FamilyTypeEnumEditor());
    }
}
