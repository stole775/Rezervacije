package com.reservationbroker.reservation.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReservationRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long uslugaId;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Invalid phone number")
    private String phone;

    @NotNull
    private LocalDateTime appointmentDate;

    @NotNull
    @Min(1)
    private Integer vremeTrajanja;

    private Boolean firstMsgSent;

    private Boolean secondMsgSent;

    private Boolean confirmed;
}
