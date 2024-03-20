package com.codingdrama.hrsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "companies")
public class Company extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String companyCode;


    @Column(nullable = false)
    private String title;

    @Column
    @Enumerated(EnumType.STRING)
    private CompanyType companyType;

    @Column(nullable = false)
    private String website;
    
    @Column(nullable = false)
    private String phoneNumber;
    
    @Column(nullable = false)
    private String adminEmail;

    @OneToOne
    private Address address;
    
    @Column
    @Enumerated(EnumType.STRING)
    private CompanyStatus companyStatus;
    
    @OneToMany(mappedBy = "company")
    private List<User> users;
    
    @OneToMany(mappedBy = "company")
    private List<Department> departments;
}
