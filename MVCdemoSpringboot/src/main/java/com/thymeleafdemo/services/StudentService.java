package com.thymeleafdemo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thymeleafdemo.repository.Student;
import com.thymeleafdemo.repository.StudentRepository;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepo;
	public void addStudent(Student student)
	{
		studentRepo.save(student);
	}
}
