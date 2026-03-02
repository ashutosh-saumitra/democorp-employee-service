package com.democorp.employee.mapper;

import com.democorp.employee.dto.EmployeeRequestDTO;
import com.democorp.employee.dto.EmployeeResponseDTO;
import com.democorp.employee.entity.Employee;

import java.time.LocalDate;

public final class EmployeeMapper {

    private EmployeeMapper(){}


    //DTO -> Entity (for create/delete)
    public static Employee toEntity(EmployeeRequestDTO requestDTO){

        if(requestDTO == null)  return null;
        Employee emp = new Employee();
        emp.setEmployeeCode(requestDTO.getEmployeeCode());
        emp.setFirstName(requestDTO.getFirstName());
        emp.setLastName(requestDTO.getLastName());
        emp.setEmail(requestDTO.getEmail());
        emp.setPhone(requestDTO.getPhone());
        emp.setDepartment(requestDTO.getDepartment());
        emp.setJobTitle(requestDTO.getJobTitle());
        emp.setStatus(requestDTO.getStatus());
        emp.setEmploymentType(requestDTO.getEmploymentType());
        emp.setManagerId(requestDTO.getManagerId());
        emp.setLocation(requestDTO.getLocation());
        emp.setDateOfJoining(requestDTO.getDateOfJoining());
        emp.setDateOfExit(requestDTO.getDateOfExit());
        emp.setNotes(requestDTO.getNotes());
        // createdAt/updatedAt handled by @PrePersist/@PreUpdate in entity
        return emp;
    }

    //Entity -> DTO (for response)
    public static EmployeeResponseDTO toResponse(Employee employee){

        if(employee == null)    return null;

        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO();
        responseDTO.setId(employee.getId());
        responseDTO.setEmployeeCode(employee.getEmployeeCode());
        responseDTO.setFirstName(employee.getFirstName());
        responseDTO.setLastName(employee.getLastName());
        responseDTO.setEmail(employee.getEmail());
        responseDTO.setPhone(employee.getPhone());
        responseDTO.setDepartment(employee.getDepartment());
        responseDTO.setJobTitle(employee.getJobTitle());
        responseDTO.setStatus(employee.getStatus());
        responseDTO.setEmploymentType(employee.getEmploymentType());
        responseDTO.setManagerId(employee.getManagerId());
        responseDTO.setLocation(employee.getLocation());
        responseDTO.setDateOfJoining(employee.getDateOfJoining());
        responseDTO.setDateOfExit(employee.getDateOfExit());
        responseDTO.setCreatedAt(employee.getCreatedAt());
        responseDTO.setUpdatedAt(employee.getUpdatedAt());
        responseDTO.setNotes(employee.getNotes());

        return responseDTO;
    }

    // For updates: copy writable fields from DTO into existing entity
    public static void copyToEntity(EmployeeRequestDTO requestDTO, Employee targetEmployee){

        if (requestDTO == null || targetEmployee == null) return;

        targetEmployee.setEmployeeCode(requestDTO.getEmployeeCode());
        targetEmployee.setFirstName(requestDTO.getFirstName());
        targetEmployee.setLastName(requestDTO.getLastName());
        targetEmployee.setEmail(requestDTO.getEmail());
        targetEmployee.setPhone(requestDTO.getPhone());
        targetEmployee.setDepartment(requestDTO.getDepartment());
        targetEmployee.setJobTitle(requestDTO.getJobTitle());
        targetEmployee.setStatus(requestDTO.getStatus());
        targetEmployee.setEmploymentType(requestDTO.getEmploymentType());
        targetEmployee.setManagerId(requestDTO.getManagerId());
        targetEmployee.setLocation(requestDTO.getLocation());
        targetEmployee.setDateOfJoining(requestDTO.getDateOfJoining());
        targetEmployee.setDateOfExit(requestDTO.getDateOfExit());
        targetEmployee.setNotes(requestDTO.getNotes());
    }
}
