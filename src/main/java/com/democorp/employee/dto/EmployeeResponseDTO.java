package com.democorp.employee.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class EmployeeResponseDTO {

    // Identity
    private Long id;
    private String employeeCode;

    // Person details
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    // Org details
    private String department;
    private String jobTitle;
    private String status;
    private String employmentType;
    private Long managerId;
    private String location;

    // Employment dates
    private LocalDate dateOfJoining;
    private LocalDate dateOfExit;

    // Audit
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String notes;

}
