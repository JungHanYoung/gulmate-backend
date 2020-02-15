package io.hanyoung.gulmatebackend.web.calendar;

import io.hanyoung.gulmatebackend.domain.account.Account;
import io.hanyoung.gulmatebackend.domain.account.AccountRepository;
import io.hanyoung.gulmatebackend.domain.calendar.Calendar;
import io.hanyoung.gulmatebackend.domain.calendar.CalendarRepository;
import io.hanyoung.gulmatebackend.domain.family.Family;
import io.hanyoung.gulmatebackend.domain.family.FamilyRepository;
import io.hanyoung.gulmatebackend.web.calendar.dto.CalendarResponseDto;
import io.hanyoung.gulmatebackend.web.calendar.dto.CalendarSaveRequestDto;
import io.hanyoung.gulmatebackend.web.calendar.dto.CalendarUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CalendarService {

    private final AccountRepository accountRepository;
    private final FamilyRepository familyRepository;
    private final CalendarRepository calendarRepository;

    @Transactional
    public CalendarResponseDto createCalendar(CalendarSaveRequestDto requestDto, Account account, Long familyId) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new IllegalArgumentException("Error: 가족관계가 아닙니다."));
        Calendar calendar = requestDto.toEntity(family, account);
        calendar.setAccountList(new HashSet<>(accountRepository.findAllById(requestDto.getAccountIds())));
        Calendar saved = calendarRepository.save(calendar);
        return new CalendarResponseDto(saved);
    }

    @Transactional
    public void deleteCalendar(Long calendarId) {
        calendarRepository.deleteById(calendarId);
    }

    @Transactional
    public CalendarResponseDto updateCalendar(Long calendarId, CalendarUpdateRequestDto requestDto) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정 데이터가 없습니다."));
        List<Account> accountAllByIds = accountRepository.findAllById(requestDto.getAccountIds());
        calendar.update(requestDto, new HashSet<>(accountAllByIds));
        return new CalendarResponseDto(calendar);
    }
}
