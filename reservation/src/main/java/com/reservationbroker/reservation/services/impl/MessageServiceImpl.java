package com.reservationbroker.reservation.services.impl;

import com.reservationbroker.reservation.entities.MessageLog;
import com.reservationbroker.reservation.entities.Reservation;
import com.reservationbroker.reservation.entities.MessageLog.Status;
import com.reservationbroker.reservation.repositories.MessageLogRepository;
import com.reservationbroker.reservation.repositories.ReservationRepository;
import com.reservationbroker.reservation.services.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final ReservationRepository reservationRepository;
    private final MessageLogRepository messageLogRepository;

    @Override
    public boolean sendMessage(Long userId, Long reservationId, String messageContent) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id :: " + reservationId));

        // Simulacija slanja poruke preko eksternog servisa
        boolean messageSent = sendExternalMessage(reservation.getPhone(), messageContent); // Zameni sa stvarnom logikom

        MessageLog messageLog = new MessageLog();
        messageLog.setUser(reservation.getUser()); // Postavljanje korisnika
        messageLog.setMessageContent(messageContent);
        messageLog.setSentAt(Instant.now()); // Postavljanje vremena slanja

        if (messageSent) {
            messageLog.setStatus(Status.SENT);
        } else {
            messageLog.setStatus(Status.FAILED);
        }

        messageLogRepository.save(messageLog);
        return messageSent;
    }

    private boolean sendExternalMessage(String phoneNumber, String messageContent) {
        // Simulacija slanja poruke, zameni sa stvarnom logikom (npr. Twilio API poziv)
        try {
            // Simulacija uspeha
            System.out.println("Message sent to: " + phoneNumber + " with content: " + messageContent);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to send message to: " + phoneNumber);
            return false;
        }
    }
}
