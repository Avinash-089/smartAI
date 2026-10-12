package com.example.student_management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    private String course;

    private Integer semester;

    private String rollNumber;

    private String address;

    @Column(length = 20)
    private String riskLevel; // LOW, MEDIUM, HIGH

    private Double attendancePercentage;

    private Double currentGpa;
}
