package io.hanyoung.gulmatebackend.domain.purchase;

import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.account.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PurchaseRepositoryTest {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void loadContext() {

    }

    @Test
    public void setup() {

        String givenEmail = "8735132@gmail.com";

        accountRepository.save(
                Account.builder()
                        .email(givenEmail)
                        .build()
        );

        Account findEmail = accountRepository.findByEmail(givenEmail).orElse(null);
        assertNotNull(findEmail);
        assertNotNull(findEmail.getEmail());
        assertEquals(findEmail.getEmail(), givenEmail);
        assertEquals(findEmail.getId(), 1);

    }

}