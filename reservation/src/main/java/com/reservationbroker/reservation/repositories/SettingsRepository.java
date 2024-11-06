package com.reservationbroker.reservation.repositories;

import com.reservationbroker.reservation.entities.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SettingsRepository extends JpaRepository<Setting, Long> {

    Optional<Setting> findByCompanyId(Long companyId);
}