package com.example.student_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendancePredictionDto {
    private Long studentId;
    private String studentName;
    private String course;
    private long totalSessions;
    private long presentSessions;
    private long absentSessions;
    private long lateSessions;
    private double attendancePercentage;
    private double requiredThreshold; // e.g. 75.0%
    private double criticalThreshold; // e.g. 65.0%
    private String eligibilityStatus;  // ELIGIBLE, AT_RISK_WARNING, DETAINED
    private int sessionsNeededForEligibility;
    private String explanation;
    private List<String> recommendations;
    private String calculationType; // "RULE_BASED_MATHEMATICAL_ANALYSIS"
}
