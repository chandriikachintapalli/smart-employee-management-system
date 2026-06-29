package com.sems.controller;

import com.sems.dto.LeaveDto;
import com.sems.service.LeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {
    private final LeaveService svc;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<LeaveDto> all() { return svc.all(); }

    @GetMapping("/employee/{employeeId}")
    public List<LeaveDto> byEmployee(@PathVariable Long employeeId) { return svc.byEmployee(employeeId); }

    @PostMapping("/employee/{employeeId}")
    public LeaveDto apply(@PathVariable Long employeeId, @Valid @RequestBody LeaveDto dto) {
        return svc.apply(employeeId, dto);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public LeaveDto setStatus(@PathVariable Long id, @RequestParam String status) {
        return svc.setStatus(id, status);
    }
}
