package com.sems.service;

import com.sems.dto.LeaveDto;
import com.sems.entity.*;
import com.sems.exception.ResourceNotFoundException;
import com.sems.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveService {
    private final LeaveRepository repo;
    private final EmployeeRepository empRepo;

    public List<LeaveDto> all() {
        return repo.findAllByOrderByAppliedAtDesc().stream().map(this::toDto).toList();
    }

    public List<LeaveDto> byEmployee(Long employeeId) {
        return repo.findByEmployeeIdOrderByAppliedAtDesc(employeeId).stream().map(this::toDto).toList();
    }

    public LeaveDto apply(Long employeeId, LeaveDto dto) {
        Employee e = empRepo.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        if (dto.getEndDate().isBefore(dto.getStartDate()))
            throw new IllegalArgumentException("End date must be after start date");
        var l = LeaveRequest.builder()
            .employee(e).leaveType(dto.getLeaveType())
            .startDate(dto.getStartDate()).endDate(dto.getEndDate())
            .reason(dto.getReason()).status(LeaveRequest.Status.PENDING)
            .build();
        return toDto(repo.save(l));
    }

    public LeaveDto setStatus(Long id, String status) {
        var l = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Leave " + id + " not found"));
        l.setStatus(LeaveRequest.Status.valueOf(status.toUpperCase()));
        return toDto(repo.save(l));
    }

    private LeaveDto toDto(LeaveRequest l) {
        var d = new LeaveDto();
        d.setId(l.getId());
        d.setEmployeeId(l.getEmployee().getId());
        d.setEmployeeName(l.getEmployee().getFirstName() + " " + l.getEmployee().getLastName());
        d.setLeaveType(l.getLeaveType());
        d.setStartDate(l.getStartDate());
        d.setEndDate(l.getEndDate());
        d.setReason(l.getReason());
        d.setStatus(l.getStatus().name());
        d.setAppliedAt(l.getAppliedAt() != null ? l.getAppliedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null);
        return d;
    }
}
