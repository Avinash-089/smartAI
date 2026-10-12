package com.example.student_management.service;

import com.example.student_management.exception.BadRequestException;
import com.example.student_management.exception.DuplicateResourceException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.model.Student;
import com.example.student_management.repository.AttendanceRepository;
import com.example.student_management.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;

    public List<Student> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        for (Student s : students) {
            updateStudentMetrics(s);
        }
        return students;
    }

    public Student getStudentById(Long id) {
        Student s = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        return updateStudentMetrics(s);
    }

    public Student getStudentByEmail(String email) {
        Student s = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with email: " + email));
        return updateStudentMetrics(s);
    }

    public List<Student> getStudentsByCourse(String course) {
        return studentRepository.findByCourse(course);
    }

    public List<Student> getStudentsByRiskLevel(String riskLevel) {
        return studentRepository.findByRiskLevel(riskLevel.toUpperCase());
    }

    public List<Student> searchStudents(String query) {
        return studentRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query);
    }

    public Student saveStudent(Student student) {
        if (student.getName() == null || student.getName().trim().isBlank()) {
            throw new BadRequestException("Student name is required");
        }
        if (student.getEmail() == null || student.getEmail().trim().isBlank()) {
            throw new BadRequestException("Student email is required");
        }
        if (!student.getEmail().contains("@") || !student.getEmail().contains(".")) {
            throw new BadRequestException("Invalid email format: " + student.getEmail());
        }

        Optional<Student> existing = studentRepository.findByEmail(student.getEmail().trim());
        if (existing.isPresent()) {
            throw new DuplicateResourceException("A student with email already exists: " + student.getEmail());
        }

        student.setName(student.getName().trim());
        student.setEmail(student.getEmail().trim().toLowerCase());

        if (student.getRollNumber() == null || student.getRollNumber().isBlank()) {
            student.setRollNumber("STU" + (1000 + (int)(Math.random() * 9000)));
        }
        if (student.getRiskLevel() == null) {
            student.setRiskLevel("LOW");
        }
        if (student.getAttendancePercentage() == null) {
            student.setAttendancePercentage(80.0);
        }
        if (student.getCurrentGpa() == null) {
            student.setCurrentGpa(3.5);
        }
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student updated) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        if (updated.getName() != null && !updated.getName().trim().isBlank()) {
            existing.setName(updated.getName().trim());
        }
        if (updated.getEmail() != null && !updated.getEmail().trim().isBlank()) {
            String newEmail = updated.getEmail().trim().toLowerCase();
            if (!newEmail.contains("@") || !newEmail.contains(".")) {
                throw new BadRequestException("Invalid email format: " + newEmail);
            }
            Optional<Student> other = studentRepository.findByEmail(newEmail);
            if (other.isPresent() && !other.get().getId().equals(id)) {
                throw new DuplicateResourceException("Email already in use by another student: " + newEmail);
            }
            existing.setEmail(newEmail);
        }
        if (updated.getPhone() != null) existing.setPhone(updated.getPhone());
        if (updated.getCourse() != null) existing.setCourse(updated.getCourse());
        if (updated.getSemester() != null) existing.setSemester(updated.getSemester());
        if (updated.getRollNumber() != null) existing.setRollNumber(updated.getRollNumber());
        if (updated.getAddress() != null) existing.setAddress(updated.getAddress());
        if (updated.getRiskLevel() != null) existing.setRiskLevel(updated.getRiskLevel());
        if (updated.getCurrentGpa() != null) existing.setCurrentGpa(updated.getCurrentGpa());
        if (updated.getAttendancePercentage() != null) existing.setAttendancePercentage(updated.getAttendancePercentage());

        return studentRepository.save(existing);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    private Student updateStudentMetrics(Student s) {
        long total = attendanceRepository.countByStudentId(s.getId());
        if (total > 0) {
            long present = attendanceRepository.countByStudentIdAndStatus(s.getId(), "PRESENT");
            double pct = Math.round(((double) present / total) * 1000.0) / 10.0;
            s.setAttendancePercentage(pct);
            if (pct < 65.0) {
                s.setRiskLevel("HIGH");
            } else if (pct < 75.0) {
                s.setRiskLevel("MEDIUM");
            } else {
                s.setRiskLevel("LOW");
            }
        }
        return s;
    }
}
