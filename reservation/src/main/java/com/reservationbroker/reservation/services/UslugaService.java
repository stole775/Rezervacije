package com.reservationbroker.reservation.services;

import com.reservationbroker.reservation.entities.Usluge;

import java.util.List;
import java.util.Optional;

public interface UslugaService {
    Usluge createUsluga(Usluge usluga);
    List<Usluge> getAllUsluge();
    Optional<Usluge> getUslugaById(Long id);
}