package com.democorp.employee.service.impl;

import com.democorp.employee.dto.EmployeeFilter;
import com.democorp.employee.entity.Employee;
import com.democorp.employee.repository.EmployeeRepository;
import com.democorp.employee.service.EmployeeService;
import com.democorp.employee.spec.EmployeeSpec;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;
    public EmployeeServiceImpl(EmployeeRepository repo){
        this.repository = repo;
    }

    @Override
    public Employee create(Employee employee){
        if(repository.existsByEmployeeCode(employee.getEmployeeCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Employee Code already exists.");
        }
        return repository.save(employee);
    }

    @Override
    public Employee getById(Long id){
        return repository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Employee Not Found."));
    }

    @Override
    public List<Employee> getAll(){
        return repository.findAll();
    }

    @Override
    public Employee update(Long id, Employee employee){
        Employee existing = repository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Employee Not Found."));
        BeanUtils.copyProperties(employee, existing, "id", "createdAt", "updatedAt", "version");
        return repository.save(existing);
    }

    @Override
    public void delete(Long id){
        if(!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found.");
        }
        repository.deleteById(id);
    }

    @Override
    public Page<Employee> search(EmployeeFilter filter, Pageable pageable) {
        return repository.findAll(EmployeeSpec.byFilter(filter), pageable);
    }
}