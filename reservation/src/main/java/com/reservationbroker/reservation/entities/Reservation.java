package com.reservationbroker.reservation.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "usluga_id", nullable = false)
    private Usluge usluga;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;

    @Column(nullable = false)
    private Boolean firstMsgSent = false;

    @Column(nullable = false)
    private Boolean secondMsgSent = false;

    @Column(nullable = false)
    private Integer vremeTrajanja = 30;

    @Column(name = "vreme_zavrsetka", nullable = false, insertable = false, updatable = false)
    private LocalDateTime vremeZavrsetka;


    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "confirmed")
    private Boolean confirmed = false;

    // Getteri i setteri

    // Method to set vremeZavrsetka based on the appointment date and duration
    public void calculateVremeZavrsetka() {
        if (this.appointmentDate != null && this.vremeTrajanja != null) {
            this.vremeZavrsetka = this.appointmentDate.plusMinutes(this.vremeTrajanja);
        }
    }public void setVremeTrajanja(Integer vremeTrajanja) {
        if (this.appointmentDate != null && this.vremeTrajanja != null) {
            this.vremeZavrsetka = this.appointmentDate.plusMinutes(this.vremeTrajanja);
        }
        this.vremeTrajanja = vremeTrajanja;
    }
    // Ostali getteri i setteri
}