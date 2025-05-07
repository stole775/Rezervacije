package com.reservationbroker.reservation.controller;

import com.reservationbroker.reservation.entities.WorkingHour;
import com.reservationbroker.reservation.enums.DayOfWeek;
import com.reservationbroker.reservation.services.WorkingHourService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/working-hours")
@AllArgsConstructor
public class WorkingHourController {

    private final WorkingHourService workingHoursService;

    /**
     * Dohvata radno vreme za određenu kompaniju i dan u nedelji
     */
    @GetMapping("/{companyId}/{dayOfWeek}")
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN', 'WORKER')")
    public ResponseEntity<WorkingHour> getWorkingHours(
            @PathVariable Long companyId,
            @PathVariable String dayOfWeek) {

        Optional<WorkingHour> workingHours = workingHoursService.getWorkingHoursForCompanyAndDay(companyId, DayOfWeek.valueOf(dayOfWeek));
        return workingHours.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Dohvata sva radna vremena za kompaniju
     */
    @GetMapping("/{companyId}")
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN', 'WORKER')")
    public ResponseEntity<List<WorkingHour>> getAllWorkingHours(@PathVariable Long companyId) {
        List<WorkingHour> workingHours = workingHoursService.getAllWorkingHoursForCompany(companyId);
        return ResponseEntity.ok(workingHours);
    }

    /**
     * Dodaje ili ažurira radno vreme
     */
    @PostMapping
    @PreAuthorize("hasRole('SADMIN') or hasRole('WORKER')")
    public ResponseEntity<WorkingHour> saveWorkingHours(@RequestBody WorkingHour workingHours) {
        WorkingHour savedWorkingHours = workingHoursService.saveWorkingHours(workingHours);
        return ResponseEntity.ok(savedWorkingHours);
    }

    /**
     * Briše radno vreme po ID-ju
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SADMIN') or hasRole( 'WORKER')")
    public ResponseEntity<Void> deleteWorkingHours(@PathVariable Long id) {
        workingHoursService.deleteWorkingHours(id);
        return ResponseEntity.noContent().build();
    }
}
