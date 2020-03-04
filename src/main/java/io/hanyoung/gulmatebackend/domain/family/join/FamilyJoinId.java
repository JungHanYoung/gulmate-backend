package io.hanyoung.gulmatebackend.domain.family.join;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Embeddable
public class FamilyJoinId implements Serializable {

    @Column(name = "account_id")
    private Long accountId;
    @Column(name = "family_id")
    private Long familyId;

    public FamilyJoinId(Long accountId, Long familyId) {
        this.accountId = accountId;
        this.familyId = familyId;
    }
}
