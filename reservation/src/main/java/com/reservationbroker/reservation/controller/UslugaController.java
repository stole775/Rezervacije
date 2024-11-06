package com.reservationbroker.reservation.controller;

import com.reservationbroker.reservation.entities.Usluge;
import com.reservationbroker.reservation.services.UslugaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usluge")
@AllArgsConstructor
public class UslugaController {

    private final UslugaService uslugaService;

    @PostMapping
    public ResponseEntity<Usluge> createUsluga(@RequestBody Usluge usluga) {
        Usluge createdUsluga = uslugaService.createUsluga(usluga);
        return ResponseEntity.ok(createdUsluga);
    }

    @GetMapping
    public ResponseEntity<List<Usluge>> getAllUsluge() {
        List<Usluge> usluge = uslugaService.getAllUsluge();
        return ResponseEntity.ok(usluge);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usluge> getUslugaById(@PathVariable Long id) {
        return uslugaService.getUslugaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
