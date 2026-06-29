package com.sems.config;

import com.sems.entity.*;
import com.sems.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepo;
    private final EmployeeRepository empRepo;
    private final DepartmentRepository deptRepo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (userRepo.count() > 0) return;

        var admin = userRepo.save(User.builder()
            .username("admin").email("admin@sems.local")
            .password(encoder.encode("Admin@123"))
            .role(Role.ADMIN).enabled(true).build());

        var emp = userRepo.save(User.builder()
            .username("employee").email("employee@sems.local")
            .password(encoder.encode("Employee@123"))
            .role(Role.EMPLOYEE).enabled(true).build());

        var eng  = deptRepo.save(Department.builder().name("Engineering").description("Builds the product").build());
        var hr   = deptRepo.save(Department.builder().name("Human Resources").description("People operations").build());
        deptRepo.save(Department.builder().name("Sales").description("Revenue").build());

        empRepo.save(Employee.builder()
            .user(admin).firstName("System").lastName("Admin")
            .email("admin@sems.local").jobTitle("Administrator")
            .status(Employee.Status.ACTIVE).hireDate(LocalDate.now())
            .department(hr).build());

        empRepo.save(Employee.builder()
            .user(emp).firstName("Jane").lastName("Doe")
            .email("employee@sems.local").jobTitle("Software Engineer")
            .status(Employee.Status.ACTIVE).hireDate(LocalDate.now())
            .department(eng).build());
    }
}
