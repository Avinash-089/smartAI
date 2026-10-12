package com.example.student_management.controller;

import com.example.student_management.dto.AiDto;
import com.example.student_management.service.AiBridgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AiController {

    private final AiBridgeService aiBridgeService;

    @PostMapping("/predict-performance")
    public ResponseEntity<AiDto.PredictResponse> predictPerformance(@RequestBody AiDto.PredictRequest request) {
        return ResponseEntity.ok(aiBridgeService.predictPerformance(request));
    }

    @PostMapping("/chat")
    public ResponseEntity<AiDto.ChatResponse> chat(@RequestBody AiDto.ChatRequest request) {
        return ResponseEntity.ok(aiBridgeService.processChat(request));
    }

    @PostMapping("/facial-attendance")
    public ResponseEntity<AiDto.FacialAttendanceResponse> facialAttendance(@RequestBody AiDto.FacialAttendanceRequest request) {
        return ResponseEntity.ok(aiBridgeService.processFacialAttendance(request));
    }
}
