package com.sems.service;

import com.sems.dto.EmployeeDto;
import com.sems.entity.*;
import com.sems.exception.ResourceNotFoundException;
import com.sems.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository repo;
    private final DepartmentRepository deptRepo;

    public Page<EmployeeDto> list(String q, int page, int size, String sortBy, String dir) {
        Sort sort = "desc".equalsIgnoreCase(dir) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pg = PageRequest.of(page, size, sort);
        Page<Employee> result = (q == null || q.isBlank()) ? repo.findAll(pg) : repo.search(q, pg);
        return result.map(this::toDto);
    }

    public EmployeeDto get(Long id) { return toDto(find(id)); }

    public EmployeeDto create(EmployeeDto dto) {
        var e = new Employee();
        apply(e, dto);
        return toDto(repo.save(e));
    }

    public EmployeeDto update(Long id, EmployeeDto dto) {
        var e = find(id);
        apply(e, dto);
        return toDto(repo.save(e));
    }

    public void delete(Long id) { repo.delete(find(id)); }

    private void apply(Employee e, EmployeeDto d) {
        e.setFirstName(d.getFirstName());
        e.setLastName(d.getLastName());
        e.setEmail(d.getEmail());
        e.setPhone(d.getPhone());
        e.setJobTitle(d.getJobTitle());
        e.setSalary(d.getSalary());
        e.setHireDate(d.getHireDate());
        if (d.getStatus() != null) e.setStatus(Employee.Status.valueOf(d.getStatus().toUpperCase()));
        if (d.getDepartmentId() != null) {
            e.setDepartment(deptRepo.findById(d.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found")));
        } else {
            e.setDepartment(null);
        }
    }

    public Employee find(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee " + id + " not found"));
    }

    public EmployeeDto toDto(Employee e) {
        var x = new EmployeeDto();
        x.setId(e.getId());
        x.setFirstName(e.getFirstName());
        x.setLastName(e.getLastName());
        x.setEmail(e.getEmail());
        x.setPhone(e.getPhone());
        x.setJobTitle(e.getJobTitle());
        x.setSalary(e.getSalary());
        x.setStatus(e.getStatus() != null ? e.getStatus().name() : null);
        x.setHireDate(e.getHireDate());
        x.setProfilePicture(e.getProfilePicture());
        if (e.getDepartment() != null) {
            x.setDepartmentId(e.getDepartment().getId());
            x.setDepartmentName(e.getDepartment().getName());
        }
        return x;
    }
}
