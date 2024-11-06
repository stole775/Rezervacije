package com.reservationbroker.reservation.services.impl;

import com.reservationbroker.reservation.entities.Reservation;
import com.reservationbroker.reservation.entities.Setting;
import com.reservationbroker.reservation.repositories.*;
import com.reservationbroker.reservation.services.CompanyService;
import com.reservationbroker.reservation.services.MessageService;
import com.reservationbroker.reservation.services.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UslugaRepository uslugaRepository;
    private final UserUslugaRepository userUslugaRepository;
    private final UserRepository userRepository;
    private final SettingsRepository settingsRepository;
    private final MessageLogRepository messageLogRepository;
    private final MessageService messageService;
    private final CompanyService companyService;

    @Override
    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }


    @Override
    public Reservation updateReservation(Reservation reservation) {
        Optional<Reservation> existingReservationOpt = reservationRepository.findById(reservation.getId());

        if (!existingReservationOpt.isPresent()) {
            throw new RuntimeException("Reservation not found with id :: " + reservation.getId());
        }

        Reservation existingReservation = existingReservationOpt.get();

        // Update the existing reservation with new values
        existingReservation.setName(reservation.getName());
        existingReservation.setPhone(reservation.getPhone());
        existingReservation.setAppointmentDate(reservation.getAppointmentDate());
        existingReservation.setVremeTrajanja(reservation.getVremeTrajanja());
        existingReservation.setVremeZavrsetka(reservation.getVremeZavrsetka());
        existingReservation.setFirstMsgSent(reservation.getFirstMsgSent());
        existingReservation.setSecondMsgSent(reservation.getSecondMsgSent());
        existingReservation.setConfirmed(reservation.getConfirmed());

        // Set updated user and service (usluga)
        existingReservation.setUser(reservation.getUser());
        existingReservation.setUsluga(reservation.getUsluga());

        // Save and return the updated reservation
        return reservationRepository.save(existingReservation);
    }



    @Override
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id :: " + id));
        reservationRepository.delete(reservation);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> getReservationsByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    @Override
    public Optional<Reservation> getAllReservationsById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public void sendFirstMessage(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id :: " + reservationId));

        if (reservation.getFirstMsgSent()) {
            throw new RuntimeException("Prva poruka već je poslana.");
        }

        // Dohvati podešavanja
        Optional<Setting> settingsOpt = settingsRepository.findByCompanyId(Long.valueOf(reservation.getUser().getCompany().getId()));
        if (!settingsOpt.isPresent()) {
            throw new RuntimeException("Podešavanja nisu pronađena za korisnika.");
        }
        Setting settings = settingsOpt.get();

        // Formatiraj poruku
        String message = formatMessage(settings.getMessageTemplate(), reservation);

        // Pošalji poruku
        boolean success = messageService.sendMessage(Long.valueOf(reservation.getUser().getId()), reservation.getId(), message);

        if (success) {
            reservation.setFirstMsgSent(true);
            reservationRepository.save(reservation);
        } else {
            throw new RuntimeException("Slanje prve poruke nije uspelo.");
        }
    }

    @Override
    public void sendSecondMessage(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id :: " + reservationId));

        if (reservation.getSecondMsgSent()) {
            throw new RuntimeException("Druga poruka već je poslana.");
        }

        // Dohvati podešavanja
        Optional<Setting> settingsOpt = settingsRepository.findByCompanyId(Long.valueOf(reservation.getUser().getCompany().getId()));
        if (!settingsOpt.isPresent()) {
            throw new RuntimeException("Podešavanja nisu pronađena za korisnika.");
        }
        Setting settings = settingsOpt.get();

        // Formatiraj poruku
        String message = formatMessage(settings.getMessageTemplate(), reservation);

        // Pošalji poruku
        boolean success = messageService.sendMessage(Long.valueOf(reservation.getUser().getId()), reservation.getId(), message);

        if (success) {
            reservation.setSecondMsgSent(true);
            reservationRepository.save(reservation);
        } else {
            throw new RuntimeException("Slanje druge poruke nije uspelo.");
        }
    }

    @Override
    public List<Reservation> getReservationsByCompanyId(Long companyId) {
        return reservationRepository.getReservationsByCompanyId(companyId);
    }

    @Override
    public boolean isReservationInCompany(Long reservationId, Long principalUserId) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            Long reservationUserId = reservation.getUser().getId();
            return companyService.isUserInCompany(reservationUserId, principalUserId);
        }
        return false;
    }

    @Override
    public boolean isReservationOwnedByUser(Long reservationId, Long principalUserId) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            return reservation.getUser().getId().equals(principalUserId);
        }
        return false;
    }

    @Override
    public boolean isUserInCompany(Long userId, Long principalUserId) {
        return companyService.isUserInCompany(userId, principalUserId);
    }
    /**
     * Formatira poruku na osnovu šablona i rezervacije.
     *
     * @param template    Šablon poruke
     * @param reservation Rezervacija
     * @return Formatirana poruka
     */
    private String formatMessage(String template, Reservation reservation) {
        return template
                .replace("{name}", reservation.getName())
                .replace("{appointment_time}", reservation.getAppointmentDate().toString());
    }


    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

}
