package com.codingdrama.hrsystem.util;

import com.codingdrama.hrsystem.exceptions.LocalizedResponseStatusException;
import com.codingdrama.hrsystem.security.AuthenticatedUserDetails;
import com.codingdrama.hrsystem.security.jwt.JwtAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Objects;

public class UserUtil {

    /**
     * Returns the client IP address from the HttpServletRequest.
     * @param request The HttpServletRequest object.
     * @return The client IP address.
     */
    public static String getClientIP(HttpServletRequest request) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    /**
     * Returns the reCAPTCHA token from the HttpServletRequest.
     * @param request The HttpServletRequest object.
     * @return The reCAPTCHA token.
     * @throws LocalizedResponseStatusException if the token is not provided.
     */
    public static String getRecaptcha(HttpServletRequest request) {
        final String xfHeader = request.getHeader("g-recaptcha-response");
        return xfHeader;
    }

    /**
     * Returns authorization token from the HttpServletRequest.
     * @param request The HttpServletRequest object.
     * @return The authorization token or null if not provided.
     * @throws LocalizedResponseStatusException if the token is not provided.
     */
    public static String getAuthorizationToken(HttpServletRequest request) {
        final String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        return Objects.nonNull(accessToken) ? accessToken.startsWith("Bearer ") ?  accessToken.substring(7) : accessToken : null;
    }


    /**
     * Returns the two-factor authentication token from the HttpServletRequest.
     * @param request The HttpServletRequest object.
     * @return The two-factor authentication token.
     * @throws LocalizedResponseStatusException if the token is not provided.
     */
    public static String getTwoFactorToken(HttpServletRequest request) {
        final String xTwoFactorHeader = request.getHeader("X-TwoFactorToken");
        if (xTwoFactorHeader == null || xTwoFactorHeader.isEmpty()) {
            throw new LocalizedResponseStatusException(HttpStatus.UNAUTHORIZED, "mfa.not.provided");
        }
        return xTwoFactorHeader;
    }

    /**
     * Returns the email confirmation token from the HttpServletRequest.
     * @param request The HttpServletRequest object.
     * @return The email confirmation token.
     * @throws LocalizedResponseStatusException if the token is not provided.
     */
    public static String getEmailConfirmationToken(HttpServletRequest request) {
        final String confirmationEmailToken = request.getHeader("X-Email-Confirmation-Token");
        if (confirmationEmailToken == null || confirmationEmailToken.isEmpty()) {
            throw new LocalizedResponseStatusException(HttpStatus.UNAUTHORIZED, "email.token.not.provided");
        }
        return confirmationEmailToken;
    }

    /**
     * Returns an array of null property names from the input object.
     * @param object The object to check for null properties.
     * @return An array of null property names.
     */
    public static String[] getNullPropertyNames(Object object) {
        BeanWrapper src = new BeanWrapperImpl(object);
        return Arrays.stream(src.getPropertyDescriptors())
                       .map(PropertyDescriptor::getName)
                       .filter(name -> src.getPropertyValue(name) == null)
                       .toArray(String[]::new);
    }

    public static AuthenticatedUserDetails getLoggedInUser() {
        SecurityContext context = SecurityContextHolder.getContext();

        if (context == null) {
            throw new LocalizedResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthenticated.request");
        }
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            throw new LocalizedResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthenticated.request");
        }
        if(authentication instanceof JwtAuthentication){
            return ((JwtAuthentication) authentication).getAuthenticatedUserDetails();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof AuthenticatedUserDetails) {
            return (AuthenticatedUserDetails) principal;
        }


        throw new LocalizedResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthenticated.request");
    }
}