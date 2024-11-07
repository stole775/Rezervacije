package com.reservationbroker.reservation.controller;

import com.reservationbroker.reservation.dto.RoleAssignmentRequest;
import com.reservationbroker.reservation.entities.Role;
import com.reservationbroker.reservation.services.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/roles")
@AllArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role savedRole = roleService.saveRole(role);
        return ResponseEntity.ok(savedRole);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Role> findByName(@PathVariable String name) {
        Role role = roleService.findByName(name);
        return ResponseEntity.ok(role);
    }

    @PostMapping("/assign")
    @PreAuthorize("hasAnyRole('SADMIN', 'CADMIN')")
    public ResponseEntity<Map<String, String>> assignRole(@RequestBody RoleAssignmentRequest request) {
        try {
            System.out.println("Assigning role {" + request.getRoleName() + "} to user {" + request.getUserId() + "} in company {" + request.getCompanyId() + "}");
            roleService.assignRoleToUser(request.getUserId(), request.getRoleName(), request.getCompanyId());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Role assigned successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Error assigning role" + e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error assigning role: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }






}
