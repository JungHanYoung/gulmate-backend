package io.hanyoung.gulmatebackend.web.calendar;

import com.fasterxml.jackson.databind.util.ClassUtil;
import io.hanyoung.gulmatebackend.config.auth.OAuthAuthenticationToken;
import io.hanyoung.gulmatebackend.config.web.AuthUser;
import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.calendar.Calendar;
import io.hanyoung.gulmatebackend.domain.calendar.CalendarRepository;
import io.hanyoung.gulmatebackend.web.account.AccountService;
import io.hanyoung.gulmatebackend.web.calendar.dto.CalendarResponseDto;
import io.hanyoung.gulmatebackend.web.calendar.dto.CalendarSaveRequestDto;
import io.hanyoung.gulmatebackend.web.calendar.dto.CalendarUpdateRequestDto;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@RestController
@Api(tags = "Gulmate Calendar")
@RequestMapping("/api/v1")
public class CalendarController {

    private final CalendarRepository calendarRepository;
    private final CalendarService calendarService;
    private final AccountService accountService;

    @ApiOperation(value = "calendar", notes = "가족의 일정 목록을 제공", response = CalendarResponseDto.class, responseContainer = "List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", type="year", required = true, example = "1", paramType = "query", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "familyId", type="familyId", required = true, example = "1", paramType = "path", dataTypeClass = Long.class)
    })
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

    @ApiOperation(value = "get recent calendar", notes = "최근 일정 가져오기", response = CalendarResponseDto.class, responseContainer = "List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "가져올 일정 수", paramType = "query", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "familyId", value = "귤메이트 ID", paramType = "path", dataTypeClass = Long.class)
    })
    @GetMapping("/{familyId}/calendar/recent")
    public ResponseEntity<?> getRecentCalendarList(
            @RequestParam(required = false, defaultValue = "3") int size,
            @PathVariable Long familyId,
            @AuthUser Account account
    ) {
        ResponseEntity<?> result;
        if (account.getMemberInfos()
                .stream()
                .noneMatch(memberInfo -> memberInfo.getFamily().getId().equals(familyId))) {
            result = ResponseEntity.status(403).build();
        } else {
            List<Calendar> recent = calendarRepository.findRecent(familyId, PageRequest.of(0, size));
            result = ResponseEntity.ok(recent);
        }

        return result;
    }

    @ApiOperation(
            value = "create calendar",
            notes = "일정 생성",
            response = CalendarResponseDto.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "가족 ID", required = true, paramType = "path", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "일정 생성 DTO", required = true, paramType = "query", dataTypeClass = CalendarSaveRequestDto.class)
    })
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

    @ApiOperation(value = "update calendar", notes = "일정 수정 및 업데이트", response = CalendarResponseDto.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "귤메이트 ID", required = true, paramType = "path", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "수정할 일정 ID", required = true, paramType = "path", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "일정 수정 DTO", required = true, paramType = "body")
    })
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

    @ApiOperation(value = "Delete calendar", notes = "일정 삭제", response = Long.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "귤메이트 ID", required = true, paramType = "path", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "일 ID", required = true, paramType = "path", dataTypeClass = Long.class),
    })
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
