package io.hanyoung.gulmatebackend.web.dto;

import io.hanyoung.gulmatebackend.domain.family.FamilyType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FamilySaveRequestDto {

    private String familyName;
    private FamilyType familyType;

    @Builder
    public FamilySaveRequestDto(String familyName, FamilyType familyType) {
        this.familyName = familyName;
        this.familyType = familyType;
    }
}
