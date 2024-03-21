package com.codingdrama.hrsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Entity
@Table( name = "users")
public class User extends BaseEntity {
    
    @Column(nullable = false)
    private String firstName;
    
    @Column
    private String middleName;
    
    @Column(nullable = false)
    private String lastName;
    

    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(unique = true, nullable = false)
    private String phoneNumber;
    
    @Column(unique = true, nullable = false)
    private String skype;
    @Column(unique = true, nullable = false)
    private String linkedin;
    @Column(unique = true, nullable = false)
    private String telegram;
    
    
    @OneToOne
    private Address address;
    
    @Column
    private String password;

    @Column
    private LocalDateTime passwordExpiredDate;

    @Column
    private boolean emailVerified;
    @Column
    private boolean mfaEnabled;
    @Column
    private boolean authenticated;
    
    @Column
    private String secret;

    @Column
    private String hash;
    
    @Column
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private  Company company;
    
    @OneToMany
    private List<Project> projects;
    
    @ManyToMany
    private List<Technology> technologies;
    
    @OneToMany
    private List<Salary> salaries;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;
}
