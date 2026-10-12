package com.example.student_management.service;

import com.example.student_management.exception.BadRequestException;
import com.example.student_management.exception.DuplicateResourceException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.model.Attendance;
import com.example.student_management.model.Student;
import com.example.student_management.repository.AttendanceRepository;
import com.example.student_management.repository.StudentRepository;
import com.example.student_management.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    private static final Set<String> VALID_STATUSES = Set.of("PRESENT", "ABSENT", "LATE");

    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    public Attendance getAttendanceById(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with ID: " + id));
    }

    public List<Attendance> getAttendanceByStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with ID: " + studentId);
        }
        return attendanceRepository.findByStudentId(studentId);
    }

    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }

    public Attendance recordAttendance(Attendance attendance) {
        if (attendance.getStudentId() == null) {
            throw new BadRequestException("Student ID is required");
        }
        if (!studentRepository.existsById(attendance.getStudentId())) {
            throw new ResourceNotFoundException("Referenced student not found with ID: " + attendance.getStudentId());
        }

        if (attendance.getSubjectId() != null && !subjectRepository.existsById(attendance.getSubjectId())) {
            throw new ResourceNotFoundException("Referenced subject not found with ID: " + attendance.getSubjectId());
        }

        if (attendance.getStatus() == null || !VALID_STATUSES.contains(attendance.getStatus().toUpperCase())) {
            throw new BadRequestException("Invalid attendance status: " + attendance.getStatus() + ". Must be PRESENT, ABSENT, or LATE");
        }
        attendance.setStatus(attendance.getStatus().toUpperCase());

        if (attendance.getDate() == null) {
            attendance.setDate(LocalDate.now());
        }

        // Prevent duplicate attendance for same student, subject and date
        if (attendance.getSubjectId() != null) {
            Optional<Attendance> existing = attendanceRepository.findByStudentIdAndSubjectIdAndDate(
                    attendance.getStudentId(), attendance.getSubjectId(), attendance.getDate());
            if (existing.isPresent()) {
                throw new DuplicateResourceException("Attendance already recorded for student "
                        + attendance.getStudentId() + " for subject " + attendance.getSubjectId() + " on " + attendance.getDate());
            }
        }

        Attendance saved = attendanceRepository.save(attendance);
        recalculateStudentAttendance(attendance.getStudentId());
        return saved;
    }

    public Attendance updateAttendance(Long id, Attendance updated) {
        Attendance existing = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with ID: " + id));

        if (updated.getStatus() != null) {
            if (!VALID_STATUSES.contains(updated.getStatus().toUpperCase())) {
                throw new BadRequestException("Invalid attendance status: " + updated.getStatus() + ". Must be PRESENT, ABSENT, or LATE");
            }
            existing.setStatus(updated.getStatus().toUpperCase());
        }
        if (updated.getDate() != null) {
            existing.setDate(updated.getDate());
        }
        if (updated.getRemarks() != null) {
            existing.setRemarks(updated.getRemarks());
        }

        Attendance saved = attendanceRepository.save(existing);
        recalculateStudentAttendance(existing.getStudentId());
        return saved;
    }

    public void deleteAttendance(Long id) {
        Attendance existing = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with ID: " + id));
        Long studentId = existing.getStudentId();
        attendanceRepository.deleteById(id);
        recalculateStudentAttendance(studentId);
    }

    public List<Attendance> recordBulkAttendance(List<Attendance> attendances) {
        Set<Long> updatedStudents = new HashSet<>();
        List<Attendance> savedList = new ArrayList<>();
        for (Attendance a : attendances) {
            if (a.getStudentId() == null || !studentRepository.existsById(a.getStudentId())) {
                continue;
            }
            if (a.getStatus() != null && VALID_STATUSES.contains(a.getStatus().toUpperCase())) {
                a.setStatus(a.getStatus().toUpperCase());
            } else {
                a.setStatus("PRESENT");
            }
            if (a.getDate() == null) a.setDate(LocalDate.now());
            savedList.add(attendanceRepository.save(a));
            updatedStudents.add(a.getStudentId());
        }
        for (Long studentId : updatedStudents) {
            recalculateStudentAttendance(studentId);
        }
        return savedList;
    }

    public Map<String, Object> getStudentAttendanceStats(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with ID: " + studentId);
        }
        long total = attendanceRepository.countByStudentId(studentId);
        long present = attendanceRepository.countByStudentIdAndStatus(studentId, "PRESENT");
        long absent = attendanceRepository.countByStudentIdAndStatus(studentId, "ABSENT");
        long late = attendanceRepository.countByStudentIdAndStatus(studentId, "LATE");

        double percentage = total > 0 ? ((double) present / total) * 100.0 : 0.0;
        percentage = Math.round(percentage * 10.0) / 10.0;

        Map<String, Object> stats = new HashMap<>();
        stats.put("studentId", studentId);
        stats.put("totalDays", total);
        stats.put("presentDays", present);
        stats.put("absentDays", absent);
        stats.put("lateDays", late);
        stats.put("percentage", percentage);
        return stats;
    }

    private void recalculateStudentAttendance(Long studentId) {
        studentRepository.findById(studentId).ifPresent(s -> {
            long total = attendanceRepository.countByStudentId(studentId);
            if (total > 0) {
                long present = attendanceRepository.countByStudentIdAndStatus(studentId, "PRESENT");
                double pct = Math.round(((double) present / total) * 1000.0) / 10.0;
                s.setAttendancePercentage(pct);
                if (pct < 65.0) {
                    s.setRiskLevel("HIGH");
                } else if (pct < 75.0) {
                    s.setRiskLevel("MEDIUM");
                } else {
                    s.setRiskLevel("LOW");
                }
                studentRepository.save(s);
            }
        });
    }
}
