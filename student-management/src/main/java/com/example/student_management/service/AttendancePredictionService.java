package com.example.student_management.service;

import com.example.student_management.dto.AttendancePredictionDto;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.model.Attendance;
import com.example.student_management.model.Student;
import com.example.student_management.repository.AttendanceRepository;
import com.example.student_management.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendancePredictionService {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;

    public static final double REQUIRED_THRESHOLD = 75.0; // Institutional requirement to appear for exams
    public static final double CRITICAL_THRESHOLD = 65.0; // Below this is automatic detention

    public AttendancePredictionDto predictAttendanceEligibility(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        List<Attendance> records = attendanceRepository.findByStudentId(studentId);
        long totalSessions = records.size();
        long presentSessions = records.stream().filter(r -> "PRESENT".equalsIgnoreCase(r.getStatus())).count();
        long absentSessions = records.stream().filter(r -> "ABSENT".equalsIgnoreCase(r.getStatus())).count();
        long lateSessions = records.stream().filter(r -> "LATE".equalsIgnoreCase(r.getStatus())).count();

        double attendancePercentage;
        if (totalSessions == 0) {
            // Safe handling of zero recorded sessions - avoids division by zero!
            attendancePercentage = 0.0;
        } else {
            // Formula: (Number of present sessions / Total valid recorded sessions) * 100
            attendancePercentage = ((double) presentSessions / totalSessions) * 100.0;
            attendancePercentage = Math.round(attendancePercentage * 10.0) / 10.0;
        }

        String eligibilityStatus;
        int sessionsNeeded = 0;
        List<String> recommendations = new ArrayList<>();
        String explanation;

        if (totalSessions == 0) {
            eligibilityStatus = "NO_RECORDS";
            explanation = "No attendance sessions have been logged for " + student.getName() + " yet.";
            recommendations.add("Ensure course instructor logs laboratory and theory session attendance.");
        } else if (attendancePercentage >= REQUIRED_THRESHOLD) {
            eligibilityStatus = "ELIGIBLE";
            explanation = student.getName() + " meets the institutional attendance benchmark with "
                    + attendancePercentage + "% (minimum requirement is " + REQUIRED_THRESHOLD + "%).";
            recommendations.add("Maintain consistent lecture attendance through end-semester finals.");
        } else if (attendancePercentage >= CRITICAL_THRESHOLD) {
            eligibilityStatus = "AT_RISK_WARNING";
            // Calculate consecutive sessions needed: (P + x) / (T + x) >= 0.75  =>  x >= (0.75*T - P) / 0.25
            double needed = (0.75 * totalSessions - presentSessions) / 0.25;
            sessionsNeeded = (int) Math.ceil(Math.max(0.0, needed));

            explanation = student.getName() + " is currently AT RISK with " + attendancePercentage
                    + "% attendance (below the " + REQUIRED_THRESHOLD + "% threshold).";
            recommendations.add("Student must attend the next " + sessionsNeeded + " consecutive lecture sessions without absence to regain eligibility.");
            recommendations.add("Submit medical or academic leave documentation if absences were excused.");
        } else {
            eligibilityStatus = "DETAINED";
            double needed = (0.75 * totalSessions - presentSessions) / 0.25;
            sessionsNeeded = (int) Math.ceil(Math.max(0.0, needed));

            explanation = "CRITICAL: " + student.getName() + " has only " + attendancePercentage
                    + "% attendance (below critical " + CRITICAL_THRESHOLD + "% threshold) and faces exam detention.";
            recommendations.add("Immediate meeting with Department Head and Academic Counselor required.");
            recommendations.add("Must attend " + sessionsNeeded + " remedial sessions and makeup labs.");
        }

        return AttendancePredictionDto.builder()
                .studentId(student.getId())
                .studentName(student.getName())
                .course(student.getCourse())
                .totalSessions(totalSessions)
                .presentSessions(presentSessions)
                .absentSessions(absentSessions)
                .lateSessions(lateSessions)
                .attendancePercentage(attendancePercentage)
                .requiredThreshold(REQUIRED_THRESHOLD)
                .criticalThreshold(CRITICAL_THRESHOLD)
                .eligibilityStatus(eligibilityStatus)
                .sessionsNeededForEligibility(sessionsNeeded)
                .explanation(explanation)
                .recommendations(recommendations)
                .calculationType("RULE_BASED_MATHEMATICAL_ANALYSIS")
                .build();
    }
}
