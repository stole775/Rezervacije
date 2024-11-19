package com.reservationbroker.reservation.services.impl;

import com.reservationbroker.reservation.entities.WorkingHour;
import com.reservationbroker.reservation.enums.DayOfWeek;
import com.reservationbroker.reservation.repositories.WorkingHourRepository;
import com.reservationbroker.reservation.services.WorkingHourService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WorkingHourServiceImpl implements WorkingHourService {

    private final WorkingHourRepository workingHoursRepository;


    @Override
    public Optional<WorkingHour> getWorkingHoursForCompanyAndDay(Long companyId, DayOfWeek dayOfWeek) {
        return workingHoursRepository.findByCompanyIdAndDayOfWeek(companyId,  dayOfWeek);
    }

    @Override
    public List<WorkingHour> getAllWorkingHoursForCompany(Long companyId) {
        return workingHoursRepository.findByCompanyId(companyId);
    }

    @Override
    public WorkingHour saveWorkingHours(WorkingHour workingHours) {
        return workingHoursRepository.save(workingHours);
    }

    @Override
    public void deleteWorkingHours(Long id) {
        workingHoursRepository.deleteById(id);
    }
}
