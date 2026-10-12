package com.example.student_management.service;

import com.example.student_management.exception.BadRequestException;
import com.example.student_management.exception.DuplicateResourceException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.model.Subject;
import com.example.student_management.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + id));
    }

    public List<Subject> getSubjectsByCourse(String courseName) {
        return subjectRepository.findByCourseName(courseName);
    }

    public Subject saveSubject(Subject subject) {
        if (subject.getCode() == null || subject.getCode().trim().isBlank()) {
            throw new BadRequestException("Subject code is required");
        }
        if (subject.getName() == null || subject.getName().trim().isBlank()) {
            throw new BadRequestException("Subject name is required");
        }

        String cleanCode = subject.getCode().trim().toUpperCase();
        if (subjectRepository.findByCode(cleanCode).isPresent()) {
            throw new DuplicateResourceException("Subject code already exists: " + cleanCode);
        }

        subject.setCode(cleanCode);
        subject.setName(subject.getName().trim());
        if (subject.getCredits() == null || subject.getCredits() <= 0) {
            subject.setCredits(3);
        }
        return subjectRepository.save(subject);
    }

    public Subject updateSubject(Long id, Subject updated) {
        Subject existing = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + id));

        if (updated.getName() != null && !updated.getName().trim().isBlank()) {
            existing.setName(updated.getName().trim());
        }
        if (updated.getCode() != null && !updated.getCode().trim().isBlank()) {
            String cleanCode = updated.getCode().trim().toUpperCase();
            Optional<Subject> other = subjectRepository.findByCode(cleanCode);
            if (other.isPresent() && !other.get().getId().equals(id)) {
                throw new DuplicateResourceException("Subject code already in use: " + cleanCode);
            }
            existing.setCode(cleanCode);
        }
        if (updated.getCredits() != null && updated.getCredits() > 0) {
            existing.setCredits(updated.getCredits());
        }
        if (updated.getCourseName() != null) {
            existing.setCourseName(updated.getCourseName());
        }
        return subjectRepository.save(existing);
    }

    public void deleteSubject(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Subject not found with ID: " + id);
        }
        subjectRepository.deleteById(id);
    }
}
