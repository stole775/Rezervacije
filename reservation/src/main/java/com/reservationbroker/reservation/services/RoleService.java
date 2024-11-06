package com.reservationbroker.reservation.services;


import com.reservationbroker.reservation.entities.Role;

public interface RoleService {
    Role findByName(String name);
    Role saveRole(Role role);
    void assignRoleToUser(Long userId, String roleName, Long companyId);

}