package com.reservationbroker.reservation.repositories;

import com.reservationbroker.reservation.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> getUsersByCompanyId(Long companyId);
    List<User> findAllByBlocked(boolean blocked);
    List<User> findAllByCompanyIdAndBlocked(Long companyId, boolean blocked);
    @Query("SELECT u FROM User u WHERE u.role.id = 6")
    List<User> findAllByRole(String worker);
    @Query("SELECT u FROM User u WHERE u.role.name = 'WORKER' AND u.company.id = :companyId")
    List<User> findWorkersByCompanyId(@Param("companyId") Long companyId);

}