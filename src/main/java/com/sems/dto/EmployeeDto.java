package com.sems.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmployeeDto {
    private Long id;
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    @Email @NotBlank private String email;
    private String phone;
    private String jobTitle;
    private BigDecimal salary;
    private String status;
    private LocalDate hireDate;
    private Long departmentId;
    private String departmentName;
    private String profilePicture;
}
