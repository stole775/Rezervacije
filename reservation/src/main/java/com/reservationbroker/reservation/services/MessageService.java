package com.reservationbroker.reservation.services;


public interface MessageService {
    boolean sendMessage(Long userId, Long reservationId, String messageContent);
}