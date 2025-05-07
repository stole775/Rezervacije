package com.reservationbroker.reservation.dto;

import lombok.Data;

@Data
public class SettingsDto {
    private int companyId;
    private int numberOfMessages;
    private int hoursBeforeFirstMsg;
    private Integer hoursBeforeSecondMsg;
    private int daysToKeep;
    private String messageTemplate;
    private boolean prikaziCene;
    private boolean cenovnik;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String zip;
    private String timezone;
    private String buttonShape;
    private String theme;
    private String imageUrlLogo;
    private String imageUrlBackground;


    // Getteri i setteri
}
