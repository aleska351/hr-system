package com.codingdrama.hrsystem.repository;


import com.codingdrama.hrsystem.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Department, Long> {
    
}
