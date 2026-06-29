package com.sems.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name="employees")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Employee {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", unique=true)
    private User user;

    @Column(name="first_name", nullable=false, length=80)
    private String firstName;

    @Column(name="last_name", nullable=false, length=80)
    private String lastName;

    @Column(nullable=false, unique=true, length=160)
    private String email;

    @Column(length=30)
    private String phone;

    @Column(name="job_title", length=120)
    private String jobTitle;

    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private Status status = Status.ACTIVE;

    @Column(name="hire_date")
    private LocalDate hireDate;

    @Column(name="profile_picture")
    private String profilePicture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="department_id")
    private Department department;

    public enum Status { ACTIVE, INACTIVE }
}
