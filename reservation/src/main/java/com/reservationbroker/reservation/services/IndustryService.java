package com.reservationbroker.reservation.services;

import com.reservationbroker.reservation.entities.Industry;

import java.util.List;
import java.util.Optional;

public interface IndustryService {
    Industry createIndustry(Industry industry);
    Industry updateIndustry(Long id, Industry industry);
    void deleteIndustry(Long id);
    List<Industry> getAllIndustries();
    Optional<Industry> getIndustryById(Long id);
    Optional<Industry> getIndustryByName(String name);
}
