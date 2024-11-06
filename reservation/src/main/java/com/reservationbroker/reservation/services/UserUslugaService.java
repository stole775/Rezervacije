package com.reservationbroker.reservation.services;


import com.reservationbroker.reservation.entities.User;
import com.reservationbroker.reservation.entities.Usluge;

import java.util.List;

public interface UserUslugaService {
    void addUslugaToUser(Long userId, Long uslugaId);
    List<Usluge> getUslugeByUserId(Long userId);
    List<User> getAllUsluge();
    List<Object[]> findUslugeByUserId(Long userId);
    void assignUslugaToUser(Long userId, Long uslugaId, Long companyId, double price);

}