package com.reservationbroker.reservation.services;


import com.reservationbroker.reservation.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> getAllUsers();
    User blockUser(Long userId);
    User unblockUser(Long userId);
    Optional<User> findUserbyId(Long id);
    User updateUser(User user);
    List<User> getUsersByCompanyId(Long companyId);
    List<User> getBlockedUsers(Long companyId, String role);
    List<User> getAllWorkers();
    public List<User> getWorkersByCompanyId(Long companyId) ;

}
