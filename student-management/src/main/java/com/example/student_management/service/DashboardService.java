package com.example.student_management.service;

import com.example.student_management.dto.DashboardStatsResponse;
import com.example.student_management.model.Student;
import com.example.student_management.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final AttendanceRepository attendanceRepository;
    private final FeeRepository feeRepository;

    public DashboardStatsResponse getDashboardStats() {
        List<Student> students = studentRepository.findAll();
        long totalStudents = students.size();
        long totalFaculty = facultyRepository.count();
        long totalCourses = courseRepository.count();
        long totalSubjects = subjectRepository.count();

        // Calculate average attendance across students
        double avgAttendance = students.stream()
                .mapToDouble(s -> s.getAttendancePercentage() != null ? s.getAttendancePercentage() : 80.0)
                .average()
                .orElse(80.0);
        avgAttendance = Math.round(avgAttendance * 10.0) / 10.0;

        // Calculate at-risk students
        long atRiskCount = students.stream()
                .filter(s -> "HIGH".equalsIgnoreCase(s.getRiskLevel()))
                .count();

        // Fee stats
        long pendingFeesCount = feeRepository.countByStatus("PENDING");
        double totalFeesCollected = feeRepository.findByStatus("PAID").stream()
                .mapToDouble(f -> f.getAmount() != null ? f.getAmount() : 0.0)
                .sum();

        // Course distribution
        Map<String, Long> courseCounts = students.stream()
                .filter(s -> s.getCourse() != null && !s.getCourse().isBlank())
                .collect(Collectors.groupingBy(Student::getCourse, Collectors.counting()));

        List<Map<String, Object>> courseDistribution = courseCounts.entrySet().stream()
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("course", e.getKey());
                    map.put("count", e.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        // Risk breakdown
        Map<String, Long> riskCounts = students.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getRiskLevel() != null ? s.getRiskLevel().toUpperCase() : "LOW",
                        Collectors.counting()
                ));

        List<Map<String, Object>> riskBreakdown = riskCounts.entrySet().stream()
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("riskLevel", e.getKey());
                    map.put("count", e.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        return DashboardStatsResponse.builder()
                .totalStudents(totalStudents)
                .totalFaculty(totalFaculty)
                .totalCourses(totalCourses)
                .totalSubjects(totalSubjects)
                .averageAttendance(avgAttendance)
                .atRiskCount(atRiskCount)
                .pendingFeesCount(pendingFeesCount)
                .totalFeesCollected(totalFeesCollected)
                .courseDistribution(courseDistribution)
                .riskBreakdown(riskBreakdown)
                .build();
    }
}
