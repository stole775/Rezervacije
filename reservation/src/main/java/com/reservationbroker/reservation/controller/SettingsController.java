package com.reservationbroker.reservation.controller;

import com.reservationbroker.reservation.entities.Setting;
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

    // Get settings by company ID
    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN')")
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
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN')")
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
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN')")
    public ResponseEntity<?> getNumberOfImages(@PathVariable Long companyId) {
        try {
            int imageCount = settingsService.getNumberOfImages(companyId);
            return ResponseEntity.ok(imageCount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving image count: " + e.getMessage());
        }
    }
}
