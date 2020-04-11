package io.hanyoung.gulmatebackend.domain.family;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.hanyoung.gulmatebackend.domain.BaseTimeEntity;
import io.hanyoung.gulmatebackend.domain.chat.Chat;
import io.hanyoung.gulmatebackend.domain.family.join.FamilyJoin;
import io.hanyoung.gulmatebackend.domain.purchase.Purchase;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class Family extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String familyName;

    @Column(nullable = false, unique = true, length = 6)
    private String inviteKey;

    @Column
    private String familyPhotoUrl;

    @JsonManagedReference
    @OneToMany(mappedBy = "family")
    private List<Purchase> purchaseList = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "family")
    private List<Chat> chatList = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "family", fetch = FetchType.EAGER)
    private Set<FamilyJoin> memberInfos = new HashSet<>();

    @Builder
    public Family(String familyName, String inviteKey, String familyPhotoUrl) {
        this.familyName = familyName;
        this.inviteKey = inviteKey;
        this.familyPhotoUrl = familyPhotoUrl;
    }


    public void addMember(FamilyJoin memberInfo) {

        this.getMemberInfos().add(memberInfo);
        memberInfo.getAccount().setCurrentFamily(this);
    }

    public void setFamilyPhotoUrl(String familyPhotoUrl) {
        this.familyPhotoUrl = familyPhotoUrl;
    }
}
