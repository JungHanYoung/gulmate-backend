package io.hanyoung.gulmatebackend.domain.family.join;

import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.family.Family;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@Entity
public class FamilyJoin {

    @EmbeddedId
    FamilyJoinId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("account_id")
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("family_id")
    @JoinColumn(name = "family_id")
    private Family family;

    @Column
    private String nickname;

    @Builder
    public FamilyJoin(FamilyJoinId id, Account account, Family family, String nickname) {
        this.id = id;
        this.account = account;
        this.family = family;
        this.nickname = nickname;
    }

    public void update(String nickname) {
        this.nickname = nickname;
    }
}

