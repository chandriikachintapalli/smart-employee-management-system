package com.sems.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class LeaveDto {
    private Long id;
    private Long employeeId;
    private String employeeName;
    @NotBlank private String leaveType;
    @NotNull private LocalDate startDate;
    @NotNull private LocalDate endDate;
    private String reason;
    private String status;
    private String appliedAt;
}
