package com.codingdrama.hrsystem.service.impl;

import com.codingdrama.hrsystem.exceptions.LocalizedResponseStatusException;
import com.codingdrama.hrsystem.model.SecureToken;
import com.codingdrama.hrsystem.model.User;
import com.codingdrama.hrsystem.repository.UserRepository;
import com.codingdrama.hrsystem.security.AuthenticatedUserDetails;
import com.codingdrama.hrsystem.security.jwt.JwtTokenProvider;
import com.codingdrama.hrsystem.service.AuthService;
import com.codingdrama.hrsystem.service.SecureTokenService;
import com.codingdrama.hrsystem.service.dto.LoginRequestDto;
import com.codingdrama.hrsystem.service.dto.LoginResponseDto;
import com.codingdrama.hrsystem.service.dto.UserDto;
import com.codingdrama.hrsystem.service.email.context.AccountRecoveryEmailContext;
import com.codingdrama.hrsystem.service.email.context.AccountVerificationEmailContext;
import com.codingdrama.hrsystem.service.email.context.PasswordResetEmailVerificationContext;
import com.codingdrama.hrsystem.service.email.context.SuccessLoginEmailContext;
import com.codingdrama.hrsystem.service.email.service.EmailService;
import com.codingdrama.hrsystem.util.UserUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    private final SecureTokenService secureTokenService;
    private PasswordEncoder passwordEncoder;

    private ModelMapper modelMapper;

    @Value("${auth.password.expired}")
    private int passwordExpirationTime;


    public AuthServiceImpl(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, EmailService emailService, SecureTokenService secureTokenService) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.secureTokenService = secureTokenService;
    }

    @Override
    @Transactional
    public LoginResponseDto register(UserDto request, String ip) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new LocalizedResponseStatusException(HttpStatus.BAD_REQUEST, "user.already.exist");
        }

        String hash = UUID.randomUUID().toString();
        User user = modelMapper.map(request, User.class);
        encodePassword(request, user);
        user.setPasswordExpiredDate(LocalDateTime.now().plus( passwordExpirationTime, ChronoUnit.SECONDS));
        user.setHash(hash);

        User savedUser = userRepository.save(user);


        log.info("User: {} successfully registered", user);
        UserDto userDto = modelMapper.map(savedUser, UserDto.class);

        sendRegistrationConfirmationEmail(user.getId(), user.getEmail());

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtTokenProvider.createToken(request.getEmail(), hash, ip, authenticate.getAuthorities());

        return new LoginResponseDto(userDto, token, null);

    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequest, HttpServletRequest httpServletRequest) {
        String username = loginRequest.getEmail();
        String ip = UserUtil.getClientIP(httpServletRequest);
        User user = userRepository.findByEmail(username).orElseThrow(() -> new LocalizedResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid.credentials"));
        try {
            user.setAuthenticated(false);
            String hash = UUID.randomUUID().toString();
            user.setHash(hash);
            User updatedUser = userRepository.save(user);


            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authenticate);

            String token = jwtTokenProvider.createToken(username, hash, ip, authenticate.getAuthorities());
            UserDto userDto = modelMapper.map(updatedUser, UserDto.class);
            if (!user.isEmailVerified()) resendEmailConfirmationEmail(new AuthenticatedUserDetails(userDto));
            log.info("Success login from email {} from ip {} at time {}", username, ip, LocalDateTime.now());
            return new LoginResponseDto(userDto, token, null);
        } catch (AuthenticationException e) {
            log.error("Attempt to invalid login for email {} from ip {} at time {}", username, ip, LocalDateTime.now());
            throw new LocalizedResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid.credentials");
        }
    }

    @Override
    public void verifyUserEmail(String email, String token) throws LocalizedResponseStatusException {
        User user = getOrThrowNotFound(email);

        SecureToken secureToken = secureTokenService.findByToken(token);
        if (Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired() || !user.getId().equals(secureToken.getUserId())) {
            throw new LocalizedResponseStatusException(HttpStatus.BAD_REQUEST, "invalid.email.token");
        }
        
        log.info("Success verified email {} at time {}", email, LocalDateTime.now());
        userRepository.save(user);
        secureTokenService.removeToken(secureToken);
    }

    @Override
    public LoginResponseDto requestPasswordReset(String email, String ip) {
        User user = getOrThrowNotFound(email);
        SecureToken secureToken = secureTokenService.createSecureToken(user.getId());
        secureTokenService.saveSecureToken(secureToken);

        PasswordResetEmailVerificationContext emailContext = new PasswordResetEmailVerificationContext();
        emailContext.init(user);
        emailContext.setToken(secureToken.getToken());
        emailService.sendMail(emailContext);


        String hash = UUID.randomUUID().toString();
        user.setHash(hash);

        User updateUser = userRepository.save(user);
        UserDto userDto = modelMapper.map(updateUser, UserDto.class);
        String token = jwtTokenProvider.createToken(email, hash, ip, userDto.getAuthorities());
        log.info("Success request for changing password  for email {} at time {}", email, LocalDateTime.now());
        return new LoginResponseDto(userDto, token, null);
    }

    @Override
    @Transactional
    public void updatePassword(String email, String newPassword) {
        User user = getOrThrowNotFound(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordExpiredDate(LocalDateTime.now().plus(passwordExpirationTime, ChronoUnit.SECONDS));
        log.info("Password for email {} was updated at time {}", email, LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void logout(String email) {
        final Cookie authCookie = new Cookie("AUTH", "");
        authCookie.setMaxAge(0);
        authCookie.setPath("/");

        User user = getOrThrowNotFound(email);
        user.setAuthenticated(false);
        user.setHash(null);
        userRepository.save(user);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(null);
        log.info("User with email {} was success logout at {}", email, LocalDateTime.now());
    }

    private void sendRegistrationConfirmationEmail(Long userId, String email) {
        SecureToken secureToken = secureTokenService.createSecureToken(userId);
        AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
        emailContext.init(email);
        emailContext.setToken(secureToken.getToken());
        emailService.sendMail(emailContext);
        log.info("Verification code was sent to user {} at {}", email, LocalDateTime.now());
    }

    private void sendAccountRecoveryEmail(String email, List<String> passPhrases) {
        AccountRecoveryEmailContext emailContext = new AccountRecoveryEmailContext();
        emailContext.init(email);
        emailContext.setPassPhrases(passPhrases);
        emailService.sendMail(emailContext);
        log.info("Verification code was sent to user {}", email);
    }

    private void sendSuccessLoginEmail(User user, String ip) {
        SuccessLoginEmailContext successLoginEmailContext = new SuccessLoginEmailContext();
        successLoginEmailContext.init(user);
        successLoginEmailContext.setIp(ip);
        successLoginEmailContext.setDate(new Date());
        emailService.sendMail(successLoginEmailContext);
        log.info("Success login message was sent to user {} at {}", user.getEmail(), LocalDateTime.now());
    }

    private void encodePassword(UserDto source, User target) {
        target.setPassword(passwordEncoder.encode(source.getPassword()));
    }


    private User getOrThrowNotFound(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new LocalizedResponseStatusException(HttpStatus.NOT_FOUND, "user.not.found"));
    }

    @Override
    public void resendEmailConfirmationEmail(AuthenticatedUserDetails authenticatedUserDetails) {
        SecureToken secureToken = secureTokenService.findByUserId(authenticatedUserDetails.getUserId());
        if (Objects.nonNull(secureToken)) secureTokenService.removeToken(secureToken);
        sendRegistrationConfirmationEmail(authenticatedUserDetails.getUserId(), authenticatedUserDetails.getEmail());
    }
    

    @Override
    public LoginResponseDto refreshAccessToken(String accessToken, String refreshToken, String ip) {

        String username = jwtTokenProvider.getUsername(refreshToken);
        Claims accessTokenClaims = jwtTokenProvider.parseExpiredAccessToken(accessToken);
        if (!accessTokenClaims.getSubject().equals(username)) {
            throw new LocalizedResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid.jwt.token");
        }
        User user = getOrThrowNotFound(username);
        if (Objects.isNull(user.getHash()) || !jwtTokenProvider.getHash(accessTokenClaims).equals(user.getHash())) {
            throw new LocalizedResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid.jwt.token");
        }

        if (!jwtTokenProvider.getHash(accessTokenClaims).equals(jwtTokenProvider.getHash(refreshToken))) {
            throw new LocalizedResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid.refresh.token");
        }
        

        String hash = UUID.randomUUID().toString();
        user.setHash(hash);
        userRepository.save(user);
        String updatedAccessToken = jwtTokenProvider.refreshToken(accessTokenClaims, refreshToken, hash, ip);
        String updatedRefreshToken = jwtTokenProvider.generateRefreshToken(username, hash, ip);
        log.info("Access token was refreshed to user  {} at {}", user.getEmail(), LocalDateTime.now());
        return new LoginResponseDto(modelMapper.map(user, UserDto.class), updatedAccessToken, updatedRefreshToken);
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
