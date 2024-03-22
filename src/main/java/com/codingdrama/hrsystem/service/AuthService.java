package com.codingdrama.hrsystem.service;

import com.codingdrama.hrsystem.exceptions.LocalizedResponseStatusException;
import com.codingdrama.hrsystem.security.AuthenticatedUserDetails;
import com.codingdrama.hrsystem.service.dto.LoginRequestDto;
import com.codingdrama.hrsystem.service.dto.LoginResponseDto;
import com.codingdrama.hrsystem.service.dto.MfaTokenData;
import com.codingdrama.hrsystem.service.dto.UserDto;
import dev.samstevens.totp.exceptions.QrGenerationException;
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
     * Initiates the process of setting up MFA for a user and returns the MfaTokenData object with QR code data.
     * @param email the email address of the user requesting MFA setup
     * @return an MfaTokenData object with QR code data
     * @throws QrGenerationException if an error occurs while generating the QR code
     */
    MfaTokenData mfaSetup(final String email) throws QrGenerationException;

    /**
     * Verifies the user's MFA token and returns a LoginResponseDto object with the user's information and an authentication token.
     * @param email the email address of the user verifying their MFA token
     * @param token the MFA token to verify
     * @param ip the IP address of the user making the request
     * @return a LoginResponseDto object containing user information and an authentication token
     */
    LoginResponseDto verifyUserMfa(String email, String token, String ip);

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
     * Put off user's password updating to 90 days.
     * @param email the email address of the user whose password is being updated
     */
    void updatePasswordLatter(String email);

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

