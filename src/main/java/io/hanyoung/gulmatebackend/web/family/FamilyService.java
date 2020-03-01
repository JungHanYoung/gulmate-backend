package io.hanyoung.gulmatebackend.web.family;

import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.account.AccountRepository;
import io.hanyoung.gulmatebackend.domain.family.Family;
import io.hanyoung.gulmatebackend.domain.family.FamilyRepository;
import io.hanyoung.gulmatebackend.web.family.dto.FamilySaveRequestDto;
import io.hanyoung.gulmatebackend.web.family.dto.FamilyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public FamilyResponseDto createFamily(Account account, FamilySaveRequestDto requestDto) {
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

        return new FamilyResponseDto(savedFamily);

    }

    @Transactional
    public FamilyResponseDto joinFamily(Account account, String inviteKey) {
        Family family = familyRepository.findByInviteKey(inviteKey)
                .orElseThrow(() -> new IllegalArgumentException("Invalid invite key"));

        account.setFamily(family);
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

}
