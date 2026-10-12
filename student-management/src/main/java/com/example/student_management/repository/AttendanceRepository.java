package com.example.student_management.repository;

import com.example.student_management.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentId(Long studentId);
    List<Attendance> findBySubjectId(Long subjectId);
    List<Attendance> findByDate(LocalDate date);
    List<Attendance> findByStudentIdAndDate(Long studentId, LocalDate date);
    List<Attendance> findBySubjectIdAndDate(Long subjectId, LocalDate date);
    java.util.Optional<Attendance> findByStudentIdAndSubjectIdAndDate(Long studentId, Long subjectId, LocalDate date);
    long countByStudentIdAndStatus(Long studentId, String status);
    long countByStudentId(Long studentId);
}
