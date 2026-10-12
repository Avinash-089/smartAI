package com.example.student_management.controller;

import com.example.student_management.model.Attendance;
import com.example.student_management.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        if (studentId != null) {
            return ResponseEntity.ok(attendanceService.getAttendanceByStudent(studentId));
        }
        if (date != null) {
            return ResponseEntity.ok(attendanceService.getAttendanceByDate(date));
        }
        return ResponseEntity.ok(attendanceService.getAllAttendance());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable Long id) {
        return ResponseEntity.ok(attendanceService.getAttendanceById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Attendance>> getAttendanceByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getAttendanceByStudent(studentId));
    }

    @PostMapping
    public ResponseEntity<Attendance> recordAttendance(@RequestBody Attendance attendance) {
        Attendance saved = attendanceService.recordAttendance(attendance);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Attendance> updateAttendance(@PathVariable Long id, @RequestBody Attendance attendance) {
        return ResponseEntity.ok(attendanceService.updateAttendance(id, attendance));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteAttendance(@PathVariable Long id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.ok(Map.of("message", "Attendance record deleted successfully", "id", id));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Attendance>> recordBulkAttendance(@RequestBody List<Attendance> attendances) {
        return ResponseEntity.ok(attendanceService.recordBulkAttendance(attendances));
    }

    @GetMapping("/stats/{studentId}")
    public ResponseEntity<Map<String, Object>> getStudentAttendanceStats(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getStudentAttendanceStats(studentId));
    }
}
