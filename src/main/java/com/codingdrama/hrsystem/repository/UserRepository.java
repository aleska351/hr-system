package com.codingdrama.hrsystem.repository;


import com.codingdrama.hrsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByDepartmentId(Long departmentId);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
