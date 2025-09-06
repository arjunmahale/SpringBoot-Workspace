package com.erp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.erp.model.Student;



public interface StudentRepository extends JpaRepository<Student, Long>{

	Student findAllById(Long id);

	List<Student> findByCourse_Name(String name);





}
