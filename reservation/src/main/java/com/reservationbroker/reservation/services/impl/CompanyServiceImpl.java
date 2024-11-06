package com.reservationbroker.reservation.services.impl;

import com.reservationbroker.reservation.entities.Company;
import com.reservationbroker.reservation.entities.User;
import com.reservationbroker.reservation.repositories.CompanyRepository;
import com.reservationbroker.reservation.repositories.UserRepository;
import com.reservationbroker.reservation.services.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Override
    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company updateCompany(Long id, Company companyDetails) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id :: " + id));
        company.setName(companyDetails.getName());
        // Ako imaš dodatna polja, postavi ih ovde
        return companyRepository.save(company);
    }

    @Override
    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id :: " + id));
        companyRepository.delete(company);
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    @Override
    public Optional<Company> getCompanyByName(String name) {
        return companyRepository.findByName(name);
    }

    @Override
    public boolean isUserInCompany(Long userId, Long principalUserId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<User> principalUserOpt = userRepository.findById(principalUserId);

        if (userOpt.isPresent() && principalUserOpt.isPresent()) {
            Company userCompany = userOpt.get().getCompany();
            Company principalCompany = principalUserOpt.get().getCompany();
            return userCompany != null && userCompany.equals(principalCompany);
        }
        return false;
    }
}
