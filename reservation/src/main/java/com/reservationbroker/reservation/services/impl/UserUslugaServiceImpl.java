package com.reservationbroker.reservation.services.impl;


import com.reservationbroker.reservation.entities.User;
import com.reservationbroker.reservation.entities.UserUsluga;
import com.reservationbroker.reservation.entities.UserUslugaId;
import com.reservationbroker.reservation.entities.Usluge;
import com.reservationbroker.reservation.repositories.UserRepository;
import com.reservationbroker.reservation.repositories.UserUslugaRepository;
import com.reservationbroker.reservation.repositories.UslugaRepository;
import com.reservationbroker.reservation.services.UserUslugaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserUslugaServiceImpl implements UserUslugaService {


    final UserRepository userRepository;

    final private UslugaRepository uslugaRepository;

    final private UserUslugaRepository userUslugaRepository;

    @Override
    public void addUslugaToUser(Long userId, Long uslugaId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id :: " + userId));
        Usluge usluga = uslugaRepository.findById(uslugaId)
                .orElseThrow(() -> new RuntimeException("Usluga not found with id :: " + uslugaId));

        if (userUslugaRepository.existsByUserIdAndUslugaId(userId, uslugaId)) {
            throw new RuntimeException("Usluga već dodeljena korisniku.");
        }

        UserUslugaId id = new UserUslugaId();
        id.setUserId(Math.toIntExact(userId));
        id.setUslugaId(uslugaId);

        UserUsluga userUsluga = new UserUsluga();
        userUsluga.setId(id);
        userUsluga.setUser(user);
        userUsluga.setUsluga(usluga);

        userUslugaRepository.save(userUsluga);
    }

    @Override
    public List<Usluge> getUslugeByUserId(Long userId) {
        List<UserUsluga> userUsluge = userUslugaRepository.findByUserId(userId);
        return userUsluge.stream()
                .map(UserUsluga::getUsluga)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getAllUsluge() {
        return userRepository.findAll();
    }

    @Override
    public List<Object[]> findUslugeByUserId(Long userId) {
        return userUslugaRepository.findUslugeByUserId(userId);
    }

    @Override
    public void assignUslugaToUser(Long userId, Long uslugaId, Long companyId, double price) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Usluge usluga = uslugaRepository.findById(uslugaId).orElseThrow(() -> new RuntimeException("Service not found"));

        if (!user.getCompany().getId().equals(companyId)) {
            throw new RuntimeException("Cannot assign services to users outside of the company");
        }

        UserUsluga userUsluga = new UserUsluga();
        userUsluga.setUser(userRepository.getById(user.getId()));
        userUsluga.setUsluga(usluga);
        userUsluga.setCena(price); // Postavljanje cene prilikom dodeljivanja usluge
        userUslugaRepository.save(userUsluga);
    }
}
