package io.hanyoung.gulmatebackend.web.calendar.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CalendarUpdateRequestDto {

    private String title;
    private String place;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;
    private List<Long> accountIds;

    @Builder
    public CalendarUpdateRequestDto(String title, String place, LocalDateTime dateTime, List<Long> accountIds) {
        this.title = title;
        this.place = place;
        this.dateTime = dateTime;
        this.accountIds = accountIds;
    }
}
