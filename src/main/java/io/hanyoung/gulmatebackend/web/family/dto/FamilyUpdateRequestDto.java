package io.hanyoung.gulmatebackend.web.family.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class FamilyUpdateRequestDto {

    private String nickname;

    @Builder
    public FamilyUpdateRequestDto(String nickname) {
        this.nickname = nickname;
    }
}
