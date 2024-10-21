package com.codingdrama.hrsystem.service;

import com.codingdrama.hrsystem.exceptions.LocalizedResponseStatusException;
import com.codingdrama.hrsystem.security.AuthenticatedUserDetails;
import com.codingdrama.hrsystem.service.dto.LoginRequestDto;
import com.codingdrama.hrsystem.service.dto.LoginResponseDto;
import com.codingdrama.hrsystem.service.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;

/**
 An interface representing a service for managing user credentials. Login registration logout etc.
 */
public interface AuthService {

    /**
     * Registers a new user and returns a LoginResponseDto object with the user's information and an authentication token.
     * @param request the sign-up request containing user information
     * @param ip the IP address of the user making the request
     * @return a LoginResponseDto object containing user information and an authentication token
     */
    LoginResponseDto register(UserDto request, String ip);

    /**
     * Logs in a user and returns a LoginResponseDto object with the user's information and an authentication token.
     * @param loginRequest the login request containing user credentials
     * @param httpServletRequest the servlet request object
     * @return a LoginResponseDto object containing user information and an authentication token
     */
    LoginResponseDto login(LoginRequestDto loginRequest, HttpServletRequest httpServletRequest);

    /**
     * Verifies a user's email address using a confirmation token.
     * @param email the email address of the user to verify
     * @param token the confirmation token
     */
    void verifyUserEmail(final String email, final String token);

    /**
     * Sends a password reset email to a user.
     * @param email the email address of the user requesting a password reset
     * @param ip the IP address of the user making the request
     * @return a LoginResponseDto object containing user information and an authentication token
     */
    LoginResponseDto requestPasswordReset(String email, String ip);
    

    /**
     * Updates a user's password.
     * @param email the email address of the user whose password is being updated
     * @param password the new password
     */
    void updatePassword(String email, String password);
    

    /**
     * Logs out a user.
     * @param email the email address of the user to log out
     */
    void logout(String email);

    /**
     * Resend email code  to a user.
     * @param user current user
     */
    void resendEmailConfirmationEmail(AuthenticatedUserDetails user);




    /**
     * Refreshes an access token using a refresh token.
     *
     * @param accessToken  the current access token
     * @param refreshToken the refresh token used to obtain a new access token
     * @param ip the IP address of the user making the request
     * @return a LoginResponseDto object containing the new access token and its expiration time
     * @throws LocalizedResponseStatusException if the current access token or refresh token is invalid
     */
    LoginResponseDto refreshAccessToken(String accessToken, String refreshToken, String ip);
}

