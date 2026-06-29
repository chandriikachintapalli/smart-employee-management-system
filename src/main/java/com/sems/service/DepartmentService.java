package com.sems.service;

import com.sems.dto.DepartmentDto;
import com.sems.entity.Department;
import com.sems.exception.ResourceNotFoundException;
import com.sems.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository repo;

    public List<DepartmentDto> all() {
        return repo.findAll().stream().map(this::toDto).toList();
    }

    public DepartmentDto get(Long id) {
        return toDto(find(id));
    }

    public DepartmentDto create(DepartmentDto dto) {
        if (repo.existsByNameIgnoreCase(dto.getName()))
            throw new IllegalArgumentException("Department name already exists");
        var d = repo.save(Department.builder().name(dto.getName()).description(dto.getDescription()).build());
        return toDto(d);
    }

    public DepartmentDto update(Long id, DepartmentDto dto) {
        var d = find(id);
        d.setName(dto.getName());
        d.setDescription(dto.getDescription());
        return toDto(repo.save(d));
    }

    public void delete(Long id) { repo.delete(find(id)); }

    private Department find(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department " + id + " not found"));
    }

    private DepartmentDto toDto(Department d) {
        var x = new DepartmentDto();
        x.setId(d.getId()); x.setName(d.getName()); x.setDescription(d.getDescription());
        return x;
    }
}
