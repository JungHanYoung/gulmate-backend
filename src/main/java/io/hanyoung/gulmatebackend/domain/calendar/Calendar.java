package io.hanyoung.gulmatebackend.domain.calendar;

import io.hanyoung.gulmatebackend.domain.BaseTimeEntity;
import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.family.Family;
import io.hanyoung.gulmatebackend.web.calendar.dto.CalendarUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class Calendar extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String place;

    @Column
    private LocalDateTime dateTime;

    @ManyToOne
    private Family family;

    @ManyToOne
    private Account creator;

    @ManyToMany
    private Set<Account> accountList = new HashSet<>();

    public void setAccountList(Set<Account> accountList) {
        this.accountList = accountList;
    }

    @Builder
    public Calendar(Long id, String title, String place, LocalDateTime dateTime, Family family, Account creator, Set<Account> accountList) {
        this.id = id;
        this.title = title;
        this.place = place;
        this.dateTime = dateTime;
        this.family = family;
        this.creator = creator;
        this.accountList = accountList;
    }

    public Calendar update(CalendarUpdateRequestDto requestDto, Set<Account> accountAllByIds) {
        this.title = requestDto.getTitle();
        this.place = requestDto.getPlace();
        this.dateTime = requestDto.getDateTime();
        this.accountList = accountAllByIds;
        return this;
    }
}
