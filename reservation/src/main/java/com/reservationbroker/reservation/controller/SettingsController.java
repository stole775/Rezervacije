package com.reservationbroker.reservation.controller;

import com.reservationbroker.reservation.dto.SettingsDto;
import com.reservationbroker.reservation.entities.Company;
import com.reservationbroker.reservation.entities.Setting;
import com.reservationbroker.reservation.repositories.CompanyRepository;
import com.reservationbroker.reservation.services.SettingsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/settings")
@AllArgsConstructor
public class SettingsController {

    private final SettingsService settingsService;
    private final CompanyRepository companyRepository;

    // Get settings by company ID
    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN', 'WORKER')")
    public ResponseEntity<?> getSettingsByCompanyId(@PathVariable Long companyId) {
        try {
            Optional<Setting> settingOpt = settingsService.getSettingsByCompanyId(companyId);
            if (settingOpt.isPresent()) {
                return ResponseEntity.ok(settingOpt.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Settings not found for the company with ID: " + companyId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server error: " + e.getMessage());
        }
    }

    // Update settings by company ID
    @PutMapping("/company/{companyId}")
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN', 'WORKER')")
    public ResponseEntity<?> updateSettings(@PathVariable Long companyId, @RequestBody Setting settings) {
        Optional<Setting> existingSettings = settingsService.getSettingsByCompanyId(companyId);

        if (existingSettings.isPresent()) {
            Setting updatedSettings = settingsService.updateSettings(existingSettings.get().getId(), settings);
            return ResponseEntity.ok(updatedSettings);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Settings not found for the company with ID: " + companyId);
        }
    }

    // Get the number of image URLs for a specific company
    @GetMapping("/company/{companyId}/imageCount")
    public ResponseEntity<?> getNumberOfImages(@PathVariable Long companyId) {
        try {
            int imageCount = settingsService.getNumberOfImages(companyId);
            return ResponseEntity.ok(imageCount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving image count: " + e.getMessage());
        }
    }

    @GetMapping("/company-name/{companyName}")
    public ResponseEntity<SettingsDto> getSettingsByCompanyName(@PathVariable String companyName) {
        Optional<Company> company = companyRepository.findByName(companyName);

        if (company.isPresent()) {
            Optional<Setting> settingOpt = settingsService.getSettingsByCompanyId(company.get().getId());

            if (settingOpt.isPresent()) {
                Setting setting = settingOpt.get();
                SettingsDto dto = new SettingsDto();

                dto.setNumberOfMessages(setting.getNumberOfMessages());
                dto.setHoursBeforeFirstMsg(setting.getHoursBeforeFirstMsg());
                dto.setHoursBeforeSecondMsg(setting.getHoursBeforeSecondMsg());
                dto.setDaysToKeep(setting.getDaysToKeep());
                dto.setMessageTemplate(setting.getMessageTemplate());
                dto.setPrikaziCene(setting.getPrikaziCene());
                dto.setCenovnik(setting.isCenovnik());
                dto.setEmail(setting.getEmail());
                dto.setPhone(setting.getPhone());
                dto.setAddress(setting.getAddress());
                dto.setCity(setting.getCity());
                dto.setZip(setting.getZip());
                dto.setTimezone(String.valueOf(setting.getTimezone()));
                dto.setButtonShape(String.valueOf(setting.getButtonShape()));
                dto.setTheme(String.valueOf(setting.getTheme()));
                dto.setImageUrlLogo(setting.getImageUrlLogo());
                dto.setImageUrlBackground(setting.getImageUrlBackground());
                dto.setCompanyId(Math.toIntExact(company.get().getId()));


                return ResponseEntity.ok(dto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
