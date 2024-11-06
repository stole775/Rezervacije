package com.reservationbroker.reservation.services;

import com.reservationbroker.reservation.entities.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
    Company createCompany(Company company);
    Company updateCompany(Long id, Company company);
    void deleteCompany(Long id);
    List<Company> getAllCompanies();
    Optional<Company> getCompanyById(Long id);
    Optional<Company> getCompanyByName(String name);
    boolean isUserInCompany(Long userId, Long principalUserId);
}
