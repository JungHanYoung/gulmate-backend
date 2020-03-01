package io.hanyoung.gulmatebackend.web.account.dto;

import io.hanyoung.gulmatebackend.domain.account.Account;
import lombok.Getter;

@Getter
public class AccountResponseDto {

    private Long id;
    private String email;
    private String name;
    private String photoUrl;

    public AccountResponseDto(Account account) {
        this.id = account.getId();
        this.email = account.getEmail();
        this.name = account.getName();
        this.photoUrl = account.getPhotoUrl();
    }

}
