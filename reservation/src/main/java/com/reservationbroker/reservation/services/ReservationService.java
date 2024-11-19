package com.reservationbroker.reservation.services;

import com.reservationbroker.reservation.entities.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationService {
    Reservation createReservation(Reservation reservationRequest);

    Reservation updateReservation(Reservation reservationRequest);

    void deleteReservation(Long id);

    List<Reservation> getAllReservations();

    List<Reservation> getReservationsByUserId(Long userId);

    Optional<Reservation> getReservationById(Long id);

    void sendFirstMessage(Long reservationId);

    void sendSecondMessage(Long reservationId);

    List<Reservation> getReservationsByCompanyId(Long companyId);

    // Metode za RBAC
    boolean isReservationInCompany(Long reservationId, Long principalUserId);

    boolean isReservationOwnedByUser(Long reservationId, Long principalUserId);

    boolean isUserInCompany(Long userId, Long principalUserId);

    // Metoda za slobodne termine
    List<LocalTime> getAvailableSlots(Long workerId, LocalDate date, int trajanje, LocalTime defaultStartTime, LocalTime defaultEndTime);

}
