package com.codingdrama.hrsystem.config;

import com.codingdrama.hrsystem.security.DataSourceAuthenticationProvider;
import com.codingdrama.hrsystem.security.SecureAuthenticationEntryPoint;
import com.codingdrama.hrsystem.security.jwt.JwtSecurityConfigurer;
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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
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
        http.httpBasic().disable()
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(secureAuthenticationEntryPoint)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(LOGIN_ENDPOINT).permitAll()
                .requestMatchers(REFRESH_TOKEN_ENDPOINT).permitAll()
                .requestMatchers(SIGNUP_ENDPOINT).permitAll()
                .requestMatchers(RESET_PASSWORD_ENDPOINT).permitAll()
                .requestMatchers(HttpMethod.GET, "/actuator/health").permitAll()
                .requestMatchers("/configuration/ui",
                        "/swagger-resources/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtSecurityConfigurer(jwtTokenFilter));
        http.headers()
                .xssProtection()
                .and()
                .contentSecurityPolicy("script-src 'self'");

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("*").allowedOriginPatterns(allowedOriginPatterns);
            }
        };
    }
}
