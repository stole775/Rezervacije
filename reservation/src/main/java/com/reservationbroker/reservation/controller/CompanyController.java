package com.reservationbroker.reservation.controller;

import com.reservationbroker.reservation.dto.CompanyDTO;
import com.reservationbroker.reservation.entities.Company;
import com.reservationbroker.reservation.entities.Industry;
import com.reservationbroker.reservation.services.CompanyService;
import com.reservationbroker.reservation.services.IndustryService;
import com.reservationbroker.reservation.services.impl.IndustryServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/api/companies")
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final IndustryService industryService;


    @PostMapping
    @PreAuthorize("hasRole('SADMIN')")
    public ResponseEntity<Company> createCompany(@RequestBody CompanyDTO companyDTO) {
        Company company = new Company();
        company.setName(companyDTO.getName());

        if (companyDTO.getIndustryId() != null) {
            Industry industry = industryService.getIndustryById(companyDTO.getIndustryId())
                    .orElseThrow(() -> new RuntimeException("Industry not found with id :: " + companyDTO.getIndustryId()));
            company.setIndustry(industry);
        }

        Company createdCompany = companyService.createCompany(company);
        return ResponseEntity.ok(createdCompany);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN')")
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN')")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        Optional<Company> company = companyService.getCompanyById(id);
        return company.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SADMIN')")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company company) {
        Company updatedCompany = companyService.updateCompany(id, company);
        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SADMIN')")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
