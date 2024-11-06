package com.reservationbroker.reservation.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleAssignmentRequest {
    private Long userId;
    private String roleName;
    private Long companyId;

    // Getteri i setteri
}

