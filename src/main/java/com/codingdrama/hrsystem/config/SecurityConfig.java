package com.codingdrama.hrsystem.config;

import com.codingdrama.hrsystem.security.DataSourceAuthenticationProvider;
import com.codingdrama.hrsystem.security.SecureAuthenticationEntryPoint;
import com.codingdrama.hrsystem.security.jwt.JwtTokenFilter;
import com.codingdrama.hrsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    public static final String LOGIN_ENDPOINT = "/api/v1/auth/login";
    public static final String REFRESH_TOKEN_ENDPOINT = "/api/v1/auth/refresh";
    public static final String SIGNUP_ENDPOINT = "/api/v1/auth/signup";
    public static final String RESET_PASSWORD_ENDPOINT = "/api/v1/auth/reset-password";
    
    @Value("${auth.allowed.origin.patterns}")
    private String allowedOriginPatterns;
    @Autowired
    private SecureAuthenticationEntryPoint secureAuthenticationEntryPoint;

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider dataSourceAuthenticationProvider(UserService userService, BCryptPasswordEncoder encoder) {
        return new DataSourceAuthenticationProvider(userService, encoder);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(List<AuthenticationProvider> authenticationProviders) throws Exception {
        return new ProviderManager(authenticationProviders);
    }

    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher
            (ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtTokenFilter jwtTokenFilter) throws Exception {
        http
            .httpBasic(AbstractHttpConfigurer::disable)  // Disable HTTP Basic authentication
            .csrf(AbstractHttpConfigurer::disable)       // Disable CSRF protection, necessary for stateless REST APIs
            .cors(AbstractHttpConfigurer::disable)       // Disable CORS if no custom configuration is required
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Use stateless sessions for REST APIs
            )
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(secureAuthenticationEntryPoint)  // Handle unauthorized access with a custom entry point
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(LOGIN_ENDPOINT).permitAll()             // Allow public access to login
                .requestMatchers(REFRESH_TOKEN_ENDPOINT).permitAll()     // Allow public access to token refresh
                .requestMatchers(SIGNUP_ENDPOINT).permitAll()            // Allow public access to signup
                .requestMatchers(RESET_PASSWORD_ENDPOINT).permitAll()    // Allow public access to password reset
                .requestMatchers(HttpMethod.GET, "/actuator/health").permitAll() // Permit access to health checks
                .requestMatchers("/configuration/ui",
                    "/swagger-resources/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**").permitAll()  // Allow public access to Swagger API docs
                .anyRequest().authenticated()  // Require authentication for all other endpoints
            )
            // Add JWT token filter before the username/password authentication filter
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // Configure security headers like content security policy (CSP)
        http.headers(headers -> headers
            .contentSecurityPolicy(csp -> csp
                .policyDirectives("script-src 'self'; object-src 'none'; style-src 'self'")  // Strict CSP rules to prevent XSS
            )
        );

        return http.build();  // Build the final SecurityFilterChain object
    }

    // Configure global CORS settings to allow cross-origin requests
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // Apply CORS rules to all endpoints
                    .allowedMethods("*")  // Allow all HTTP methods (GET, POST, etc.)
                    .allowedOriginPatterns(allowedOriginPatterns);  // Allow specified origins from application properties
            }
        };
    }
}
