package com.democorp.employee.service;

import com.democorp.employee.dto.EmployeeFilter;
import com.democorp.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    Employee create(Employee employee);
    Employee getById(Long id);
    List<Employee> getAll();
    Employee update(Long id, Employee employee);
    void delete(Long id);

    // NEW: paged listing with optional filters
    Page<Employee> search(EmployeeFilter filter, Pageable pageable);

}
