package com.erp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.Course;

public interface CoursesRepository extends JpaRepository<Course, Integer>{

	Course findAllById(Long id);

}
