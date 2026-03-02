package com.democorp.employee.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeFilter {

    private String department;
    private String status;
    private String employmentType;

    private String blankToNull(String s){
        return (s == null || s.isBlank()) ? null : s;
    }
}
