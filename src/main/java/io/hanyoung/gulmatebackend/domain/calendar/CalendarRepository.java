package io.hanyoung.gulmatebackend.domain.calendar;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    @Query("SELECT c FROM Calendar c WHERE year(c.dateTime) = :year and month(c.dateTime) = :month and c.family.id = :familyId")
    List<Calendar> getCalendarListByYearAndMonth(@Param("year") int year, @Param("month") int month, @Param("familyId") Long familyId);

}
