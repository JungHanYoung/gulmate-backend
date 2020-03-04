package io.hanyoung.gulmatebackend.web.family.dto;

import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.family.Family;
import io.hanyoung.gulmatebackend.web.account.dto.AccountResponseDto;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FamilyResponseDto {

    private Long id;
    private String familyName;
    private String familyType;
    private String inviteKey;
    private List<MemberResponseDto> accountList;

    public FamilyResponseDto(Family family) {
        this.id = family.getId();
        this.familyName = family.getFamilyName();
        this.familyType = family.getFamilyType().name();
        this.inviteKey = family.getInviteKey();
        this.accountList = family.getMemberInfos()
                .stream()
                .map(memberInfo -> new MemberResponseDto(memberInfo.getAccount(), memberInfo.getFamily()))
                .collect(Collectors.toList());
    }

}
