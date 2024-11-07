package com.reservationbroker.reservation.controller;

import com.reservationbroker.reservation.entities.MessageLog;
import com.reservationbroker.reservation.services.MessageLogService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/api/message-logs")
@AllArgsConstructor
public class MessageLogController {

    private final MessageLogService messageLogService;

    // Create a new message log
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN')")
    public ResponseEntity<MessageLog> saveMessageLog(@RequestParam Long userId,
                                                     @RequestParam String messageContent,
                                                     @RequestParam String status,
                                                     Authentication authentication) {
        // Opcionalno: Proveri da li CADMIN može da kreira logove samo za korisnike u svojoj kompaniji
        MessageLog savedLog = messageLogService.saveMessageLog(userId, messageContent, status, authentication);
        return ResponseEntity.ok(savedLog);
    }

    // Get message logs by user ID
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('SADMIN') or " +
            "(hasRole('CADMIN') and @companyService.isUserInCompany(#userId, principal.id)) or " +
            "(hasRole('CUSTOMER') and #userId == principal.id)")
    public ResponseEntity<List<MessageLog>> getMessageLogsByUserId(@PathVariable Long userId) {
        List<MessageLog> logs = messageLogService.findMessageLogsByUserId(userId);
        return logs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(logs);
    }

    // Get a specific message log by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SADMIN') or " +
            "(hasRole('CADMIN') and @messageLogService.isLogInCompany(#id, principal.id)) or " +
            "(hasRole('CUSTOMER') and @messageLogService.isLogOwnedByUser(#id, principal.id))")
    public ResponseEntity<MessageLog> getMessageLogById(@PathVariable Long id) {
        Optional<MessageLog> log = messageLogService.findMessageLogById(id);
        return log.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get all message logs
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN')")
    public ResponseEntity<List<MessageLog>> getAllMessageLogs() {
        List<MessageLog> logs = messageLogService.findAllMessageLogs();
        return logs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(logs);
    }

    // Delete a message log by ID
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('SADMIN') or " +
            "(hasRole('CADMIN') and @messageLogService.isLogInCompany(#id, principal.id))")
    public ResponseEntity<Void> deleteMessageLogById(@PathVariable Long id) {
        messageLogService.deleteMessageLogById(id);
        return ResponseEntity.ok().build();
    }
}
