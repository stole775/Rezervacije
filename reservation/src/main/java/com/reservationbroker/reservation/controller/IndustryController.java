package com.reservationbroker.reservation.controllers;

import com.reservationbroker.reservation.entities.Industry;
import com.reservationbroker.reservation.services.IndustryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/industries")
@AllArgsConstructor
public class IndustryController {

    private final IndustryService industryService;

    @PostMapping
    @PreAuthorize("hasRole('SADMIN')")
    public ResponseEntity<Industry> createIndustry(@RequestBody Industry industry) {
        Industry createdIndustry = industryService.createIndustry(industry);
        return ResponseEntity.ok(createdIndustry);
    }

    @GetMapping
    @PreAuthorize("hasRole('SADMIN') or hasRole('CADMIN')")
    public ResponseEntity<List<Industry>> getAllIndustries() {
        List<Industry> industries = industryService.getAllIndustries();
        return ResponseEntity.ok(industries);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SADMIN') or hasRole('CADMIN')")
    public ResponseEntity<Industry> getIndustryById(@PathVariable Long id) {
        Optional<Industry> industry = industryService.getIndustryById(id);
        return industry.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SADMIN')")
    public ResponseEntity<Industry> updateIndustry(@PathVariable Long id, @RequestBody Industry industry) {
        Industry updatedIndustry = industryService.updateIndustry(id, industry);
        return ResponseEntity.ok(updatedIndustry);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SADMIN')")
    public ResponseEntity<Void> deleteIndustry(@PathVariable Long id) {
        industryService.deleteIndustry(id);
        return ResponseEntity.noContent().build();
    }
}
