package com.codingdrama.hrsystem.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Data Transfer Object (DTO) representing the response object for user login API.
 */
@Schema(description = "Login response for user login API")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {


    /**
     * The email address of the logged-in user.
     */
    @Schema(description = "User email")
    private String email;

    /**
     * The information about the last login of the user.
     */
    @Schema(description = "Last login info. Including date and ip")
    private LocalDateTime loginDate;

    /**
     * Indicates whether the user's password has expired or not.
     */
    @Schema(description = " Indicates whether the user's password has expired or not")
    private boolean passwordExpired;

    /**
     * Indicates whether the user's email has been verified or not.
     */
    @Schema(description = " Indicates whether the user's email has been verified or not")
    private boolean emailVerified;

    /**
     * Indicates whether the user has passed multi-factor authentication or not.
     */
    @Schema(description = " Indicates whether the user has passed multi-factor authentication or not")
    private boolean mfaEnabled;
    

    /**
     * The authentication token for the logged-in user.
     */
    @Schema(description = " JWT authentication token")
    private String token;

    /**
     * The authentication refresh token for access token.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = " Refresh token")
    private String refreshToken;

    public LoginResponseDto(UserDto userDto, String token, String refreshToken) {
        this.email = userDto.getEmail();
        this.loginDate = userDto.getLoginDate();
        this.emailVerified = userDto.isEmailVerified();
        this.mfaEnabled = userDto.isMfaEnabled();
        this.passwordExpired = userDto.getPasswordExpiredDate().isBefore(LocalDateTime.now());
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
