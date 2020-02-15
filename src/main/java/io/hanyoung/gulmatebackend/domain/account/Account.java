package io.hanyoung.gulmatebackend.domain.account;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.hanyoung.gulmatebackend.domain.BaseTimeEntity;
import io.hanyoung.gulmatebackend.domain.calendar.Calendar;
import io.hanyoung.gulmatebackend.domain.family.Family;
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
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column
    private String photoUrl;

    @JsonBackReference
    @ManyToOne
    private Family family;

    @JsonManagedReference
    @OneToMany(mappedBy = "account")
    private List<Purchase> purchaseList = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "calendar_account",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "calendar_id")
    )
    private Set<Calendar> calendars = new HashSet<>();

    @Builder
    public Account(Long id, String email, String name, String photoUrl, Family family) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.photoUrl = photoUrl;
        this.family = family;
    }

    public Account update(String name) {
        this.name = name;
        return this;
    }

    public Account setFamily(Family family) {
        this.family = family;
        return this;
    }
}
