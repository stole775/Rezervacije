package com.reservationbroker.reservation.services.impl;


import com.reservationbroker.reservation.entities.User;
import com.reservationbroker.reservation.repositories.RoleRepository;
import com.reservationbroker.reservation.repositories.UserRepository;
import com.reservationbroker.reservation.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {


    final private UserRepository userRepository;

    final private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final RoleRepository roleRepository;

    @Override
    public User registerUser(User user)  {
        // Check if the role is provided and if it's 1 (ADMIN), which is restricted
        if (user.getRole() != null && user.getRole().getId() == 1) {
            throw new RuntimeException("Role ID 1 is reserved for admins and cannot be assigned.");
        }

        // Set default role if no valid role is provided (only 2 or 3 are allowed)
        if (user.getRole() == null || (user.getRole().getId() != 2 && user.getRole().getId() != 3)) {
            user.setRole(roleRepository.findById(2L).orElseThrow(() -> new RuntimeException("Role not found.")));
        }

        // Encrypt the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default values
        user.setBlocked(false);  // Default blocked status
        user.setCreatedAt(Instant.now());  // Automatically set the created date

        // Save the user to the database
        return userRepository.save(user);
    }


    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User blockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id :: " + userId));
        user.setBlocked(true);
        return userRepository.save(user);
    }

    @Override
    public User unblockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id :: " + userId));
        user.setBlocked(false);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findUserbyId(Long id) {
        return userRepository.findById(id);
    }
    public User updateUser(User user) {
        return userRepository.save(user); // Saves the updated user in the database
    }

    @Override
    public List<User> getUsersByCompanyId(Long companyId) {
        return userRepository.getUsersByCompanyId(companyId);
    }

    @Override
    public List<User> getBlockedUsers(Long companyId, String role) {
        if (role.equals("SADMIN")) {
            return userRepository.findAllByBlocked(true);
        } else if (role.equals("CADMIN")) {
            return userRepository.findAllByCompanyIdAndBlocked(companyId, true);
        } else {
            throw new RuntimeException("Unauthorized role");
        }
    }

    @Override
    public List<User> getAllWorkers() {
        return userRepository.findAllByRole("WORKER");
    }

    public List<User> getWorkersByCompanyId(Long companyId) {
        return userRepository.findWorkersByCompanyId( companyId);
    }


}
