package com.example.student_management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "grades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "subject_id", nullable = false)
    private Long subjectId;

    private String examType; // MIDTERM, FINAL, ASSIGNMENT, QUIZ

    private Double marksObtained;

    private Double maxMarks;

    private Integer semester;

    private String grade; // A+, A, B, C, D, F
}
