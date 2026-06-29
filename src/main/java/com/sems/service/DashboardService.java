package com.sems.service;

import com.sems.entity.Employee;
import com.sems.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final EmployeeRepository empRepo;

    public Map<String, Object> stats() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("totalEmployees", empRepo.count());
        m.put("activeEmployees", empRepo.countByStatus(Employee.Status.ACTIVE));
        m.put("inactiveEmployees", empRepo.countByStatus(Employee.Status.INACTIVE));

        List<Map<String,Object>> byDept = new ArrayList<>();
        for (Object[] row : empRepo.countByDepartment()) {
            Map<String,Object> r = new LinkedHashMap<>();
            r.put("department", row[0]);
            r.put("count", row[1]);
            byDept.add(r);
        }
        m.put("byDepartment", byDept);
        return m;
    }
}
