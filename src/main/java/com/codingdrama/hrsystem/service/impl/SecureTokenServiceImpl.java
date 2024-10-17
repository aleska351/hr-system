package com.codingdrama.hrsystem.service.impl;

import com.codingdrama.hrsystem.model.SecureToken;
import com.codingdrama.hrsystem.repository.SecureTokenRepository;
import com.codingdrama.hrsystem.service.SecureTokenService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

@Service
public class SecureTokenServiceImpl implements SecureTokenService {

    @Getter
    @Value("${email.secure.token.expired}")
    private int tokenExpiredInSeconds;

    private final SecureTokenRepository secureTokenRepository;

    public SecureTokenServiceImpl(SecureTokenRepository secureTokenRepository) {
        this.secureTokenRepository = secureTokenRepository;
    }

    @Override
    public SecureToken createSecureToken(Long userId){
        String randomToken = String.format("%06d", new Random().nextInt(999999));
        SecureToken secureToken = new SecureToken();
        secureToken.setToken(randomToken);
        secureToken.setUserId(userId);
        secureToken.setExpireAt(LocalDateTime.now().plusSeconds(getTokenExpiredInSeconds()));
        this.saveSecureToken(secureToken);
        return secureToken;
    }

    @Override
    public void saveSecureToken(SecureToken token) {
        SecureToken existedToken = findByUserId(token.getUserId());
        if(Objects.nonNull(existedToken)){
            removeToken(existedToken);
        }
        secureTokenRepository.save(token);
    }

    @Override
    public SecureToken findByToken(String token) {
        return secureTokenRepository.findByToken(token);
    }

    @Override
    public SecureToken findByUserId(Long userId) {
        return secureTokenRepository.findByUserId(userId);
    }

    @Override
    public void removeToken(SecureToken token) {
        secureTokenRepository.delete(token);
    }
}
