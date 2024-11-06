package com.reservationbroker.reservation.repositories;

import com.reservationbroker.reservation.entities.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageLogRepository extends JpaRepository<MessageLog, Long> {
    List<MessageLog> findByUserId(Long userId);
}
