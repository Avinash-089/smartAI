package com.example.student_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

public class AiDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PredictRequest {
        private Long studentId;
        private Double attendancePercentage;
        private Double internalMarks;
        private Double assignmentScore;
        private Integer studyHoursPerWeek;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PredictResponse {
        private Long studentId;
        private String studentName;
        private Double predictedGpa;
        private Double predictedPercentage;
        private String riskLevel; // LOW, MEDIUM, HIGH
        private List<String> riskFactors;
        private List<String> recommendations;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatRequest {
        private String message;
        private String role;
        private String userEmail;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatResponse {
        private String reply;
        private String intent;
        private List<String> suggestions;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FacialAttendanceRequest {
        private String imageBase64;
        private Long subjectId;
        private String classroom;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FacialAttendanceResponse {
        private boolean success;
        private int recognizedCount;
        private List<Long> presentStudentIds;
        private String message;
    }
}
