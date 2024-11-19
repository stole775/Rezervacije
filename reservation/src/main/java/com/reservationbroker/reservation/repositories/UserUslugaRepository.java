package com.reservationbroker.reservation.repositories;

import com.reservationbroker.reservation.entities.UserUsluga;
import com.reservationbroker.reservation.entities.UserUslugaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserUslugaRepository extends JpaRepository<UserUsluga, UserUslugaId> {
    List<UserUsluga> findByUserId(Long userId);
    boolean existsByUserIdAndUslugaId(Long userId, Long uslugaId);
    @Query(value = "SELECT u.id, u.naziv, uu.cena, uu.trajanje FROM user_usluga uu " +
            "JOIN usluge u ON uu.usluga_id = u.id " +
            "WHERE uu.user_id = :userId", nativeQuery = true)
    List<Object[]> findUslugeByUserId(@Param("userId") Long userId);



}