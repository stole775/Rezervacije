package com.reservationbroker.reservation.services.impl;

import com.reservationbroker.reservation.entities.MessageLog;
import com.reservationbroker.reservation.entities.User;
import com.reservationbroker.reservation.repositories.MessageLogRepository;
import com.reservationbroker.reservation.repositories.UserRepository;
import com.reservationbroker.reservation.services.CompanyService;
import com.reservationbroker.reservation.services.MessageLogService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageLogServiceImpl implements MessageLogService {

    private final MessageLogRepository messageLogRepository;
    private final UserRepository userRepository;
    private final CompanyService companyService;

    @Override
    public MessageLog saveMessageLog(Long userId, String messageContent, String status, Authentication authentication) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id :: " + userId));

        MessageLog messageLog = new MessageLog();
        messageLog.setUser(user);
        messageLog.setMessageContent(messageContent);
        messageLog.setStatus(MessageLog.Status.valueOf(status.toUpperCase()));

        return messageLogRepository.save(messageLog);
    }

    @Override
    public List<MessageLog> findMessageLogsByUserId(Long userId) {
        return messageLogRepository.findByUserId(userId);
    }

    @Override
    public Optional<MessageLog> findMessageLogById(Long id) {
        return messageLogRepository.findById(id);
    }

    @Override
    public List<MessageLog> findAllMessageLogs() {
        return messageLogRepository.findAll();
    }

    @Override
    public void deleteMessageLogById(Long id) {
        MessageLog log = messageLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MessageLog not found with id :: " + id));
        messageLogRepository.delete(log);
    }

    @Override
    public boolean isLogInCompany(Long logId, Long principalUserId) {
        Optional<MessageLog> logOpt = messageLogRepository.findById(logId);
        if (logOpt.isPresent()) {
            MessageLog log = logOpt.get();
            Long logUserId = log.getUser().getId();
            return companyService.isUserInCompany(logUserId, principalUserId);
        }
        return false;
    }

    @Override
    public boolean isLogOwnedByUser(Long logId, Long principalUserId) {
        Optional<MessageLog> logOpt = messageLogRepository.findById(logId);
        if (logOpt.isPresent()) {
            MessageLog log = logOpt.get();
            return log.getUser().getId().equals(principalUserId);
        }
        return false;
    }
}
