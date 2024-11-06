package com.reservationbroker.reservation.services.impl;


import com.reservationbroker.reservation.entities.Usluge;
import com.reservationbroker.reservation.repositories.UslugaRepository;
import com.reservationbroker.reservation.services.UslugaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UslugaServiceImpl implements UslugaService {


    final private UslugaRepository uslugaRepository;

    @Override
    public Usluge createUsluga(Usluge usluga) {
        if (uslugaRepository.findByNaziv(usluga.getNaziv()).isPresent()) {
            throw new RuntimeException("Usluga sa nazivom '" + usluga.getNaziv() + "' već postoji.");
        }
        return uslugaRepository.save(usluga);
    }

    @Override
    public List<Usluge> getAllUsluge() {
        return uslugaRepository.findAll();
    }

    @Override
    public Optional<Usluge> getUslugaById(Long id) {
        return uslugaRepository.findById(id);
    }
}