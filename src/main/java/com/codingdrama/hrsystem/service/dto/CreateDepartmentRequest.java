package com.codingdrama.hrsystem.service.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
public class CreateDepartmentRequest {
    private String name;
    private String email;
    private String title;
    private String description;
    private Long companyId;

}
