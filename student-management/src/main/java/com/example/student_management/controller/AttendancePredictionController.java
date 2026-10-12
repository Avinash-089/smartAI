package com.example.student_management.controller;

import com.example.student_management.dto.AttendancePredictionDto;
import com.example.student_management.service.AttendancePredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prediction")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AttendancePredictionController {

    private final AttendancePredictionService predictionService;

    @GetMapping("/attendance/{studentId}")
    public ResponseEntity<AttendancePredictionDto> getAttendancePrediction(@PathVariable Long studentId) {
        return ResponseEntity.ok(predictionService.predictAttendanceEligibility(studentId));
    }
}
