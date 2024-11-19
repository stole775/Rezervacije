package com.reservationbroker.reservation.controller;

import com.reservationbroker.reservation.dto.ReservationRequest;
import com.reservationbroker.reservation.entities.Reservation;
import com.reservationbroker.reservation.entities.User;
import com.reservationbroker.reservation.entities.Usluge;
import com.reservationbroker.reservation.services.ReservationService;
import com.reservationbroker.reservation.services.UserService;
import com.reservationbroker.reservation.services.UslugaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final UserService userService;
    private final UslugaService uslugeService ;


    /**
     * Kreira novu rezervaciju.
     *
     * - SADMIN može da kreira rezervacije za bilo kog korisnika.
     * - CADMIN može da kreira rezervacije samo za korisnike unutar svoje kompanije.
     * - CUSTOMER može da kreira samo svoje rezervacije.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN', 'CUSTOMER')")
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
        try {
            // Log incoming reservation object
            System.out.println("Incoming Reservation: " + reservation);

            // Step 1: Fetch user role based on user_id from the reservation
            Long userId = Long.valueOf(reservation.getUser().getId()); // Get the user ID from the reservation
            System.out.println("User ID: " + userId);

            Optional<User> userOpt = userService.findUserbyId(userId); // Fetch user details from the service

            // Check if user exists
            if (!userOpt.isPresent()) {
                System.out.println("User not found for ID: " + userId);
                return ResponseEntity.badRequest().body("User not found.");
            }

            User user = userOpt.get(); // Extract the User object from Optional
            System.out.println("User Role: " + user.getRole().getName());
            String roleName = user.getRole().getName();

            // Step 2: Check if the user is an USER mozda ne treba
             if (user.getRole().getName().equals("USER"))
             {
                System.out.println("User is not USER, reservation unconfirmed.");
                reservation.setConfirmed(false); // If not admin, set it as unconfirmed
            }
            if ("CADMIN".equalsIgnoreCase(roleName)) {
                // Proveri da li korisnik za kojeg se kreira rezervacija pripada istoj kompaniji kao CADMIN
                Long principalUserId = reservation.getUser().getId(); // Implementiraj metodu za dobijanje ID-a prijavljenog korisnika
                boolean isInCompany = reservationService.isUserInCompany(userId, principalUserId);

                if (!isInCompany) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("CADMIN can only create reservations for users within their company.");
                }
            } else if ("CUSTOMER".equalsIgnoreCase(roleName)) {
                // CUSTOMER može da kreira samo svoje rezervacije
                Long principalUserId = reservation.getUser().getId(); // Implementiraj metodu za dobijanje ID-a prijavljenog korisnika
                if (!userId.equals(principalUserId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("CUSTOMER can only create reservations for themselves.");
                }
            }

            // Step 3: Set Confirmation Based on Role
            if ("CUSTOMER".equalsIgnoreCase(roleName)) {
                System.out.println("User is CUSTOMER, reservation unconfirmed.");
                reservation.setConfirmed(false); // Customers cannot auto-confirm
            } else {
                System.out.println("User is SADMIN or CADMIN, confirming reservation.");
                reservation.setConfirmed(true); // Admins auto-confirm
            }

            // Step 3: Calculate the end time (vremeZavrsetka)
            LocalDateTime appointmentDate = reservation.getAppointmentDate();
            Integer vremeTrajanja = reservation.getVremeTrajanja();

            // Ensure appointmentDate and vremeTrajanja are valid
            if (appointmentDate == null || vremeTrajanja == null || vremeTrajanja <= 0) {
                System.out.println("Invalid appointment date or duration. Date: " + appointmentDate + ", Duration: " + vremeTrajanja);
                return ResponseEntity.badRequest().body("Invalid appointment date or duration.");
            }

            // Add the duration to the appointment date to calculate the end time
            LocalDateTime vremeZavrsetka = appointmentDate.plusMinutes(vremeTrajanja);
            System.out.println("End time calculated: " + vremeZavrsetka);
            reservation.setVremeZavrsetka(vremeZavrsetka);

            // Step 4: Save the reservation to the database
            System.out.println(reservation );
            Reservation savedReservation = reservationService.createReservation(reservation);

            // Step 5: Return the saved reservation
            System.out.println("Reservation saved successfully: " + savedReservation);
            return ResponseEntity.ok(savedReservation);

        } catch (Exception e) {
            // Log the error for easier debugging
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create reservation: " + e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(@PathVariable Long id, @RequestBody Reservation reservationRequest) {

            Reservation updatedReservation = reservationService.updateReservation(reservationRequest);
            return ResponseEntity.ok(updatedReservation);

    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
//admin
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        for(Reservation reservation : reservations) {
            reservation.getUser().setPassword("****");
        }
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUserId(@PathVariable Long userId) {
        List<Reservation> reservations = reservationService.getReservationsByUserId(userId);
        for(Reservation reservation : reservations) {
            reservation.getUser().setPassword("****");
        }
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/user/companyId/{companyId}")
    public ResponseEntity<List<Reservation>> getReservationsByCompanyId(@PathVariable Long companyId) {
        List<Reservation> reservations = reservationService.getReservationsByCompanyId(companyId);
        for(Reservation reservation : reservations) {
            reservation.getUser().setPassword("****");
        }
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/available-slots")
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN', 'CUSTOMER')")
    public ResponseEntity<List<String>> getAvailableSlots(
            @RequestParam Long workerId,
            @RequestParam String date,
            @RequestParam int trajanje) {
        try {
            System.out.println("WorkerId: " + workerId);
            System.out.println("Date: " + date);
            System.out.println("Trajanje: " + trajanje);

            LocalDate localDate = LocalDate.parse(date);
            LocalTime defaultStartTime = LocalTime.of(8, 0);
            LocalTime defaultEndTime = LocalTime.of(16, 0);

            List<LocalTime> availableSlots = reservationService.getAvailableSlots(
                    workerId, localDate, trajanje, defaultStartTime, defaultEndTime);

            List<String> formattedSlots = new ArrayList<>();
            for (LocalTime slot : availableSlots) {
                formattedSlots.add(slot.toString());
            }

            return ResponseEntity.ok(formattedSlots);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(List.of("Error: " + e.getMessage()));
        }
    }



}
