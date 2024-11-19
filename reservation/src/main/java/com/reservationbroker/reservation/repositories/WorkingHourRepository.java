package com.reservationbroker.reservation.repositories;

import com.reservationbroker.reservation.entities.WorkingHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkingHourRepository extends JpaRepository<WorkingHour, Long> {

    @Query("SELECT w FROM WorkingHour w WHERE w.company.id = :companyId AND w.dayOfWeek = :dayOfWeek")
    Optional<WorkingHour> findByCompanyIdAndDayOfWeek(@Param("companyId") Long companyId, @Param("dayOfWeek") com.reservationbroker.reservation.enums.DayOfWeek dayOfWeek);

    List<WorkingHour> findByCompanyId(Long companyId);

}
