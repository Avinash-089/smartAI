package com.example.student_management.service;

import com.example.student_management.model.Fee;
import com.example.student_management.repository.FeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeeService {

    private final FeeRepository feeRepository;

    public List<Fee> getAllFees() {
        return feeRepository.findAll();
    }

    public List<Fee> getFeesByStudent(Long studentId) {
        return feeRepository.findByStudentId(studentId);
    }

    public List<Fee> getFeesByStatus(String status) {
        return feeRepository.findByStatus(status.toUpperCase());
    }

    public Fee saveFee(Fee fee) {
        if (fee.getStatus() == null) {
            fee.setStatus("PENDING");
        }
        return feeRepository.save(fee);
    }

    public Fee markAsPaid(Long feeId) {
        Fee fee = feeRepository.findById(feeId)
                .orElseThrow(() -> new com.example.student_management.exception.ResourceNotFoundException("Fee record not found with ID: " + feeId));
        fee.setStatus("PAID");
        fee.setPaidDate(LocalDate.now());
        return feeRepository.save(fee);
    }

    public void deleteFee(Long id) {
        if (!feeRepository.existsById(id)) {
            throw new com.example.student_management.exception.ResourceNotFoundException("Fee record not found with ID: " + id);
        }
        feeRepository.deleteById(id);
    }
}
