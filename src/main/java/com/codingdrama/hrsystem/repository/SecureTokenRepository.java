package com.codingdrama.hrsystem.repository;

import com.codingdrama.hrsystem.model.SecureToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SecureTokenRepository extends JpaRepository<SecureToken, Long> {
    SecureToken findByUserId(Long userId);
    SecureToken findByToken(String token);
}
