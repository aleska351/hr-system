package com.codingdrama.hrsystem.service.dto;

import com.codingdrama.hrsystem.model.UserStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<Long> projectIds;
    private List<Long> technologyIds;
    private List<Long> salaryIds;
    private List<Long> roleIds;
}
