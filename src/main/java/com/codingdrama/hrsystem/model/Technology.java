package com.codingdrama.hrsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
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
@Table(name = "technologies")
public class Technology extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column
    private String description;
    
    @Column(nullable = false)
    private String category;
    
    private boolean defaultTechnology;
    
    @OneToOne
    private Company company;
    
    @ManyToMany
    private List<User> users;
}
