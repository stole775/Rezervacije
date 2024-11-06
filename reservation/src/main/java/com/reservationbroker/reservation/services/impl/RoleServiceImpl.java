package com.reservationbroker.reservation.services.impl;

import com.reservationbroker.reservation.entities.Role;
import com.reservationbroker.reservation.entities.User;
import com.reservationbroker.reservation.repositories.RoleRepository;
import com.reservationbroker.reservation.repositories.UserRepository;
import com.reservationbroker.reservation.services.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {


    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Role not found: " + name));
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void assignRoleToUser(Long userId, String roleName, Long companyId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Proverava da li SAdmin može dodeliti SAdmin ulogu
        if (roleName.equals("SADMIN") && !user.getRole().getName().equals("SADMIN")) {
            throw new RuntimeException("Only SAdmin can assign the SADMIN role");
        }

        // Sprečava dodelu uloga izvan kompanije za CAdmin-a
        if ((roleName.equals("CADMIN") || roleName.equals("CUSTOMER")) && !user.getCompany().getId().equals(companyId)) {
            throw new RuntimeException("Cannot assign roles outside of the company");
        }

        // Dodela nove uloge
        user.setRole(role);
        userRepository.save(user);
    }

}