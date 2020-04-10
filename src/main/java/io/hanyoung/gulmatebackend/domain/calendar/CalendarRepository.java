package io.hanyoung.gulmatebackend.domain.calendar;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    @Query("SELECT c FROM Calendar c WHERE year(c.dateTime) = :year and c.family.id = :familyId")
    List<Calendar> getCalendarListByYearAndMonth(@Param("year") int year, @Param("familyId") Long familyId);

    @Query("SELECT c FROM Calendar c WHERE CURRENT_TIMESTAMP < c.dateTime and c.family.id = :familyId")
    List<Calendar> findRecent(@Param("familyId") Long familyId);

    @Query("SELECT c FROM Calendar c WHERE CURRENT_TIMESTAMP < c.dateTime and c.family.id = :familyId")
    List<Calendar> findRecent(@Param("familyId") Long familyId, Pageable pageable);
}


