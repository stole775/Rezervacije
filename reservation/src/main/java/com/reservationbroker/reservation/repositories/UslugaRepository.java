package com.reservationbroker.reservation.repositories;

import com.reservationbroker.reservation.entities.Usluge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UslugaRepository extends JpaRepository<Usluge, Long> {
    Optional<Usluge> findByNaziv(String naziv);
}