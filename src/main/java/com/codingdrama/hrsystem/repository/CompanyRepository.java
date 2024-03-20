package com.codingdrama.hrsystem.repository;


import com.codingdrama.hrsystem.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByCompanyCode(String code);
    
    boolean existsByCompanyCode(String companyCode);
    
}
