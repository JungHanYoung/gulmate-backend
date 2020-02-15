package io.hanyoung.gulmatebackend.web.calendar;

import io.hanyoung.gulmatebackend.config.web.AuthUser;
import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.calendar.Calendar;
import io.hanyoung.gulmatebackend.domain.calendar.CalendarRepository;
import io.hanyoung.gulmatebackend.web.calendar.dto.CalendarResponseDto;
import io.hanyoung.gulmatebackend.web.calendar.dto.CalendarSaveRequestDto;
import io.hanyoung.gulmatebackend.web.calendar.dto.CalendarUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CalendarController {

    private final CalendarRepository calendarRepository;
    private final CalendarService calendarService;

    @GetMapping("/api/v1/{familyId}/calendar")
    public ResponseEntity<?> getCalendarList(
            @RequestParam int year,
            @RequestParam int month,
            @PathVariable Long familyId,
            @AuthUser Account account
    ) {
        if (account.getFamily().getId().equals(familyId)) {
            List<Calendar> calendarListByYearAndMonth = calendarRepository.getCalendarListByYearAndMonth(year, month);
            return ResponseEntity.ok(calendarListByYearAndMonth);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .build();
    }

    @PostMapping("/api/v1/{familyId}/calendar")
    public ResponseEntity<?> createCalendar(
            @PathVariable Long familyId,
            @AuthUser Account account,
            @RequestBody CalendarSaveRequestDto requestDto
    ) {
        if(account.getFamily().getId().equals(familyId)) {
            CalendarResponseDto calendar = calendarService.createCalendar(requestDto, account, familyId);
            return ResponseEntity.ok(calendar);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .build();
    }

    @PutMapping("/api/v1/{familyId}/calendar/{calendarId}")
    public ResponseEntity<?> updateCalendar(
            @PathVariable Long familyId,
            @PathVariable Long calendarId,
            @AuthUser Account account,
            @RequestBody CalendarUpdateRequestDto requestDto
    ) {
        if (account.getFamily().getId().equals(familyId)) {
            CalendarResponseDto responseDto = calendarService.updateCalendar(calendarId, requestDto);
            return ResponseEntity.ok(responseDto);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .build();
    }

    @DeleteMapping("/api/v1/{familyId}/calendar/{calendarId}")
    public ResponseEntity<?> deleteCalendar(
            @PathVariable Long familyId,
            @PathVariable Long calendarId,
            @AuthUser Account account
    ) {
        if(account.getFamily().getId().equals(familyId)) {
            calendarService.deleteCalendar(calendarId);
            return ResponseEntity.ok(calendarId);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .build();
    }

}
