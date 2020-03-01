package io.hanyoung.gulmatebackend.domain.chat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.hanyoung.gulmatebackend.domain.BaseTimeEntity;
import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.family.Family;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Chat extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String message;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Family family;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @Builder
    public Chat(String message, Family family, Account account) {
        this.message = message;
        this.family = family;
        this.account = account;
    }
}
