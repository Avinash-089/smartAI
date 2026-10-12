package com.example.student_management.service;

import com.example.student_management.model.Grade;
import com.example.student_management.model.Student;
import com.example.student_management.repository.GradeRepository;
import com.example.student_management.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;

    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    public List<Grade> getGradesByStudent(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    public List<Grade> getGradesByStudentAndSemester(Long studentId, Integer semester) {
        return gradeRepository.findByStudentIdAndSemester(studentId, semester);
    }

    public Grade saveGrade(Grade grade) {
        if (grade.getGrade() == null && grade.getMarksObtained() != null && grade.getMaxMarks() != null && grade.getMaxMarks() > 0) {
            double pct = (grade.getMarksObtained() / grade.getMaxMarks()) * 100.0;
            if (pct >= 90) grade.setGrade("A+");
            else if (pct >= 80) grade.setGrade("A");
            else if (pct >= 70) grade.setGrade("B");
            else if (pct >= 60) grade.setGrade("C");
            else if (pct >= 50) grade.setGrade("D");
            else grade.setGrade("F");
        }
        Grade saved = gradeRepository.save(grade);
        updateStudentGpa(grade.getStudentId());
        return saved;
    }

    public void deleteGrade(Long id) {
        gradeRepository.findById(id).ifPresent(g -> {
            Long studentId = g.getStudentId();
            gradeRepository.deleteById(id);
            updateStudentGpa(studentId);
        });
    }

    private void updateStudentGpa(Long studentId) {
        studentRepository.findById(studentId).ifPresent(s -> {
            List<Grade> grades = gradeRepository.findByStudentId(studentId);
            if (!grades.isEmpty()) {
                double totalMarks = 0;
                double totalMax = 0;
                for (Grade g : grades) {
                    if (g.getMarksObtained() != null && g.getMaxMarks() != null && g.getMaxMarks() > 0) {
                        totalMarks += g.getMarksObtained();
                        totalMax += g.getMaxMarks();
                    }
                }
                if (totalMax > 0) {
                    double pct = (totalMarks / totalMax) * 100.0;
                    double gpa = (pct / 20.0); // 4.0 scale roughly (e.g. 80% = 4.0, 70% = 3.5, etc)
                    gpa = Math.min(4.0, Math.round(gpa * 10.0) / 10.0);
                    s.setCurrentGpa(gpa);
                    studentRepository.save(s);
                }
            }
        });
    }
}
