package com.erp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.ui.Model;

import com.erp.model.Student;

public interface StudentRepository extends JpaRepository<Student, Integer>{

	Student findAllById(Long id);

	


}
