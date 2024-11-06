package com.reservationbroker.reservation.services;

import com.reservationbroker.reservation.entities.MessageLog;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface MessageLogService {
    MessageLog saveMessageLog(Long userId, String messageContent, String status, Authentication authentication);
    List<MessageLog> findMessageLogsByUserId(Long userId);
    Optional<MessageLog> findMessageLogById(Long id);
    List<MessageLog> findAllMessageLogs();
    void deleteMessageLogById(Long id);

    boolean isLogInCompany(Long logId, Long principalUserId);
    boolean isLogOwnedByUser(Long logId, Long principalUserId);
}
