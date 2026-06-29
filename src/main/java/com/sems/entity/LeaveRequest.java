package com.sems.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Table(name="leaves")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LeaveRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional=false)
    @JoinColumn(name="employee_id", nullable=false)
    private Employee employee;

    @Column(name="leave_type", nullable=false, length=40)
    private String leaveType;

    @Column(name="start_date", nullable=false)
    private LocalDate startDate;

    @Column(name="end_date", nullable=false)
    private LocalDate endDate;

    @Column(length=500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private Status status = Status.PENDING;

    @Column(name="applied_at")
    private LocalDateTime appliedAt = LocalDateTime.now();

    public enum Status { PENDING, APPROVED, REJECTED }
}
