package com.erp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.Course;
import com.erp.model.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, Long>{
	
	Faculty findAllById(Long id);

	Faculty findByCourse(Course course);
}
