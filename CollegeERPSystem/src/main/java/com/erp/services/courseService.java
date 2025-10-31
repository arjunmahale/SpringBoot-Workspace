package com.erp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.Course;
import com.erp.repositories.CoursesRepository;

import jakarta.transaction.Transactional;

@Service
public class courseService {

    @Autowired
    private CoursesRepository courseRepo;

    public List<Course> getAllcourses() {
        List<Course> courses = courseRepo.findAll();
        if (courses == null) {
            System.out.println("empty list");
        }
        return courses;
    }

    public Course savecourse(Course course) {
        // TODO Auto-generated method stub
        return courseRepo.save(course);
    }

    @Transactional
    public void deletecourse(Course course) {
        // ✅ Fix 1: Re-fetch by ID safely (avoid detached entity issues)
    	int id1=course.getId();
        Course course1 = courseRepo.findById(id1)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        // ✅ Fix 2: Null-safe student check
        if (course1.getStudents() != null && !course1.getStudents().isEmpty()) {
            throw new IllegalStateException("❌ Cannot delete course with enrolled students");
        }

        // ✅ Fix 3: Perform delete
        courseRepo.delete(course1);
    }

    public Course getCourseById(Long id) {
        // ✅ Fix 4: Use findById (findAllById returns Iterable, not single Course)
        return courseRepo.findById(id).orElseThrow(null);
    }

    // Count total courses
    public long countCourses() {
        return courseRepo.count();
    }

    public Course findByName(String name) {
        return courseRepo.findByName(name)
                .orElseThrow(() -> new RuntimeException("Course not found: " + name));
    }
}
