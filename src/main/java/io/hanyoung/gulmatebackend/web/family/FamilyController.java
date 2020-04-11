package io.hanyoung.gulmatebackend.web.family;

import io.hanyoung.gulmatebackend.config.web.AuthUser;
import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.family.FamilyType;
import io.hanyoung.gulmatebackend.service.UploadService;
import io.hanyoung.gulmatebackend.web.exception.ResourceNotFoundException;
import io.hanyoung.gulmatebackend.web.family.dto.FamilySaveRequestDto;
import io.hanyoung.gulmatebackend.web.family.dto.FamilyResponseDto;
import io.hanyoung.gulmatebackend.web.family.dto.FamilyUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/family")
public class FamilyController {

    private final FamilyService familyService;
    private final UploadService uploadService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyFamily(@AuthUser Account account) throws ResourceNotFoundException {
        FamilyResponseDto responseDto = familyService.getCurrentFamily(account);

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{familyId}")
    public ResponseEntity<?> modifyMyInfoFromFamily(@AuthUser Account account, @PathVariable Long familyId, @RequestBody FamilyUpdateRequestDto requestDto) {
        familyService.modifyMemberInfo(account, familyId, requestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> createFamily(
            @AuthUser Account account,
            @RequestBody FamilySaveRequestDto requestDto) {

        FamilyResponseDto responseDto = familyService.createFamily(account, requestDto);

        return ResponseEntity.ok(responseDto);

    }

    @PostMapping("/join")
    public ResponseEntity<?> joinFamily(
            @AuthUser Account account,
            @RequestParam("inviteKey") String inviteKey) {

        FamilyResponseDto responseDto = familyService.joinFamily(account, inviteKey);

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/withdraw")
    public ResponseEntity<?> withdraw(
            @AuthUser Account account
    ) {
        familyService.withdraw(account);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{familyId}/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long familyId,
            @AuthUser Account account
    ) throws IOException {
        if (account.getMemberInfos()
                .stream()
                .noneMatch(memberInfo -> memberInfo.getId().getFamilyId().equals(familyId))) {
            return ResponseEntity.status(403).build();
        }
        if(file.isEmpty()) {
            System.out.println("file is not exists");
            return ResponseEntity.badRequest().build();
        }

        String imageUrl = familyService.upload(file, familyId);
        return ResponseEntity.ok(imageUrl);
    }



    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(FamilyType.class, new FamilyTypeEnumEditor());
    }
}
