package com.example.student_management.config;

import com.example.student_management.model.*;
import com.example.student_management.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository userRepository;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final AttendanceRepository attendanceRepository;
    private final GradeRepository gradeRepository;
    private final FeeRepository feeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Starting DataInitializer...");

        // 1. Seed Users
        seedUsers();

        // 2. Seed Courses & Subjects
        seedCoursesAndSubjects();

        // 3. Seed Faculty
        seedFaculty();

        // 4. Seed Students
        seedStudents();

        // 5. Seed Attendance & Fees
        seedAttendanceAndFees();

        log.info("DataInitializer completed successfully.");
    }

    private void seedUsers() {
        if (!userRepository.existsByEmail("admin@example.com")) {
            userRepository.save(AppUser.builder()
                    .name("System Administrator")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role("ADMIN")
                    .build());
        }

        if (!userRepository.existsByEmail("faculty@example.com")) {
            userRepository.save(AppUser.builder()
                    .name("Prof. Ada Lovelace")
                    .email("faculty@example.com")
                    .password(passwordEncoder.encode("faculty123"))
                    .role("FACULTY")
                    .build());
        }

        if (!userRepository.existsByEmail("avinash.student@example.com")) {
            userRepository.save(AppUser.builder()
                    .name("Avinash")
                    .email("avinash.student@example.com")
                    .password(passwordEncoder.encode("student123"))
                    .role("STUDENT")
                    .build());
        }
    }

    private void seedCoursesAndSubjects() {
        if (courseRepository.count() == 0) {
            courseRepository.save(Course.builder().code("CS101").name("Computer Science & Engineering").duration(4).description("Core computing, algorithms, and systems engineering.").build());
            courseRepository.save(Course.builder().code("AI301").name("Artificial Intelligence & Data Science").duration(4).description("Machine learning, deep neural models, and data analytics.").build());
            courseRepository.save(Course.builder().code("IT201").name("Information Technology").duration(4).description("Enterprise systems, networking, and cloud services.").build());
        }

        if (subjectRepository.count() == 0) {
            subjectRepository.save(Subject.builder().code("CS301").name("Data Structures & Algorithms").credits(4).courseName("Computer Science & Engineering").build());
            subjectRepository.save(Subject.builder().code("AI302").name("Machine Learning Fundamentals").credits(4).courseName("Artificial Intelligence & Data Science").build());
            subjectRepository.save(Subject.builder().code("CS303").name("Database Management Systems").credits(3).courseName("Computer Science & Engineering").build());
            subjectRepository.save(Subject.builder().code("IT304").name("Cloud Computing & DevOps").credits(3).courseName("Information Technology").build());
        }
    }

    private void seedFaculty() {
        if (facultyRepository.count() == 0) {
            facultyRepository.save(Faculty.builder().name("Dr. Alan Turing").email("alan.turing@example.com").department("Computer Science").designation("Professor & HOD").phone("9876500001").build());
            facultyRepository.save(Faculty.builder().name("Prof. Ada Lovelace").email("faculty@example.com").department("Artificial Intelligence").designation("Associate Professor").phone("9876500002").build());
            facultyRepository.save(Faculty.builder().name("Dr. Grace Hopper").email("grace.hopper@example.com").department("Information Technology").designation("Dean of Academics").phone("9876500003").build());
        }
    }

    private void seedStudents() {
        if (studentRepository.count() <= 1) {
            studentRepository.save(Student.builder()
                    .name("Priya Sharma")
                    .email("priya.sharma@example.com")
                    .phone("9876543211")
                    .course("Artificial Intelligence & Data Science")
                    .semester(3)
                    .rollNumber("AI2024-042")
                    .address("Block B, Campus Hostel")
                    .attendancePercentage(58.0)
                    .currentGpa(2.3)
                    .riskLevel("HIGH")
                    .build());

            studentRepository.save(Student.builder()
                    .name("Rahul Varma")
                    .email("rahul.varma@example.com")
                    .phone("9876543212")
                    .course("Information Technology")
                    .semester(5)
                    .rollNumber("IT2023-018")
                    .address("Downtown Apartments #4")
                    .attendancePercentage(71.5)
                    .currentGpa(2.9)
                    .riskLevel("MEDIUM")
                    .build());

            studentRepository.save(Student.builder()
                    .name("Sneha Patel")
                    .email("sneha.patel@example.com")
                    .phone("9876543213")
                    .course("Computer Science & Engineering")
                    .semester(1)
                    .rollNumber("CS2026-009")
                    .address("Sector 12, Green Avenue")
                    .attendancePercentage(93.0)
                    .currentGpa(3.9)
                    .riskLevel("LOW")
                    .build());
        }
    }

    private void seedAttendanceAndFees() {
        List<Student> students = studentRepository.findAll();
        if (attendanceRepository.count() <= 1 && !students.isEmpty()) {
            LocalDate today = LocalDate.now();
            for (Student s : students) {
                attendanceRepository.save(Attendance.builder().studentId(s.getId()).subjectId(1L).date(today.minusDays(2)).status("PRESENT").remarks("On time").build());
                attendanceRepository.save(Attendance.builder().studentId(s.getId()).subjectId(1L).date(today.minusDays(1)).status("PRESENT").remarks("On time").build());
                attendanceRepository.save(Attendance.builder().studentId(s.getId()).subjectId(1L).date(today).status(
                        "HIGH".equals(s.getRiskLevel()) ? "ABSENT" : "PRESENT"
                ).remarks("Regular session").build());
            }
        }

        if (feeRepository.count() == 0 && !students.isEmpty()) {
            LocalDate dueDate = LocalDate.now().plusMonths(1);
            for (Student s : students) {
                feeRepository.save(Fee.builder()
                        .studentId(s.getId())
                        .amount(45000.0)
                        .dueDate(dueDate)
                        .paidDate("LOW".equals(s.getRiskLevel()) ? LocalDate.now().minusDays(5) : null)
                        .status("LOW".equals(s.getRiskLevel()) ? "PAID" : "PENDING")
                        .description("Semester Tuition & Lab Maintenance Fee")
                        .build());
            }
        }
    }
}
