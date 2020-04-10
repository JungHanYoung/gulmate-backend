package io.hanyoung.gulmatebackend.web.family;

import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.account.AccountRepository;
import io.hanyoung.gulmatebackend.domain.family.Family;
import io.hanyoung.gulmatebackend.domain.family.FamilyRepository;
import io.hanyoung.gulmatebackend.domain.family.join.FamilyJoin;
import io.hanyoung.gulmatebackend.domain.family.join.FamilyJoinId;
import io.hanyoung.gulmatebackend.domain.family.join.FamilyJoinRepository;
import io.hanyoung.gulmatebackend.service.UploadService;
import io.hanyoung.gulmatebackend.web.exception.ResourceNotFoundException;
import io.hanyoung.gulmatebackend.web.family.dto.FamilySaveRequestDto;
import io.hanyoung.gulmatebackend.web.family.dto.FamilyResponseDto;
import io.hanyoung.gulmatebackend.web.family.dto.FamilyUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RequiredArgsConstructor
@Service
public class FamilyService {

    private final UploadService uploadService;
    private final FamilyRepository familyRepository;
    private final AccountRepository accountRepository;
    private final FamilyJoinRepository memberInfoRepository;

    @Transactional
    public FamilyResponseDto createFamily(Account account, FamilySaveRequestDto requestDto) {
        String generated;
        do {
            generated = generateRandomString();
        } while (familyRepository.existsByInviteKey(generated));

        Family savedFamily = familyRepository.save(
                Family.builder()
                        .familyName(requestDto.getFamilyName())
                        .inviteKey(generated)
                        .build()
        );



        FamilyJoin memberInfo = memberInfoRepository.save(FamilyJoin.builder()
                .id(new FamilyJoinId(account.getId(), savedFamily.getId()))
                .account(account)
                .family(savedFamily)
                .build());

        account.setCurrentFamily(savedFamily);
        savedFamily.addMember(memberInfo);
        accountRepository.save(account);

        return new FamilyResponseDto(savedFamily);

    }

    @Transactional
    public FamilyResponseDto joinFamily(Account account, String inviteKey) {
        Family family = familyRepository.findByInviteKey(inviteKey)
                .orElseThrow(() -> new IllegalArgumentException("Invalid invite key"));

        account.setCurrentFamily(family);
        return new FamilyResponseDto(family);
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

    @Transactional
    public FamilyResponseDto getCurrentFamily(Account account) throws ResourceNotFoundException {

        Family currentFamily = account.getCurrentFamily();
        if(currentFamily == null) throw new ResourceNotFoundException(Family.class);

        return new FamilyResponseDto(currentFamily);
    }

    @Transactional
    public void withdraw(Account account) {
        memberInfoRepository.deleteById(new FamilyJoinId(account.getId(), account.getCurrentFamily().getId()));
        account.setCurrentFamily(null);
        List<FamilyJoin> byAccountId = memberInfoRepository.findByAccountId(account.getId());
        accountRepository.save(account);
    }

    @Transactional
    public FamilyJoin modifyMemberInfo(Account account, Long familyId, FamilyUpdateRequestDto requestDto) {
        FamilyJoin familyJoin = memberInfoRepository.findById(new FamilyJoinId(account.getId(), familyId))
            .orElseThrow(() -> new IllegalArgumentException("해당 유저는 가족에 속해있지 않습니다. accountId=" + account.getId()));
        familyJoin.update(requestDto.getNickname());
        memberInfoRepository.save(familyJoin);

        return familyJoin;
    }

    @Transactional
    public String upload(MultipartFile file, Long familyId) throws IOException {


        String familyImageUri = uploadService.save(file, Optional.of(extractMetadata(file)));

        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(family.getFamilyPhotoUrl() != null) {
            uploadService.delete(family.getFamilyPhotoUrl());
        }
        family.setFamilyPhotoUrl(familyImageUri);
        familyRepository.save(family);
        return familyImageUri;
    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }
}
