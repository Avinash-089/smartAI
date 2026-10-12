package com.example.student_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsResponse {
    private long totalStudents;
    private long totalFaculty;
    private long totalCourses;
    private long totalSubjects;
    private double averageAttendance;
    private long atRiskCount;
    private long pendingFeesCount;
    private double totalFeesCollected;
    private List<Map<String, Object>> courseDistribution;
    private List<Map<String, Object>> riskBreakdown;
}
