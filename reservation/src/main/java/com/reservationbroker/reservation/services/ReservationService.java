package com.reservationbroker.reservation.services;

import com.reservationbroker.reservation.entities.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationService {
    Reservation createReservation(Reservation reservationRequest);
    Reservation updateReservation(Reservation reservationRequest);
    void deleteReservation(Long id);
    List<Reservation> getAllReservations();
    List<Reservation> getReservationsByUserId(Long userId);
    Optional<Reservation> getAllReservationsById(Long id);
    Optional<Reservation> getReservationById(Long id);
    void sendFirstMessage(Long reservationId);
    void sendSecondMessage(Long reservationId);

    List<Reservation> getReservationsByCompanyId( Long companyId);

    // Metode za RBAC
    boolean isReservationInCompany(Long reservationId, Long principalUserId);
    boolean isReservationOwnedByUser(Long reservationId, Long principalUserId);
    boolean isUserInCompany(Long userId, Long principalUserId);
}
