package com.codingdrama.hrsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@Entity
@Table(name = "clients")
public class Client extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contactPersonName;

    @Column(nullable = false)
    private String contactPersonEmail;

    @Column(nullable = false)
    private String contactPersonPhone;

    @Column
    private String description;

    @OneToMany
    private List<Project> projects;
}
