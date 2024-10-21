package com.codingdrama.hrsystem.controller;

import com.codingdrama.hrsystem.exceptions.LocalizedResponse;
import com.codingdrama.hrsystem.exceptions.LocalizedResponseStatusException;
import com.codingdrama.hrsystem.security.AuthenticatedUserDetails;
import com.codingdrama.hrsystem.service.AuthService;
import com.codingdrama.hrsystem.service.dto.LoginRequestDto;
import com.codingdrama.hrsystem.service.dto.LoginResponseDto;
import com.codingdrama.hrsystem.service.dto.MessageResponse;
import com.codingdrama.hrsystem.service.dto.RefreshTokenRequest;
import com.codingdrama.hrsystem.service.dto.ResetPasswordRequest;
import com.codingdrama.hrsystem.service.dto.UpdatePasswordRequest;
import com.codingdrama.hrsystem.service.dto.UserDto;
import com.codingdrama.hrsystem.util.UserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Sign up a new user")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "400", description = "Possible reasons: \n * user.already.exist : User is already exist \n * email.invalid : Invalid email",content ={ @Content(mediaType = "application/json",
            schema = @Schema(implementation = LocalizedResponse.class)) })})
    @PostMapping("/signup")
    public ResponseEntity<LoginResponseDto> userRegistration(@Validated @RequestBody UserDto request, HttpServletRequest httpServletRequest) {
        LoginResponseDto loginResponseDto = authService.register(request, UserUtil.getClientIP(httpServletRequest));
        return ResponseEntity.ok().body(loginResponseDto);
    }

    @Operation(summary = "Login user by email and password")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "401", description = "Possible reasons: \n * invalid.credentials : Invalid email or password \n * captcha.max.attempts : User exceeded maximum number of failed attempts \n * captcha.invalid : ReCaptcha was not successfully validated",content ={ @Content(mediaType = "application/json",
            schema = @Schema(implementation = LocalizedResponse.class)) })})
    @Parameter(in = ParameterIn.HEADER, name = "g-recaptcha-response", schema = @Schema(type = "string"))
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest, HttpServletRequest request) {
        LoginResponseDto loginResponseDto = authService.login(loginRequest, request);
        return ResponseEntity.ok().body(loginResponseDto);
    }
    

    @Operation(summary = "Resend email code request")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "401", description = "Possible reasons: \n * invalid.jwt.token: Invalid authorization token ",content ={ @Content(mediaType = "application/json",
            schema = @Schema(implementation = LocalizedResponse.class)) }), @ApiResponse(responseCode = "404", description = "Possible reasons: \n * user.not.found: User not found",content ={ @Content(mediaType = "application/json", schema = @Schema(implementation = LocalizedResponse.class)) })})
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/resend-email-code")
    @PreAuthorize("hasAuthority('VERIFY_EMAIL')")
    public ResponseEntity<MessageResponse> resendEmailCode() {
        AuthenticatedUserDetails authenticatedUserDetails = UserUtil.getLoggedInUser();
        authService.resendEmailConfirmationEmail(authenticatedUserDetails);
        return ResponseEntity.ok(new MessageResponse("Email code was send"));
    }

    @Operation(summary = "Verify email code request")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "400", description = "Possible reasons: \n * email.is.already.confirmed : Email already confirmed \n * invalid.email.token : Token is not valid",content ={ @Content(mediaType = "application/json",
            schema = @Schema(implementation = LocalizedResponse.class))}),  @ApiResponse(responseCode = "401", description = "Possible reasons: \n * invalid.jwt.token: Invalid authorization token",content ={ @Content(mediaType = "application/json",
            schema = @Schema(implementation = LocalizedResponse.class)) }), @ApiResponse(responseCode = "404", description = "Possible reasons: \n * user.not.found: User not found", content ={ @Content(mediaType = "application/json",
            schema = @Schema(implementation = LocalizedResponse.class)) })})
    @SecurityRequirement(name = "Bearer Authentication")
    @Parameter(in = ParameterIn.HEADER, name = "X-Email-Confirmation-Token", schema = @Schema(type = "string"))
    @PostMapping("/verify-email-code")
    @PreAuthorize("hasAuthority('VERIFY_EMAIL')")
    public ResponseEntity<MessageResponse> verifyEmailCode(HttpServletRequest httpServletRequest) {
        AuthenticatedUserDetails authenticatedUserDetails = UserUtil.getLoggedInUser();
        authService.verifyUserEmail(authenticatedUserDetails.getEmail(), UserUtil.getEmailConfirmationToken(httpServletRequest));
        return ResponseEntity.ok(new MessageResponse("Email success verified"));
    }

    @Operation(summary = "Reset user password")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404", description = "Possible reasons: \n * user.not.found: User not found", content ={ @Content(mediaType = "application/json",
            schema = @Schema(implementation = LocalizedResponse.class)) })})
    @PostMapping("/reset-password")
    public ResponseEntity<LoginResponseDto> resetPassword(@RequestBody ResetPasswordRequest resetPasswordDTO, HttpServletRequest request) {
        LoginResponseDto loginResponseDto = authService.requestPasswordReset(resetPasswordDTO.getEmail(), UserUtil.getClientIP(request));
        return ResponseEntity.ok(loginResponseDto);
    }

    @Operation(summary = "Update user password")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "400", description = "Possible reasons:  \n * email.not.confirmed : Email not confirmed \n * mfa.not.confirmed : Mfa not confirmed \n * previously.used.password: User can't use previous 5 password",content ={ @Content(mediaType = "application/json",
            schema = @Schema(implementation = LocalizedResponse.class))}),  @ApiResponse(responseCode = "401", description = "Possible reasons: \n * invalid.jwt.token: Invalid authorization token",content ={ @Content(mediaType = "application/json",
            schema = @Schema(implementation = LocalizedResponse.class)) }), @ApiResponse(responseCode = "404", description = "Possible reasons: \n * user.not.found: User not found", content ={ @Content(mediaType = "application/json",
            schema = @Schema(implementation = LocalizedResponse.class)) })})
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/update-password")
    @PreAuthorize("hasAuthority('UPDATE_PASSWORD')")
    public ResponseEntity<MessageResponse> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        AuthenticatedUserDetails loggedInUser = UserUtil.getLoggedInUser();
        authService.updatePassword(loggedInUser.getEmail(), updatePasswordRequest.getPassword());
