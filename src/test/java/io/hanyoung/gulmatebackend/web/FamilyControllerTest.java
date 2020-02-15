package io.hanyoung.gulmatebackend.web;

import io.hanyoung.gulmatebackend.config.auth.JwtUtils;
import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.account.AccountRepository;
import io.hanyoung.gulmatebackend.domain.family.Family;
import io.hanyoung.gulmatebackend.domain.family.FamilyRepository;
import io.hanyoung.gulmatebackend.domain.family.FamilyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
class FamilyControllerTest {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    private String token;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();


    }

    @Test
    public void GET_ME_FAMILY() throws Exception {
        // given
        Account account = accountRepository.save(Account.builder()
                .name("hanyoung")
                .email("10019209@gmail.com")
                .id(1L)
                .build());

        String token = jwtUtils.generateTokenByEntity(account);

        mvc.perform(
                get("/api/v1/family/me")
                .header("Authorization", "Bearer " + token)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void GET_ME_FAMILY_WITH_NO_FAMILY() throws Exception {
        Account account = accountRepository.save(Account.builder()
                .name("hanyoung")
                .email("10019209@gmail.com")
                .id(1L)
                .build());

        String token = jwtUtils.generateTokenByEntity(account);

        Family family = familyRepository.save(Family.builder()
                .familyType(FamilyType.ONLY)
                .familyName("Family1")
                .inviteKey("111122")
                .build());
        account.setFamily(family);
        accountRepository.save(account);

        mvc.perform(get("/api/v1/family/me")
        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.familyName").exists());

    }

}