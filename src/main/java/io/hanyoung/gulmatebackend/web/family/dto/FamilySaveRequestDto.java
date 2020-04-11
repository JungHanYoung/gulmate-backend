package io.hanyoung.gulmatebackend.web.family.dto;

import io.hanyoung.gulmatebackend.domain.family.FamilyType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class FamilySaveRequestDto {

    private String familyName;

    @Builder
    public FamilySaveRequestDto(String familyName) {
        this.familyName = familyName;
    }
}
