package io.hanyoung.gulmatebackend.web.account.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class AccountUpdateRequestDto {

    private String nickname;

    @Builder
    public AccountUpdateRequestDto(String nickname) {
        this.nickname = nickname;
    }
}
