package com.reservationbroker.reservation.controller;

import com.reservationbroker.reservation.entities.Usluge;
import com.reservationbroker.reservation.repositories.UserUslugaRepository;
import com.reservationbroker.reservation.services.UserUslugaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user-usluge")
@AllArgsConstructor
public class UserUslugaController {

    private final UserUslugaService userUslugaService;
    private final UserUslugaRepository userUslugaRepository;

   /* @GetMapping("")
    public ResponseEntity<List<Usluge>> getAll() {
        List<Usluge> usluge = userUslugaService.getAllUsluge();
        return ResponseEntity.ok(usluge);
    }*/



    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Usluge>> getUslugeByUserId(@PathVariable Long userId) {
        List<Usluge> usluge = userUslugaService.getUslugeByUserId(userId);
        return ResponseEntity.ok(usluge);
    }
    @GetMapping("/user/{userId}/usluge")
    public ResponseEntity<List<Object[]>> getUserUsluge(@PathVariable Long userId) {
        List<Object[]> usluge = userUslugaService.findUslugeByUserId(userId);
        return ResponseEntity.ok(usluge);
    }
    @GetMapping("/user/{userId}/usluge2")
    public ResponseEntity<List<Map<String, Object>>> getUserUsluge2(@PathVariable Long userId) {
        List<Object[]> uslugeList = userUslugaRepository.findUslugeByUserId(userId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] obj : uslugeList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", obj[0]);        // ID usluge
            map.put("naziv", obj[1]);    // Naziv usluge
            map.put("cena", obj[2]);     // Cena usluge
            map.put("trajanje", obj[3]); // Trajanje termina
            result.add(map);
        }

        return ResponseEntity.ok(result);
    }


    @PostMapping("/assign")
    public ResponseEntity<String> assignUslugaToUser(
            @RequestParam Long userId,
            @RequestParam Long uslugaId,
            @RequestParam Long companyId,
            @RequestParam double price) {

        userUslugaService.assignUslugaToUser(userId, uslugaId, companyId, price);
        return ResponseEntity.ok("Service assigned to user with price successfully");
    }

}
