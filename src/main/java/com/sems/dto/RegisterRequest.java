package com.sems.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank private String username;
    @Email @NotBlank private String email;
    @NotBlank @Size(min=6) private String password;
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    private String role; // ADMIN or EMPLOYEE (admin endpoint)
}
