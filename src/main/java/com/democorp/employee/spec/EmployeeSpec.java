package com.democorp.employee.spec;

import com.democorp.employee.dto.EmployeeFilter;
import com.democorp.employee.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpec {

    private EmployeeSpec() {}

    private static Specification<Employee> alwaysTrue(){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
    }

    public static Specification<Employee> byFilter(EmployeeFilter filter){
        Specification<Employee> employeeSpecification = Specification.where(alwaysTrue());
        if(filter == null) return employeeSpecification;
        if(filter.getDepartment() != null){
            employeeSpecification = employeeSpecification.and(
                    ( (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                                    criteriaBuilder.lower(root.get("department")), filter.getDepartment().toLowerCase()
                            )
                    )
            );
        }
        if(filter.getStatus() != null){
            employeeSpecification = employeeSpecification.and(
                    ((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                            criteriaBuilder.lower(root.get("status")), filter.getStatus().toLowerCase()
                        )
                    )
            );
        }
        if(filter.getEmploymentType() != null){
            employeeSpecification = employeeSpecification.and(
                    ((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                            criteriaBuilder.lower(root.get("employmentType")), filter.getEmploymentType().toLowerCase()
                        )
                    )
            );
        }

        return employeeSpecification;
    }
}
