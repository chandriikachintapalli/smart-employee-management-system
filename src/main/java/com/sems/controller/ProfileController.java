package com.sems.controller;

import com.sems.dto.*;
import com.sems.entity.Employee;
import com.sems.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService svc;
    private final EmployeeService employeeService;

    @GetMapping
    public EmployeeDto me(@AuthenticationPrincipal UserDetails u) {
        return employeeService.toDto(svc.currentProfile(u.getUsername()));
    }

    @PutMapping
    public EmployeeDto update(@AuthenticationPrincipal UserDetails u, @RequestBody Employee patch) {
        return employeeService.toDto(svc.updateProfile(u.getUsername(), patch));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
        @AuthenticationPrincipal UserDetails u,
        @Valid @RequestBody ChangePasswordRequest req) {
        svc.changePassword(u.getUsername(), req);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/picture")
    public Map<String,String> upload(
        @AuthenticationPrincipal UserDetails u,
        @RequestParam("file") MultipartFile file) {
        return Map.of("url", svc.uploadPicture(u.getUsername(), file));
    }
}
