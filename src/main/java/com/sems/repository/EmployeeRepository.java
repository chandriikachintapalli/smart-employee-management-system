package com.sems.repository;

import com.sems.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
        SELECT e FROM Employee e
        WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :q, '%'))
           OR LOWER(e.lastName)  LIKE LOWER(CONCAT('%', :q, '%'))
           OR LOWER(e.email)     LIKE LOWER(CONCAT('%', :q, '%'))
           OR LOWER(e.jobTitle)  LIKE LOWER(CONCAT('%', :q, '%'))
    """)
    Page<Employee> search(String q, Pageable pageable);

    long countByStatus(Employee.Status status);

    Optional<Employee> findByUserId(Long userId);
    Optional<Employee> findByEmailIgnoreCase(String email);

    @Query("SELECT e.department.name, COUNT(e) FROM Employee e WHERE e.department IS NOT NULL GROUP BY e.department.name")
    java.util.List<Object[]> countByDepartment();
}
