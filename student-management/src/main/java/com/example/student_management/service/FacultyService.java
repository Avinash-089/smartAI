package com.example.student_management.service;

import com.example.student_management.exception.BadRequestException;
import com.example.student_management.exception.DuplicateResourceException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.model.Faculty;
import com.example.student_management.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public List<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

    public Faculty getFacultyById(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with ID: " + id));
    }

    public List<Faculty> getFacultyByDepartment(String dept) {
        return facultyRepository.findByDepartment(dept);
    }

    public Faculty saveFaculty(Faculty faculty) {
        if (faculty.getName() == null || faculty.getName().trim().isBlank()) {
            throw new BadRequestException("Faculty name is required");
        }
        if (faculty.getEmail() == null || faculty.getEmail().trim().isBlank()) {
            throw new BadRequestException("Faculty email is required");
        }
        if (!faculty.getEmail().contains("@") || !faculty.getEmail().contains(".")) {
            throw new BadRequestException("Invalid faculty email format: " + faculty.getEmail());
        }

        String cleanEmail = faculty.getEmail().trim().toLowerCase();
        if (facultyRepository.findByEmail(cleanEmail).isPresent()) {
            throw new DuplicateResourceException("Faculty email already in use: " + cleanEmail);
        }

        faculty.setName(faculty.getName().trim());
        faculty.setEmail(cleanEmail);
        if (faculty.getDepartment() == null || faculty.getDepartment().isBlank()) {
            faculty.setDepartment("General Engineering");
        }
        if (faculty.getDesignation() == null || faculty.getDesignation().isBlank()) {
            faculty.setDesignation("Assistant Professor");
        }
        return facultyRepository.save(faculty);
    }

    public Faculty updateFaculty(Long id, Faculty updated) {
        Faculty existing = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with ID: " + id));

        if (updated.getName() != null && !updated.getName().trim().isBlank()) {
            existing.setName(updated.getName().trim());
        }
        if (updated.getEmail() != null && !updated.getEmail().trim().isBlank()) {
            String cleanEmail = updated.getEmail().trim().toLowerCase();
            if (!cleanEmail.contains("@") || !cleanEmail.contains(".")) {
                throw new BadRequestException("Invalid faculty email format: " + cleanEmail);
            }
            Optional<Faculty> other = facultyRepository.findByEmail(cleanEmail);
            if (other.isPresent() && !other.get().getId().equals(id)) {
                throw new DuplicateResourceException("Faculty email already in use by another instructor: " + cleanEmail);
            }
            existing.setEmail(cleanEmail);
        }
        if (updated.getDepartment() != null) existing.setDepartment(updated.getDepartment());
        if (updated.getDesignation() != null) existing.setDesignation(updated.getDesignation());
        if (updated.getPhone() != null) existing.setPhone(updated.getPhone());

        return facultyRepository.save(existing);
    }

    public void deleteFaculty(Long id) {
        if (!facultyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Faculty not found with ID: " + id);
        }
        facultyRepository.deleteById(id);
    }
}
