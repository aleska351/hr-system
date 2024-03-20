package com.codingdrama.hrsystem.service.dto;

import com.codingdrama.hrsystem.model.CompanyStatus;
import com.codingdrama.hrsystem.model.CompanyType;
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
public class CompanyDto {

    private Long id;
    private String name;
    private String companyCode;
    private String title;
    private CompanyType companyType;
    private String website;
    private String phoneNumber;
    private String email;
    private AddressDto address;
    private CompanyStatus companyStatus;
}
