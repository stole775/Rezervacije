package com.reservationbroker.reservation.repositories;

import com.reservationbroker.reservation.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByFirstMsgSentFalse();
    List<Reservation> findBySecondMsgSentFalse();
    @Query(value = "SELECT r.* FROM reservations r " +
            "JOIN users u ON u.id = r.user_id " +
            "WHERE u.company_id = :companyId", nativeQuery = true)
    List<Reservation> getReservationsByCompanyId(@Param("companyId") Long companyId);
    // Provera dostupnosti termina
    List<Reservation> findByUslugaIdAndAppointmentDateLessThanEqualAndVremeZavrsetkaGreaterThanEqual(
            Long uslugaId, LocalDateTime endTime, LocalDateTime startTime);
}
