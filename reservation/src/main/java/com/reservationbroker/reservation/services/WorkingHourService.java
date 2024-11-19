package com.reservationbroker.reservation.services;

import com.reservationbroker.reservation.entities.WorkingHour;
import com.reservationbroker.reservation.enums.DayOfWeek;

import java.util.List;
import java.util.Optional;

public interface WorkingHourService {
    Optional<WorkingHour> getWorkingHoursForCompanyAndDay(Long companyId, DayOfWeek dayOfWeek);

    List<WorkingHour> getAllWorkingHoursForCompany(Long companyId);

    WorkingHour saveWorkingHours(WorkingHour workingHours);

    void deleteWorkingHours(Long id);
}
