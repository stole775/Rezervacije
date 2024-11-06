package com.reservationbroker.reservation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDTO {
    @NotNull
    @Size(max = 100)
    private String name;

    private Long industryId;
}
