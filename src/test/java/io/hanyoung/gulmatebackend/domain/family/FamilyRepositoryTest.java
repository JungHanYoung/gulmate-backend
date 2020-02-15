package io.hanyoung.gulmatebackend.domain.family;

import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.account.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FamilyRepositoryTest {

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @Transactional
    public void relationTest() {
        // given
        Family family = Family.builder()
                .familyName("Family1")
                .familyType(FamilyType.MORE_THAN_ONE)
                .inviteKey("112233")
                .build();

        familyRepository.save(family);

        Account account = Account.builder()
                .email("10101010@gmail.com")
                .name("hanyoung")
                .family(family)
                .build();

        accountRepository.save(account);

        family.addAccount(account);

        // expected
        Family getOneFamily = familyRepository.findById(1L)
                                    .orElse(null);

        assertNotNull(getOneFamily);
        System.out.println(getOneFamily.getAccountList());
        assertEquals(getOneFamily.getAccountList().size(), 1);

    }

}