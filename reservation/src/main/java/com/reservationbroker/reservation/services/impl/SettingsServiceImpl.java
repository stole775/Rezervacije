package com.reservationbroker.reservation.services.impl;

import com.reservationbroker.reservation.entities.Setting;
import com.reservationbroker.reservation.repositories.SettingsRepository;
import com.reservationbroker.reservation.services.SettingsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SettingsServiceImpl implements SettingsService {

    private final SettingsRepository settingsRepository;

    @Override
    public Optional<Setting> getSettingsByCompanyId(Long companyId) {
        return settingsRepository.findByCompanyId(companyId);
    }

    @Override
    public Setting updateSettings(Long id, Setting newSettings) {
        return settingsRepository.findById(id).map(existingSettings -> {
            // Update fields
            existingSettings.setNumberOfMessages(newSettings.getNumberOfMessages());
            existingSettings.setHoursBeforeFirstMsg(newSettings.getHoursBeforeFirstMsg());
            existingSettings.setHoursBeforeSecondMsg(newSettings.getHoursBeforeSecondMsg());
            existingSettings.setMessageTemplate(newSettings.getMessageTemplate());
            existingSettings.setDaysToKeep(newSettings.getDaysToKeep());

            existingSettings.setEmail(newSettings.getEmail());
            existingSettings.setPhone(newSettings.getPhone());
            existingSettings.setAddress(newSettings.getAddress());
            existingSettings.setCity(newSettings.getCity());
            existingSettings.setZip(newSettings.getZip());
            existingSettings.setTimezone(newSettings.getTimezone());
            existingSettings.setPrikaziCene(newSettings.getPrikaziCene());

            existingSettings.setButtonShape(newSettings.getButtonShape());
            existingSettings.setTheme(newSettings.getTheme());

            // Image URLs are updated separately via upload endpoints
            return settingsRepository.save(existingSettings);
        }).orElseThrow(() -> new RuntimeException("Settings not found with id: " + id));
    }

    @Override
    public Setting saveSettings(Setting settings) {
        return settingsRepository.save(settings);
    }

    @Override
    public void saveImageUrl(Long companyId, String imageUrl, String columnName) {
        settingsRepository.findByCompanyId(companyId).ifPresent(settings -> {
            switch (columnName) {
                case "logo":
                    settings.setImageUrlLogo(imageUrl);
                    break;
                case "background":
                    settings.setImageUrlBackground(imageUrl);
                    break;
                case "additionalImages":
                    settings.addImageUrl(imageUrl);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid column name: " + columnName);
            }
            settingsRepository.save(settings);
        });
    }

    @Override
    public void removeImageUrl(Long companyId, String filename) {
        settingsRepository.findByCompanyId(companyId).ifPresent(settings -> {
            boolean imageRemoved = false;

            // Check logo
            if (filename.equals(settings.getImageUrlLogo())) {
                settings.setImageUrlLogo(null);
                imageRemoved = true;
            }

            // Check background
            if (filename.equals(settings.getImageUrlBackground())) {
                settings.setImageUrlBackground(null);
                imageRemoved = true;
            }

            // Check additional images
            List<String> imageUrls = settings.getImageUrl10();
            if (imageUrls.remove(filename)) {
                settings.setImageUrl10(imageUrls);
                imageRemoved = true;
            }

            if (imageRemoved) {
                settingsRepository.save(settings);
            }
        });
    }

    @Override
    public boolean addImageUrl(Long companyId, String imageUrl) {
        return settingsRepository.findByCompanyId(companyId).map(settings -> {
            boolean added = settings.addImageUrl(imageUrl);
            if (added) {
                settingsRepository.save(settings);
                return true;
            }
            return false;
        }).orElseThrow(() -> new RuntimeException("Settings not found with companyId: " + companyId));
    }

    @Override
    public int getNumberOfImages(Long companyId) {
        return settingsRepository.findByCompanyId(companyId)
                .map(Setting::getNumberOfImages)
                .orElseThrow(() -> new RuntimeException("Settings not found with companyId: " + companyId));
    }
}
