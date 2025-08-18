package com.thymeleafdemo.StudentController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.thymeleafdemo.repository.Student;
import com.thymeleafdemo.services.StudentService;

@Controller
public class Studentcontrollerclass {

	@Autowired
	private StudentService studentService;

	@PostMapping("/submit")
	public String addStudent(@ModelAttribute Student student)
	{
		studentService.addStudent(student);
		System.out.println(student);
		return "redirect:/";

	}
}
