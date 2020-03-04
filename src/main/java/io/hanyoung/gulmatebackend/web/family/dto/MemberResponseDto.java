package io.hanyoung.gulmatebackend.web.family.dto;

import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.family.Family;
import lombok.Getter;

@Getter
public class MemberResponseDto {

    private Long id;
    private String email;
    private String name;
    private String photoUrl;
    private String colorHex;
    private String nickname;

    public MemberResponseDto(Account account, Family family) {
        this.id = account.getId();
        this.email = account.getEmail();
        this.name = account.getName();
        this.photoUrl = account.getPhotoUrl();
        family.getMemberInfos()
                .stream()
                .filter(memberInfo -> memberInfo.getAccount().getId().equals(account.getId())).findFirst()
                .ifPresent(memberInfo -> {
                    this.colorHex = memberInfo.getColorHex();
                    this.nickname = memberInfo.getNickname();
                });
    }

}
