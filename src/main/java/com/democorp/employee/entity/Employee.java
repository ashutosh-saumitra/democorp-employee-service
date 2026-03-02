package com.democorp.employee.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "employees",
        indexes = {
                @Index(name = "idx_employee_code", columnList = "employeeCode", unique = true),
                @Index(name = "idx_department", columnList = "department"),
                @Index(name = "idx_status", columnList = "status"),
                @Index(name = "idx_manager_id", columnList = "managerId")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    //Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Business identity
    @Column(nullable = false, length = 64, unique = true)
    private String employeeCode;

    //Person Details
    @Column(nullable = false, length = 80)
    private String firstName;

    @Column(nullable = false, length = 80)
    private String lastName;

    @Column(nullable = false, length = 160)
    private String email;

    @Column(length = 32)
    private String phone;

    //Org details
    @Column(length = 80)
    private String department;

    @Column(length = 120)
    private String jobTitle;

    @Column(length = 40)
    private String status;

    @Column(length = 40)
    private String employmentType;

    private Long managerId;

    @Column(length = 120)
    private String location;

    // Employment dates
    private LocalDate dateOfJoining;
    private LocalDate dateOfExit;

    // Audit timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Optimistic locking
    //@Version - Prevents lost updates when multiple requests update the same row.
    @Version
    private Long version;

    // Notes
    @Column(columnDefinition = "TEXT")
    private String notes;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
