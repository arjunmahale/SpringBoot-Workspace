package com.erp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.Course;
import com.erp.model.Student;
import com.erp.repositories.CoursesRepository;

import jakarta.transaction.Transactional;
@Service
public class courseService {
	@Autowired
	private CoursesRepository courseRepo;
	
	public List<Course> getAllcourses()
	{
		List<Course> courses= courseRepo.findAll();
		if(courses ==null) {
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
	    Course course1 = courseRepo.findById(course.getId())
	        .orElseThrow(() -> new RuntimeException("Course not found"));

	    if (!course1.getStudents().isEmpty()) {
	        throw new IllegalStateException("❌ Cannot delete course with enrolled students");
	      
	    }

	    courseRepo.delete(course1);
	}


	public Course getCourseById(Long id) {
		// TODO Auto-generated method stub
		Course course = courseRepo.findAllById(id);
		return course;
	}
	
}
