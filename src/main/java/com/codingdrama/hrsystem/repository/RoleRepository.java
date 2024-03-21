package com.codingdrama.hrsystem.repository;

import com.codingdrama.hrsystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

    List<Role> findByNameIn(List<String> names);
}
