package io.hanyoung.gulmatebackend.web;

import io.hanyoung.gulmatebackend.config.auth.JwtUtils;
import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.account.AccountRepository;
import io.hanyoung.gulmatebackend.domain.family.Family;
import io.hanyoung.gulmatebackend.domain.family.FamilyRepository;
import io.hanyoung.gulmatebackend.domain.family.FamilyType;
import io.hanyoung.gulmatebackend.domain.purchase.PurchaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
class PurchaseControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
            private AccountRepository accountRepository;
    @Autowired
            private FamilyRepository familyRepository;
    @Autowired
            private PurchaseRepository purchaseRepository;
    @Autowired
            private JwtUtils jwtUtils;

    MockMvc mvc;
    Account account;
    Family family;


    @BeforeEach
    public void init() {
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        account = Account.builder()
                .email("8735132@gmail.com")
                .name("정한영")
                .build();
        account = accountRepository.save(account);
        family = Family.builder()
                .familyName("Family1")
                .inviteKey("1lkK23")
                .familyType(FamilyType.ONLY)
                .build();
        family = familyRepository.save(family);
    }

    @Test
    public void setup() {
        assertNotNull(mvc);
    }

    @Test
    @WithMockUser
    public void GET_DEFAULT_PAGE() throws Exception {

        mvc.perform(get("/api/v1/purchase"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void POST_CREATE_PURCHASE() throws Exception {
        // given

//        PurchaseSaveRequestDto requestDto = PurchaseSaveRequestDto.builder()
//                .title(title)
//                .build();

//        given(purchaseRepository.save(any())).willReturn(Purchase.builder()
//                .id(1L)
//                .title(title)
//                .build());
//
//        // expect
//        mvc.perform(post("/api/v1/purchase")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(requestDto))
//        )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string("1"));

    }

}