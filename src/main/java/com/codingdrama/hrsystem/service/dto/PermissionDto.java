package com.codingdrama.hrsystem.service.dto;

import com.codingdrama.hrsystem.model.Permission;
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
public class PermissionDto {

    private Long id;
    private String name;
    private String description;
}
