package com.erp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.Course;
import com.erp.repositories.CoursesRepository;
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
	
}
