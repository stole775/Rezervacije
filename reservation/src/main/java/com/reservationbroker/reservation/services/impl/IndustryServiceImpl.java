package com.reservationbroker.reservation.services.impl;

import com.reservationbroker.reservation.entities.Industry;
import com.reservationbroker.reservation.repositories.IndustryRepository;
import com.reservationbroker.reservation.services.IndustryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class IndustryServiceImpl implements IndustryService {

    private final IndustryRepository industryRepository;

    @Override
    public Industry createIndustry(Industry industry) {
        return industryRepository.save(industry);
    }

    @Override
    public Industry updateIndustry(Long id, Industry industryDetails) {
        Industry industry = industryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Industry not found with id :: " + id));
        industry.setName(industryDetails.getName());
        // Ako imaš dodatna polja, postavi ih ovde
        return industryRepository.save(industry);
    }

    @Override
    public void deleteIndustry(Long id) {
        Industry industry = industryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Industry not found with id :: " + id));
        industryRepository.delete(industry);
    }

    @Override
    public List<Industry> getAllIndustries() {
        return industryRepository.findAll();
    }

    @Override
    public Optional<Industry> getIndustryById(Long id) {
        return industryRepository.findById(id);
    }

    @Override
    public Optional<Industry> getIndustryByName(String name) {
        return industryRepository.findByName(name);
    }
}
