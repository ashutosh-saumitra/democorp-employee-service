package com.democorp.employee.controller;

import com.democorp.employee.dto.EmployeeFilter;
import com.democorp.employee.dto.EmployeeRequestDTO;
import com.democorp.employee.dto.EmployeeResponseDTO;
import com.democorp.employee.entity.Employee;
import com.democorp.employee.mapper.EmployeeMapper;
import com.democorp.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service){
        this.service = service;
    }

    /**
     * Create - API
     * Complex implementation using UriComponentBuilder
     * --Simpler version of create() is also here.
     * */
    /*
    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee, UriComponentsBuilder builder){
        Employee saved = service.create(employee);
        return ResponseEntity.created(builder.path("/api/v1/employees/{id}").buildAndExpand(saved.getId()).toUri()).body(saved);
    }*/

    /**
     * Create - API
     * Simpler version of create() is here.
     * */
    /*
    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee){
        Employee saved = service.create(employee);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }*/

    /**
     * Create - API
     * Modified to use the EmployeeRequestDTO and EmployeeResponseDTO
     * */
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> create(@Valid @RequestBody EmployeeRequestDTO requestDTO){
        Employee toSave = EmployeeMapper.toEntity(requestDTO);
        Employee saved = service.create(toSave);
        return new ResponseEntity<>(EmployeeMapper.toResponse(saved), HttpStatus.CREATED);
    }

    /**
     * Read by ID - API
     * Simpler Version
     * */
    /*
    @GetMapping("/{id}")
    public Employee getById(@PathVariable Long id){
        return service.getById(id);
    }*/

    /**
     * Read by ID - API
     * Modified to use the EmployeeRequestDTO and EmployeeResponseDTO
     * */
    @GetMapping("/{id}")
    public EmployeeResponseDTO getById(@PathVariable Long id){
        Employee employee = service.getById(id);
        return EmployeeMapper.toResponse(employee);
    }

    /**
     * Read ALL - API
     * Simpler Version
     * */
    /*
    @GetMapping
    public List<Employee> getAll(){
        return service.getAll();
    }*/

    /**
     * Read ALL - API
     * Modified to use the EmployeeRequestDTO and EmployeeResponseDTO
     * */
    @GetMapping
    public List<EmployeeResponseDTO> getAll(){
        return service.getAll().stream().map(EmployeeMapper::toResponse).collect(Collectors.toList());
    }

    /**
     * Update - API
     * Simpler Version
     * */
    /*
    @PutMapping("/{id}")
    public Employee update(@PathVariable Long id, @RequestBody Employee employee){
        return service.update(id, employee);
    }*/

    /**
     * Update - API
     * Modified to use the EmployeeRequestDTO and EmployeeResponseDTO
     * */
    @PutMapping("/{id}")
    public EmployeeResponseDTO update(@PathVariable Long id, @Valid @RequestBody EmployeeRequestDTO requestDTO){
        // Load existing entity, copy fields from DTO, then save
        Employee existing = service.getById(id);
        EmployeeMapper.copyToEntity(requestDTO, existing);
        Employee saved = service.update(id, existing);
        return EmployeeMapper.toResponse(saved);
    }

    /**
     * Delete - API
     * This is a cleaner version of Delete API, but it returns 200 OK as response.
     * As per REST API rules we must return a body when we are using 200 Response Code, a general practice.
     * */
    /*
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok("Employee Deleted Successfully.");
    }*/

    /**
     * Delete - API
     * Best Practice for REST DELETE Api is to use 204 NO_CONTENT. The use will understand the use of 204 if he is aware of REST best practices.
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        //return (ResponseEntity<Void>) ResponseEntity.status(HttpStatus.NO_CONTENT); //Does not give any response. It deleted the Employee successfully, but says 500 Internal Server Error.
        //return ResponseEntity.noContent().build(); //Does not give any response. It deleted the Employee successfully, but says 204 No Content.
        return ResponseEntity.noContent().header("X-Message", "Employee Deleted Successfully.").build(); //Does not give any response. It deleted the Employee successfully, and attached a message in the header but the body is still empty.
    }

    @GetMapping("/search")
    public Page<EmployeeResponseDTO> search(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String employmentType,
            @PageableDefault(size = 10, sort = "id") Pageable pageable)
    {
        EmployeeFilter filter = new EmployeeFilter();
        filter.setDepartment(department);
        filter.setStatus(status);
        filter.setEmploymentType(employmentType);

        return service.search(filter, pageable).map(EmployeeMapper::toResponse);
    }
}