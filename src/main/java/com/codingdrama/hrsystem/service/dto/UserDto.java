package com.codingdrama.hrsystem.service.dto;

import com.codingdrama.hrsystem.model.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String skype;
    private String linkedin;
    private String telegram;
    private AddressDto address;
    private String password;
    private LocalDateTime passwordExpiredDate;
    private boolean emailVerified;
    private UserStatus status;
    private Long departmentId;
    private Long companyId;
    
    private boolean mfaEnabled;
    private boolean authenticated;
    
    private String secret;
    private String hash;
    
    private LocalDateTime loginDate;
    
    private List<Long> projectIds;
    private List<Long> technologyIds;
    private List<Long> salaryIds;
    private Set<RoleDto> roles;
    
     public List<GrantedAuthority> getAuthorities() {
        return roles.stream()
                .flatMap(role ->
                        Stream.concat(
                                Stream.of(role.getName()),
                                role.getPermissions().stream().map(PermissionDto::getName)))
                .distinct()
                .toList().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
