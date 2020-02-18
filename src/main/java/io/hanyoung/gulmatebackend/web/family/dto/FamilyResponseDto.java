package io.hanyoung.gulmatebackend.web.family.dto;

import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.family.Family;
import lombok.Getter;

import java.util.List;

@Getter
public class FamilyResponseDto {

    private Long id;
    private String familyName;
    private String familyType;
    private String inviteKey;
    private List<Account> accountList;

    public FamilyResponseDto(Family family) {
        this.id = family.getId();
        this.familyName = family.getFamilyName();
        this.familyType = family.getFamilyType().name();
        this.inviteKey = family.getInviteKey();
        this.accountList = family.getAccountList();
    }

}
