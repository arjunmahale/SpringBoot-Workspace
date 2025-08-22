package com.erp.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.erp.model.Course;
import com.erp.model.Student;
import com.erp.services.StudentService;
import com.erp.services.courseService;

@Controller

public class ErpController {

	@Autowired
	private StudentService studserv;
	@Autowired
	private courseService courseServ;
	@GetMapping("/")
	public String index()
	{
		return "index";
	}
	
	@GetMapping("/student-management")
	public  String  showStudents( Model model) {
		

		List<Student> students= studserv.getAllStudent();
		        model.addAttribute("students", students);
		        
		        List<Course> courses= courseServ.getAllcourses();
		        model.addAttribute("courses", courses);
		return "student-management";
		
	}
	@PostMapping("/students/save")
	public  String  saveStudents(@ModelAttribute Student student) {
		
		
		studserv.saveStudent(student);
		
		return "redirect:/student-management";
		
	}
}