//        authService.logout(loggedInUser.getEmail());
        return ResponseEntity.ok(new MessageResponse("Success"));
    }
    

    @ApiResponses(value = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "401", description = "Possible reasons: \n * invalid.jwt.token: Invalid authorization token",content ={ @Content(mediaType = "application/json",
            schema = @Schema(implementation = LocalizedResponse.class))}), @ApiResponse(responseCode = "404", description = "Possible reasons: \n * user.not.found: User not found", content ={ @Content(mediaType = "application/json",
            schema = @Schema(implementation = LocalizedResponse.class)) })})
    @Operation(summary = "Logout current user")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout() {
        AuthenticatedUserDetails userDetails = UserUtil.getLoggedInUser();
        authService.logout(userDetails.getEmail());
        return ResponseEntity.ok(new MessageResponse("logout successful"));
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "400", description = "Possible reasons: \n * invalid.refresh.token: Invalid refresh token",content ={ @Content(mediaType = "application/json",
            schema = @Schema(implementation = LocalizedResponse.class))}), @ApiResponse(responseCode ="401", description = "Possible reasons:  \n * invalid.jwt.token: Invalid authorization token \n * jwt.not.present: Access Token not present",content ={ @Content(mediaType = "application/json",
            schema = @Schema(implementation = LocalizedResponse.class))}), @ApiResponse(responseCode = "404", description = "Possible reasons: \n * user.not.found: User not found", content ={ @Content(mediaType = "application/json",
            schema = @Schema(implementation = LocalizedResponse.class)) })})
    @Operation(summary = "Refresh access token")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest, HttpServletRequest httpServletRequest) {
        String accessToken = UserUtil.getAuthorizationToken(httpServletRequest);
        if (accessToken == null || accessToken.isEmpty()) {
            throw new LocalizedResponseStatusException(HttpStatus.UNAUTHORIZED, "jwt.not.present");
        }
        return ResponseEntity.ok(authService.refreshAccessToken(accessToken, refreshTokenRequest.getRefreshToken(), UserUtil.getClientIP(httpServletRequest)));
    }
}
