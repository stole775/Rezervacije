package com.reservationbroker.reservation.controller;

import com.reservationbroker.reservation.entities.Role;
import com.reservationbroker.reservation.entities.User;
import com.reservationbroker.reservation.services.UserService;
import com.reservationbroker.reservation.services.impl.RoleServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleServiceImpl roleServiceImpl;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            registeredUser.setPassword("****");
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userService.findUserbyId(id);

        if (userOpt.isPresent()) {
            userOpt.get().setPassword("****");
            return ResponseEntity.ok(userOpt.get()); // Return the found user
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + id); // Return 404 if user not found
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        for(User user : users) {
            user.setPassword("****");
        }
        return ResponseEntity.ok(users);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN', 'CUSTOMER', 'WORKER')")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        try {
            // Pronađi postojećeg korisnika po ID-u
            Optional<User> existingUserOpt = userService.findUserbyId(id);
            //  System.out.println("Pokušaj ažuriranja korisnika sa ID: " + id);

            if (existingUserOpt.isPresent()) {
                User existingUser = existingUserOpt.get();
                //  System.out.println("Postojeći korisnik: " + existingUser);

                // Provera da li već postoji korisnik sa istim korisničkim imenom (username)
                Optional<User> userWithSameUsername = userService.findByUsername(updatedUser.getUsername());
                // System.out.println("Traženje korisnika sa korisničkim imenom: " + updatedUser.getUsername());
                if (userWithSameUsername.isPresent()) {
                    User foundUser = userWithSameUsername.get();
                    //System.out.println("Pronađen korisnik sa istim korisničkim imenom: " + foundUser);

                    if (!foundUser.getId().equals(id)) {
                        //  System.out.println("Korisničko ime već postoji kod drugog korisnika. ID drugog korisnika: " + foundUser.getId());
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(Map.of("field", "username", "message", "Korisničko ime '" + updatedUser.getUsername() + "' već postoji."));
                    }
                }

                // Provera da li već postoji korisnik sa istim emailom
                Optional<User> userWithSameEmail = userService.findByEmail(updatedUser.getEmail());
                // System.out.println("Traženje korisnika sa emailom: " + updatedUser.getEmail());
                if (userWithSameEmail.isPresent()) {
                    User foundUser = userWithSameEmail.get();
                    // System.out.println("Pronađen korisnik sa istim emailom: " + foundUser);

                    if (!foundUser.getId().equals(id)) {
                        // System.out.println("Email već postoji kod drugog korisnika. ID drugog korisnika: " + foundUser.getId());
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(Map.of("field", "email", "message", "Email '" + updatedUser.getEmail() + "' već postoji."));
                    }
                }

                // Ažuriranje samo onih polja koja su poslana u zahtevu
                existingUser.setUsername(updatedUser.getUsername());
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setIme(updatedUser.getIme());
                existingUser.setPrezime(updatedUser.getPrezime());
                existingUser.setSmsNaslov(updatedUser.getSmsNaslov());

                // Ažuriranje polja 'blocked'
                existingUser.setBlocked(updatedUser.getBlocked());

                // Čuvanje uloge ako nije poslata u zahtevu
                if (updatedUser.getRole() != null) {
                    existingUser.setRole(updatedUser.getRole());
                }

                // Ako se menja lozinka (iako nije u ovom primeru)
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty() && !updatedUser.getPassword().equals("****")) {
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                // Čuvanje korisnika nazad u bazu
                // System.out.println("Ažurirani podaci korisnika: " + existingUser);
                userService.updateUser(existingUser);

                return ResponseEntity.ok(existingUser); // Vraćanje ažuriranog korisnika
            } else {
                // System.out.println("Korisnik sa ID: " + id + " nije pronađen.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + id);
            }

        } catch (Exception e) {
            //System.out.println("Greška tokom ažuriranja korisnika: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user: " + e.getMessage());
        }
    }


    @GetMapping("/workers")
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN', 'CUSTOMER', 'WORKER')")
    public ResponseEntity<List<User>> getAllWorkers() {
        List<User> workers = userService.getAllWorkers();
        for(User user : workers) {
            user.setPassword("****");
        }
        return ResponseEntity.ok(workers);
    }

    @GetMapping("/workersByCompany")
    public ResponseEntity<List<User>> getWorkersByCompanyId(@RequestParam Long companyId) {
        List<User> workers = userService.getWorkersByCompanyId(companyId);
        workers.forEach(w -> w.setPassword("****"));
        return ResponseEntity.ok(workers);
    }




    @GetMapping("/companyId/{companyId}")
    public ResponseEntity<List<User>> getUsersByCompanyId(@PathVariable Long companyId) {
        List<User> users = userService.getUsersByCompanyId(companyId);
        // Maskiranje lozinki i mapiranje na DTO
        List<User> userDTOs = users.stream()
                .peek(user -> user.setPassword("****"))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }
    @PostMapping("/change-password")
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN', 'CUSTOMER', 'WORKER')")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody Map<String, String> passwordData) {
        Long userId = Long.parseLong(passwordData.get("userId"));
        String newPassword = passwordData.get("newPassword");

        // Pronalaženje korisnika po ID-u
        Optional<User> userOpt = userService.findUserbyId(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Postavi novu lozinku (kodiranu)
            user.setPassword(passwordEncoder.encode(newPassword));
            userService.updateUser(user);

            // Vraćanje JSON objekta umesto plain text-a
            Map<String, String> response = new HashMap<>();
            response.put("message", "Lozinka uspešno promenjena.");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Korisnik nije pronađen.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }


    @PostMapping("/{userId}/block")
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN')")
    public ResponseEntity<Map<String, String>> blockUser(@PathVariable Long userId) {
        userService.blockUser(userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User blocked successfully");
        return ResponseEntity.ok(response); // Vraćamo JSON objekat sa porukom
    }

    @PostMapping("/{userId}/unblock")
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN', 'WORKER')")
    public ResponseEntity<Map<String, String>> unblockUser(@PathVariable Long userId) {
        userService.unblockUser(userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Lozinka uspešno promenjena.");
        return ResponseEntity.ok(response);

    }



}