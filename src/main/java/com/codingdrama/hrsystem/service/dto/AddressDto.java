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
public class AddressDto{
    private Long id;
    private String country;
    private String city;
    private String address;
    private String zip;
}
