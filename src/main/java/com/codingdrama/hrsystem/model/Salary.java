package com.codingdrama.hrsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@Entity
@Table(name = "salaries")
public class Salary extends BaseEntity {

    @Column(nullable = false)
    private double baseSalary;

    @Column
    private double bonuses;

    @Column
    private double allowances;

    @Column
    private double deductions;

    @Column
    private double overtimePay;

    @Column
    private double commission;

    @Column
    private String benefits;

    @Column(nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SalaryFrequency frequency;
    
    private LocalDateTime salaryDay;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
