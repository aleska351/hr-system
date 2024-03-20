package com.codingdrama.hrsystem.repository;


import com.codingdrama.hrsystem.model.Company;
import com.codingdrama.hrsystem.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByNameAndAndCompany(String name, Company company);
    
    boolean existsByNameAndCompany(String companyCode, Company company);
    
}
