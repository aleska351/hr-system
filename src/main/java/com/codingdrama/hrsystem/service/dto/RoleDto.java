package com.codingdrama.hrsystem.service.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class RoleDto {

    private Long id;
    private String name;
    private List<Long> userIds;
    private List<Long> permissionIds;
}
