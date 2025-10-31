package com.erp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.Course;

public interface CoursesRepository extends JpaRepository<Course, Integer>{

	Course findAllById(Long id);

	Optional<Course> findByName(String name);

	Course findById(Long id);

}
