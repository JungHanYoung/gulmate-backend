package io.hanyoung.gulmatebackend.web.family;

import io.hanyoung.gulmatebackend.config.web.AuthUser;
import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.family.FamilyType;
import io.hanyoung.gulmatebackend.service.UploadService;
import io.hanyoung.gulmatebackend.web.exception.ResourceNotFoundException;
import io.hanyoung.gulmatebackend.web.family.dto.FamilySaveRequestDto;
import io.hanyoung.gulmatebackend.web.family.dto.FamilyResponseDto;
import io.hanyoung.gulmatebackend.web.family.dto.FamilyUpdateRequestDto;
import io.swagger.annotations.*;
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


@Api(tags = "Gulmate family API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/family")
public class FamilyController {

    private final FamilyService familyService;
    private final UploadService uploadService;

    @ApiOperation(value = "get to gulmate", notes = "내가 속해있는 귤메이트 조회", response = FamilyResponseDto.class)
    @GetMapping("/me")
    public ResponseEntity<?> getMyFamily(@AuthUser Account account) throws ResourceNotFoundException {
        FamilyResponseDto responseDto = familyService.getCurrentFamily(account);

        return ResponseEntity.ok(responseDto);
    }

    @ApiOperation(value = "update my info form gulmate", notes = "어느 귤메이트에 대한 나의 정보 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "familyId", required = true, paramType = "path", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "requestDto", required = true, paramType = "body", dataTypeClass = FamilyUpdateRequestDto.class)
    })
    @PutMapping("/{familyId}")
    public ResponseEntity<?> modifyMyInfoFromFamily(
            @AuthUser Account account,
            @PathVariable Long familyId,
            @RequestBody FamilyUpdateRequestDto requestDto) {
        familyService.modifyMemberInfo(account, familyId, requestDto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "create gulmate", notes = "귤메이트 생성", response = FamilyResponseDto.class)
    @PostMapping
    public ResponseEntity<?> createFamily(
            @AuthUser Account account,
            @ApiParam @RequestBody FamilySaveRequestDto requestDto) {

        FamilyResponseDto responseDto = familyService.createFamily(account, requestDto);

        return ResponseEntity.ok(responseDto);

    }

    @ApiOperation(value = "join to gulmate", notes = "한 귤메이트에 들어가기", response = FamilyResponseDto.class)
    @PostMapping("/join")
    public ResponseEntity<?> joinFamily(
            @AuthUser Account account,
            @ApiParam @RequestParam("inviteKey") String inviteKey) {

        FamilyResponseDto responseDto = familyService.joinFamily(account, inviteKey);

        return ResponseEntity.ok(responseDto);
    }

    @ApiOperation(value = "withdraw the gulmate", notes = "속한 귤메이트에서 빠지기")
    @PutMapping("/withdraw")
    public ResponseEntity<?> withdraw(
            @AuthUser Account account
    ) {
        familyService.withdraw(account);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "update gulmate photo", notes = "귤메이트의 배경사진 바꾸기")
    @PostMapping(value = "/{familyId}/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> upload(
            @ApiParam @RequestParam("file") MultipartFile file,
            @ApiParam @PathVariable Long familyId,
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
