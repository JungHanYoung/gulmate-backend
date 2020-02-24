package io.hanyoung.gulmatebackend.domain.family;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.hanyoung.gulmatebackend.domain.BaseTimeEntity;
import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.chat.Chat;
import io.hanyoung.gulmatebackend.domain.purchase.Purchase;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Family extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String familyName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FamilyType familyType;

    @Column(nullable = false, unique = true, length = 6)
    private String inviteKey;

    @JsonManagedReference
    @OneToMany(mappedBy = "family")
    private List<Account> accountList = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "family")
    private List<Purchase> purchaseList = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "family")
    private List<Chat> chatList = new ArrayList<>();

    @Builder
    public Family(String familyName, FamilyType familyType, String inviteKey) {
        this.familyName = familyName;
        this.familyType = familyType;
        this.inviteKey = inviteKey;
    }

    public void addAccount(Account account) {
        accountList.add(account);
    }
}
