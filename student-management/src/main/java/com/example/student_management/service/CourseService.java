package com.example.student_management.service;

import com.example.student_management.exception.BadRequestException;
import com.example.student_management.exception.DuplicateResourceException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.model.Course;
import com.example.student_management.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));
    }

    public Course saveCourse(Course course) {
        if (course.getCode() == null || course.getCode().trim().isBlank()) {
            throw new BadRequestException("Course code is required");
        }
        if (course.getName() == null || course.getName().trim().isBlank()) {
            throw new BadRequestException("Course name is required");
        }

        String cleanCode = course.getCode().trim().toUpperCase();
        if (courseRepository.findByCode(cleanCode).isPresent()) {
            throw new DuplicateResourceException("Course with code already exists: " + cleanCode);
        }

        course.setCode(cleanCode);
        course.setName(course.getName().trim());
        if (course.getDuration() == null || course.getDuration() <= 0) {
            course.setDuration(4);
        }
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course updated) {
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));

        if (updated.getName() != null && !updated.getName().trim().isBlank()) {
            existing.setName(updated.getName().trim());
        }
        if (updated.getCode() != null && !updated.getCode().trim().isBlank()) {
            String cleanCode = updated.getCode().trim().toUpperCase();
            Optional<Course> other = courseRepository.findByCode(cleanCode);
            if (other.isPresent() && !other.get().getId().equals(id)) {
                throw new DuplicateResourceException("Course code already in use: " + cleanCode);
            }
            existing.setCode(cleanCode);
        }
        if (updated.getDuration() != null && updated.getDuration() > 0) {
            existing.setDuration(updated.getDuration());
        }
        if (updated.getDescription() != null) {
            existing.setDescription(updated.getDescription());
        }
        return courseRepository.save(existing);
    }

    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found with ID: " + id);
        }
        courseRepository.deleteById(id);
    }
}
