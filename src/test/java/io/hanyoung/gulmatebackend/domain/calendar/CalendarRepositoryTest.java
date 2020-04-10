package io.hanyoung.gulmatebackend.domain.calendar;

import io.hanyoung.gulmatebackend.domain.family.Family;
import io.hanyoung.gulmatebackend.domain.family.FamilyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class CalendarRepositoryTest {

    @Autowired
    private CalendarRepository calendarRepository;
    @Autowired
    private FamilyRepository familyRepository;

    @BeforeEach
    void setUp() {
        Family family = Family.builder()
                .familyName("hello")
                .familyPhoto("http://...")
                .inviteKey("QIWKED")
                .build();
        familyRepository.save(family);
        for(int i = 1; i < 5; i++) {
            calendarRepository.save(Calendar.builder()
                    .title("title" + (i+1))
                    .place("place" + (i+1))
                    .family(family)
                    .dateTime(LocalDateTime.now().minusDays(3 * i))
                    .build());
        }
        for(int i = 0; i < 5; i++) {
            calendarRepository.save(Calendar.builder()
                    .title("title" + (i+1))
                    .place("place" + (i+1))
                    .family(family)
                    .dateTime(LocalDateTime.now().plusDays(3 * i))
                    .build());
        }
        calendarRepository.flush();
    }

    @Test
    void name() {
        Family family = familyRepository.findAll().get(0);

        List<Calendar> recent = calendarRepository.findRecent(family.getId());
        for (Calendar calendar : recent) {
            System.out.println(String.format("%d-%d-%d", calendar.getDateTime().getYear(), calendar.getDateTime().getMonthValue(), calendar.getDateTime().getDayOfMonth()));
        }
    }

    @Test
    void findRecentDateTimeCalendarList() {

        Family family = familyRepository.findAll().get(0);

//        calendarRepository.findAll()
//                .forEach(calendar -> System.out.println(String.format("%d-%d-%d", calendar.getDateTime().getYear(), calendar.getDateTime().getMonthValue(), calendar.getDateTime().getDayOfMonth())));

        List<Calendar> recentByYear = calendarRepository.findRecent(family.getId(), PageRequest.of(0, 3));
        assertThat(recentByYear).hasSize(3);
        for (Calendar calendar : recentByYear) {
            assertThat(LocalDateTime.now()).isBefore(calendar.getDateTime());
//            System.out.println(String.format("%d-%d-%d", calendar.getDateTime().getYear(), calendar.getDateTime().getMonthValue(), calendar.getDateTime().getDayOfMonth()));
        }
    }
}