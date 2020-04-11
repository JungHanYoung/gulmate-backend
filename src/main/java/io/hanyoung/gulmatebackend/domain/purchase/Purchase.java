package io.hanyoung.gulmatebackend.domain.purchase;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.hanyoung.gulmatebackend.domain.BaseTimeEntity;
import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.family.Family;
import io.hanyoung.gulmatebackend.web.purchase.dto.PurchaseCheckRequestDto;
import io.hanyoung.gulmatebackend.web.purchase.dto.PurchaseUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
@Entity
public class Purchase extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String place;

    @Column
    private LocalDateTime deadline;

    @Column
    private boolean isComplete = false;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Account checker;

    @Column
    private LocalDateTime checkedDateTime;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Family family;

    @Builder
    public Purchase(String title, String place, LocalDateTime deadline, boolean isComplete, Account account, Account checker, Family family) {
        this.title = title;
        this.place = place;
        this.deadline = deadline;
        this.isComplete = isComplete;
        this.account = account;
        this.checker = checker;
        this.family = family;
    }

    public Purchase update(PurchaseUpdateRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.place = requestDto.getPlace();
        this.deadline = requestDto.getDeadline();
        this.isComplete = requestDto.isComplete();
        return this;
    }

    public Purchase check(PurchaseCheckRequestDto requestDto, Account account) {
        if(requestDto.isComplete()) {
            this.complete(account);
        } else {
            this.cancel();
        }
        return this;
    }

    public Purchase complete(Account account) {
        this.isComplete = true;
        this.checker = account;
        this.checkedDateTime = LocalDateTime.now();
        return this;
    }

    public Purchase cancel() {
        this.isComplete = false;
        this.checker = null;
        this.checkedDateTime = null;
        return this;
    }
}
