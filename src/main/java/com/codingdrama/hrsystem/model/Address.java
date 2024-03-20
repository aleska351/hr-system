package com.codingdrama.hrsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Entity
@Table( name = "addresses")
public class Address extends BaseEntity{
    private String country;
    private String city;
    private String address;
    private String zip;
}
