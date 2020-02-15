package io.hanyoung.gulmatebackend.web.calendar.dto;

import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.calendar.Calendar;
import io.hanyoung.gulmatebackend.domain.family.Family;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CalendarSaveRequestDto {

    private String title;
    private String place;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;
    private List<Long> accountIds;

    @Builder
    public CalendarSaveRequestDto(String title, String place, LocalDateTime dateTime, List<Long> accountIds) {
        this.title = title;
        this.place = place;
        this.dateTime = dateTime;
        this.accountIds = accountIds;
    }

    public Calendar toEntity(Family family, Account creator) {
        return Calendar.builder()
                .title(title)
                .place(place)
                .dateTime(dateTime)
                .family(family)
                .creator(creator)
                .build();
    }
}
