package com.reservationbroker.reservation.controller;

import com.reservationbroker.reservation.services.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<Boolean> sendMessage(@RequestParam Long userId, @RequestParam Long reservationId, @RequestParam String messageContent) {
        boolean sent = messageService.sendMessage(userId, reservationId, messageContent);
        return ResponseEntity.ok(sent);
    }
}
