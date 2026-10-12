package com.example.student_management;

import com.example.student_management.model.*;
import com.example.student_management.repository.*;
import com.example.student_management.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class SmartAiSystemFullAuditTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    private MockMvc mockMvc;
    private String validJwtToken;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        validJwtToken = "Bearer " + jwtUtils.generateToken("admin@example.com", "ADMIN", 1L);
    }

    // =========================================================================
    // 1. AUTHENTICATION & JWT SECURITY TESTS (Including Known Issue Verification)
    // =========================================================================

    @Test
    @DisplayName("1.1 Login with valid credentials returns 200 OK and authentic JWT")
    void testLoginSuccess() throws Exception {
        String json = "{\"email\":\"admin@example.com\",\"password\":\"admin123\"}";

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.email").value("admin@example.com"));
    }

    @Test
    @DisplayName("1.2 Login with incorrect password returns 400 Bad Request")
    void testLoginInvalidPassword() throws Exception {
        String json = "{\"email\":\"admin@example.com\",\"password\":\"wrongpassword\"}";

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Invalid email or password")));
    }

    @Test
    @DisplayName("1.3 KNOWN ISSUE VERIFIED: Login token successfully authenticates protected /api/students endpoint")
    void testLoginTokenAccessesStudentsEndpoint() throws Exception {
        // Step 1: Perform login to get real token
        String loginJson = "{\"email\":\"admin@example.com\",\"password\":\"admin123\"}";
        String responseStr = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Extract token
        org.json.JSONObject jsonObj = new org.json.JSONObject(responseStr);
        String token = jsonObj.getString("token");

        // Step 2: Access protected /api/students with the token
        mockMvc.perform(get("/api/students")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // =========================================================================
    // 2. STUDENT CRUD TESTS
    // =========================================================================

    @Test
    @DisplayName("2.1 Create student with valid data returns 201 Created")
    void testCreateStudentSuccess() throws Exception {
        String uniqueEmail = "audit.student." + System.currentTimeMillis() + "@example.com";
        String json = "{\"name\":\"Audit Test Student\",\"email\":\"" + uniqueEmail + "\",\"phone\":\"9876599999\",\"course\":\"Computer Science & Engineering\",\"semester\":2}";

        mockMvc.perform(post("/api/students")
                        .header("Authorization", validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.email").value(uniqueEmail))
                .andExpect(jsonPath("$.rollNumber").isNotEmpty());
    }

    @Test
    @DisplayName("2.2 Create student with duplicate email returns 409 Conflict")
    void testCreateStudentDuplicateEmail() throws Exception {
        String dupEmail = "duplicate.test." + System.currentTimeMillis() + "@example.com";
        String json1 = "{\"name\":\"Original Student\",\"email\":\"" + dupEmail + "\"}";
        mockMvc.perform(post("/api/students")
                        .header("Authorization", validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json1))
                .andExpect(status().isCreated());

        String json2 = "{\"name\":\"Duplicate Student\",\"email\":\"" + dupEmail + "\"}";
        mockMvc.perform(post("/api/students")
                        .header("Authorization", validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"));
    }

    @Test
    @DisplayName("2.3 Create student with invalid email format returns 400 Bad Request")
    void testCreateStudentInvalidEmail() throws Exception {
        String json = "{\"name\":\"Bad Email Student\",\"email\":\"not-an-email\"}";

        mockMvc.perform(post("/api/students")
                        .header("Authorization", validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("2.4 Get student by invalid ID returns 404 Not Found")
    void testGetStudentNotFound() throws Exception {
        mockMvc.perform(get("/api/students/999999")
                        .header("Authorization", validJwtToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    // =========================================================================
    // 3. COURSE CRUD TESTS
    // =========================================================================

    @Test
    @DisplayName("3.1 Create course with valid code returns 201 Created")
    void testCreateCourseSuccess() throws Exception {
        String uniqueCode = "CS_" + (System.currentTimeMillis() % 100000);
        String json = "{\"code\":\"" + uniqueCode + "\",\"name\":\"Distributed Systems Architecture\",\"duration\":4,\"description\":\"Cloud and scalable systems\"}";

        mockMvc.perform(post("/api/courses")
                        .header("Authorization", validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(uniqueCode));
    }

    @Test
    @DisplayName("3.2 Create course with duplicate code returns 409 Conflict")
    void testCreateCourseDuplicateCode() throws Exception {
        String json = "{\"code\":\"CS101\",\"name\":\"Another CS\",\"duration\":4}";

        mockMvc.perform(post("/api/courses")
                        .header("Authorization", validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    // =========================================================================
    // 4. SUBJECT CRUD TESTS
    // =========================================================================

    @Test
    @DisplayName("4.1 Create subject with valid details returns 201 Created")
    void testCreateSubjectSuccess() throws Exception {
        String uniqueCode = "SUB_" + (System.currentTimeMillis() % 100000);
        String json = "{\"code\":\"" + uniqueCode + "\",\"name\":\"Computer Networks\",\"credits\":4,\"courseName\":\"Computer Science & Engineering\"}";

        mockMvc.perform(post("/api/subjects")
                        .header("Authorization", validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(uniqueCode));
    }

    // =========================================================================
    // 5. ATTENDANCE & PREDICTION TESTS
    // =========================================================================

    @Test
    @DisplayName("5.1 Record attendance for nonexistent student returns 404 Not Found")
    void testAttendanceNonexistentStudent() throws Exception {
        String json = "{\"studentId\":999999,\"date\":\"" + LocalDate.now() + "\",\"status\":\"PRESENT\"}";

        mockMvc.perform(post("/api/attendance")
                        .header("Authorization", validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("5.2 Record attendance with invalid status returns 400 Bad Request")
    void testAttendanceInvalidStatus() throws Exception {
        Student s = studentRepository.findAll().get(0);
        String json = "{\"studentId\":" + s.getId() + ",\"date\":\"" + LocalDate.now() + "\",\"status\":\"UNKNOWN_STATUS\"}";

        mockMvc.perform(post("/api/attendance")
                        .header("Authorization", validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("5.3 Attendance prediction calculates percentage and threshold correctly")
    void testAttendancePrediction() throws Exception {
        Student s = studentRepository.findAll().get(0);

        mockMvc.perform(get("/api/prediction/attendance/" + s.getId())
                        .header("Authorization", validJwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(s.getId()))
                .andExpect(jsonPath("$.attendancePercentage").isNumber())
                .andExpect(jsonPath("$.requiredThreshold").value(75.0))
                .andExpect(jsonPath("$.eligibilityStatus").isNotEmpty())
                .andExpect(jsonPath("$.calculationType").value("RULE_BASED_MATHEMATICAL_ANALYSIS"));
    }

    // =========================================================================
    // 6. FACULTY CRUD TESTS
    // =========================================================================

    @Test
    @DisplayName("6.1 Create faculty with valid data returns 201 Created")
    void testCreateFacultySuccess() throws Exception {
        String uniqueEmail = "prof." + System.currentTimeMillis() + "@example.com";
        String json = "{\"name\":\"Dr. Margaret Hamilton\",\"email\":\"" + uniqueEmail + "\",\"department\":\"Computer Science\",\"designation\":\"Senior Scientist\",\"phone\":\"9876540001\"}";

        mockMvc.perform(post("/api/faculty")
                        .header("Authorization", validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(uniqueEmail));
    }

    @Test
    @DisplayName("6.2 Get faculty by invalid ID returns 404 Not Found")
    void testGetFacultyNotFound() throws Exception {
        mockMvc.perform(get("/api/faculty/999999")
                        .header("Authorization", validJwtToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
