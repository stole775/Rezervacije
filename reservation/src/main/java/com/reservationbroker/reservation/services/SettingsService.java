package com.reservationbroker.reservation.services;

import com.reservationbroker.reservation.entities.Setting;

import java.util.Optional;

public interface SettingsService {

    Optional<Setting> getSettingsByCompanyId(Long companyId);
    Setting updateSettings(Long id, Setting settings);
    boolean addImageUrl(Long id, String imageUrl);
    int getNumberOfImages(Long id);
    void removeImageUrl(Long id, String imageUrl);
    // Save general settings without handling images
    Setting saveSettings(Setting settings);
    // Save image URL in the specified column (e.g., for logo or background)
    void saveImageUrl(Long companyId, String imageUrl, String columnName);
    Optional<Setting> getSettingsByCompanyName(String companyName);

}
