package com.democorp.employee.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class EmployeeRequestDTO {

    //Business identifier
    @NotBlank(message="employee code is required")
    @Size(max = 64)
    private String employeeCode;

    //Person Details
    @NotBlank(message="firstName is required")
    @Size(max = 80)
    private String firstName;

    @NotBlank(message="lastName is required")
    @Size(max = 80)
    private String lastName;

    @NotBlank(message="email is required")
    @Email(message="firstName is required")
    @Size(max = 84)
    private String email;

    @Size(max = 32)
    private String phone;

    //Organization Details
    @Size(max = 84)
    private String department;

    @Size(max = 120)
    private String jobTitle;

    @Size(max = 40)
    private String status;

    @Size(max = 40)
    private String employmentType;

    private Long managerId;

    @Size(max = 120)
    private String location;

    //Employment Dates
    private LocalDate dateOfJoining;
    private LocalDate dateOfExit;

    //Notes
    @Size(max = 10_000)
    private String notes;
}
