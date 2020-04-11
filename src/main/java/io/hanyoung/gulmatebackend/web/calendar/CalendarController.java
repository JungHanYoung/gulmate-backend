package io.hanyoung.gulmatebackend.web.calendar;

import io.hanyoung.gulmatebackend.config.auth.OAuthAuthenticationToken;
import io.hanyoung.gulmatebackend.config.web.AuthUser;
import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.calendar.Calendar;
import io.hanyoung.gulmatebackend.domain.calendar.CalendarRepository;
import io.hanyoung.gulmatebackend.web.calendar.dto.CalendarResponseDto;
import io.hanyoung.gulmatebackend.web.calendar.dto.CalendarSaveRequestDto;
import io.hanyoung.gulmatebackend.web.calendar.dto.CalendarUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class CalendarController {

    private final CalendarRepository calendarRepository;
    private final CalendarService calendarService;

    @GetMapping("/{familyId}/calendar")
    public ResponseEntity<?> getCalendarList(
            @RequestParam int year,
            @PathVariable Long familyId,
            @AuthUser Account account
    ) {

        if (account.getCurrentFamily().getId().equals(familyId)) {
            List<Calendar> calendarListByYearAndMonth = calendarRepository.getCalendarListByYearAndMonth(year, familyId);
            return ResponseEntity.ok(calendarListByYearAndMonth.stream()
                    .map(CalendarResponseDto::new)
                    .collect(Collectors.toList()));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .build();
    }

    @GetMapping("/{familyId}/calendar/recent")
    public ResponseEntity<?> getRecentCalendarList(
            @RequestParam(required = false, defaultValue = "3") int size,
            @PathVariable Long familyId,
            @AuthUser Account account
    ) {
        if(account.getMemberInfos()
                .stream()
                .noneMatch(memberInfo -> memberInfo.getFamily().getId().equals(familyId))) {
            return ResponseEntity.status(403).build();
        }

        List<Calendar> recent = calendarRepository.findRecent(familyId, PageRequest.of(0, size));

        return ResponseEntity.ok(recent);
    }

    @PostMapping("/{familyId}/calendar")
    public ResponseEntity<?> createCalendar(
            @PathVariable Long familyId,
            @AuthUser Account account,
            @RequestBody CalendarSaveRequestDto requestDto
    ) {
        if(account.getCurrentFamily().getId().equals(familyId)) {
            CalendarResponseDto calendar = calendarService.createCalendar(requestDto, account, familyId);
            return ResponseEntity.ok(calendar);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .build();
    }

    @PutMapping("/{familyId}/calendar/{calendarId}")
    public ResponseEntity<?> updateCalendar(
            @PathVariable Long familyId,
            @PathVariable Long calendarId,
            @AuthUser Account account,
            @RequestBody CalendarUpdateRequestDto requestDto
    ) {
        if (account.getCurrentFamily().getId().equals(familyId)) {
            CalendarResponseDto responseDto = calendarService.updateCalendar(calendarId, requestDto);
            return ResponseEntity.ok(responseDto);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .build();
    }

    @DeleteMapping("/{familyId}/calendar/{calendarId}")
    public ResponseEntity<?> deleteCalendar(
            @PathVariable Long familyId,
            @PathVariable Long calendarId,
            @AuthUser Account account
    ) {
        if(account.getCurrentFamily().getId().equals(familyId)) {
            calendarService.deleteCalendar(calendarId);
            return ResponseEntity.ok(calendarId);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .build();
    }

}
