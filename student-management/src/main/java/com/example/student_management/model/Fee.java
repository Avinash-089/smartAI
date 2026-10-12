package com.example.student_management.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "fees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    private Double amount;

    private LocalDate dueDate;

    private LocalDate paidDate;

    @Column(length = 50)
    private String status; // PAID, PENDING, OVERDUE

    private String description;
}
