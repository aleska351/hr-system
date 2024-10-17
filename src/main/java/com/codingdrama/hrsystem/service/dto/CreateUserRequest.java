package com.codingdrama.hrsystem.service.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private List<String> roles;
    private Long companyId;
    private Long departmentId;
}
