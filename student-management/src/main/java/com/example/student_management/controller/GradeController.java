package com.example.student_management.controller;

import com.example.student_management.model.Grade;
import com.example.student_management.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GradeController {

    private final GradeService gradeService;

    @GetMapping
    public ResponseEntity<List<Grade>> getAllGrades(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Integer semester) {

        if (studentId != null && semester != null) {
            return ResponseEntity.ok(gradeService.getGradesByStudentAndSemester(studentId, semester));
        }
        if (studentId != null) {
            return ResponseEntity.ok(gradeService.getGradesByStudent(studentId));
        }
        return ResponseEntity.ok(gradeService.getAllGrades());
    }

    @PostMapping
    public ResponseEntity<Grade> recordGrade(@RequestBody Grade grade) {
        return ResponseEntity.ok(gradeService.saveGrade(grade));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGrade(@PathVariable Long id) {
        try {
            gradeService.deleteGrade(id);
            return ResponseEntity.ok(java.util.Map.of("message", "Grade deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }
}
