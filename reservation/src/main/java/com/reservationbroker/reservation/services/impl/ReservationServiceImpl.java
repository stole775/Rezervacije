package com.reservationbroker.reservation.services.impl;

import com.reservationbroker.reservation.entities.Reservation;
import com.reservationbroker.reservation.entities.WorkingHour;
import com.reservationbroker.reservation.repositories.*;
import com.reservationbroker.reservation.services.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final CompanyService companyService;
    private final WorkingHourService workingHourService;
    private final UserService userService;

    @Override
    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation updateReservation(Reservation reservation) {
        Optional<Reservation> existingReservationOpt = reservationRepository.findById(reservation.getId());
        if (!existingReservationOpt.isPresent()) {
            throw new RuntimeException("Reservation not found with id :: " + reservation.getId());
        }
        Reservation existingReservation = existingReservationOpt.get();

        // Ažuriraj rezervaciju
        existingReservation.setName(reservation.getName());
        existingReservation.setPhone(reservation.getPhone());
        existingReservation.setAppointmentDate(reservation.getAppointmentDate());
        existingReservation.setVremeTrajanja(reservation.getVremeTrajanja());
        existingReservation.setVremeZavrsetka(reservation.getVremeZavrsetka());
        existingReservation.setUser(reservation.getUser());
        existingReservation.setUsluga(reservation.getUsluga());

        return reservationRepository.save(existingReservation);
    }

    @Override
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id :: " + id));
        reservationRepository.delete(reservation);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> getReservationsByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    @Override
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public void sendFirstMessage(Long reservationId) {
        // Implementacija slanja prve poruke
    }

    @Override
    public void sendSecondMessage(Long reservationId) {
        // Implementacija slanja druge poruke
    }

    @Override
    public List<Reservation> getReservationsByCompanyId(Long companyId) {
        return reservationRepository.getReservationsByCompanyId(companyId);
    }

    @Override
    public boolean isReservationInCompany(Long reservationId, Long principalUserId) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isPresent()) {
            Long reservationUserId = reservationOpt.get().getUser().getId();
            return companyService.isUserInCompany(reservationUserId, principalUserId);
        }
        return false;
    }

    @Override
    public boolean isReservationOwnedByUser(Long reservationId, Long principalUserId) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        return reservationOpt.map(reservation -> reservation.getUser().getId().equals(principalUserId)).orElse(false);
    }

    @Override
    public boolean isUserInCompany(Long userId, Long principalUserId) {
        return companyService.isUserInCompany(userId, principalUserId);
    }
    private Long getCompanyIdByWorker(Long workerId) {
        return userService.findUserbyId(workerId)
                .orElseThrow(() -> new RuntimeException("Radnik nije pronađen"))
                .getCompany()
                .getId();
    }

    @Override
    public List<LocalTime> getAvailableSlots(Long workerId, LocalDate date, int trajanje, LocalTime defaultStartTime, LocalTime defaultEndTime) {
        // Log input parameters
        System.out.println("Input Parameters - Worker ID: " + workerId + ", Date: " + date + ", Duration: " + trajanje);
        System.out.println("Default Start Time: " + defaultStartTime + ", Default End Time: " + defaultEndTime);

        // Kombinovanje datuma sa radnim vremenom
        LocalDateTime startOfDay = date.atTime(defaultStartTime);
        LocalDateTime endOfDay = date.atTime(defaultEndTime);
        System.out.println("startOfDay: " + startOfDay + ", endOfDay: " + endOfDay);

        // Fetch rezervacije između početka i kraja radnog dana
        List<Reservation> reservations = reservationRepository.findByWorkerIdAndDate(workerId, startOfDay, endOfDay);
        System.out.println("Fetched Reservations: " + reservations);

        // Fetch radne sate za radnika i dan u nedelji
        Optional<WorkingHour> workingHourOpt = workingHourService.getWorkingHoursForCompanyAndDay(
                getCompanyIdByWorker(workerId), com.reservationbroker.reservation.enums.DayOfWeek.valueOf(date.getDayOfWeek().name()));

        // Ako postoje radni sati, ažuriraj početno i krajnje vreme
        if (workingHourOpt.isPresent()) {
            startOfDay = date.atTime(workingHourOpt.get().getStartTime());
            endOfDay = date.atTime(workingHourOpt.get().getEndTime());
        }
        System.out.println("Updated Start Time: " + startOfDay + ", Updated End Time: " + endOfDay);

        // Generisanje slobodnih termina
        List<LocalTime> availableSlots = new ArrayList<>();
        LocalDateTime currentTime = startOfDay;

        while (currentTime.plusMinutes(trajanje).isBefore(endOfDay) || currentTime.plusMinutes(trajanje).equals(endOfDay)) {
            LocalDateTime slotStartTime = currentTime;
            LocalDateTime slotEndTime = slotStartTime.plusMinutes(trajanje);

            // Provera da li se slot preklapa sa postojećim rezervacijama
            boolean isOverlapping = reservations.stream().anyMatch(reservation -> {
                LocalDateTime reservedStart = reservation.getAppointmentDate();
                LocalDateTime reservedEnd = reservation.getVremeZavrsetka();
                return !(slotEndTime.isBefore(reservedStart) || slotStartTime.isAfter(reservedEnd));
            });

            // Ako nema preklapanja, dodaj slot u listu
            if (!isOverlapping) {
                availableSlots.add(slotStartTime.toLocalTime());
            }

            currentTime = currentTime.plusMinutes(trajanje);
        }

        System.out.println("Available Slots: " + availableSlots);
        return availableSlots;
    }






}
