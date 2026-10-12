package com.example.student_management.controller;

import com.example.student_management.model.Fee;
import com.example.student_management.service.FeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fees")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FeeController {

    private final FeeService feeService;

    @GetMapping
    public ResponseEntity<List<Fee>> getAllFees(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) String status) {

        if (studentId != null) {
            return ResponseEntity.ok(feeService.getFeesByStudent(studentId));
        }
        if (status != null && !status.isBlank()) {
            return ResponseEntity.ok(feeService.getFeesByStatus(status));
        }
        return ResponseEntity.ok(feeService.getAllFees());
    }

    @PostMapping
    public ResponseEntity<Fee> createFee(@RequestBody Fee fee) {
        return ResponseEntity.ok(feeService.saveFee(fee));
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<?> payFee(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(feeService.markAsPaid(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFee(@PathVariable Long id) {
        try {
            feeService.deleteFee(id);
            return ResponseEntity.ok(java.util.Map.of("message", "Fee deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }
}
