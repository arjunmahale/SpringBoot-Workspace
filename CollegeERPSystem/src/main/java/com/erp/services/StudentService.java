package com.erp.services;

import java.util.List;

import org.hibernate.engine.jdbc.spi.TypeNullability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.Student;
import com.erp.repositories.StudentRepository;

@Service
public class StudentService {

	@Autowired
	private	StudentRepository studRepo;
	
	public List<Student> getAllStudent()
	{
		List<Student> student= studRepo.findAll();
		if(student ==null) {
			System.out.println("empty list");
		}
		return student;
		
	}
	
	
	
	public void saveStudent(Student student)
	{
		studRepo.save(student);
		
		
	}
}
