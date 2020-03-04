package io.hanyoung.gulmatebackend.web.calendar.dto;

import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.calendar.Calendar;
import io.hanyoung.gulmatebackend.web.account.dto.AccountResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class CalendarResponseDto {

    private Long id;
    private String title;
    private String place;
    private LocalDateTime dateTime;
    private String creator;
    private Set<AccountResponseDto> accountList;

    public CalendarResponseDto(Calendar calendar) {
        this.id = calendar.getId();
        this.title = calendar.getTitle();
        this.place = calendar.getPlace();
        this.dateTime = calendar.getDateTime();
        if(calendar.getCreator() != null) {
            this.creator = calendar.getCreator().getName();
        }
        this.accountList = calendar.getAccountList().stream().map(AccountResponseDto::new).collect(Collectors.toSet());
    }
}
