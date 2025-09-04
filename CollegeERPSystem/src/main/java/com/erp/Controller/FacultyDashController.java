package com.erp.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.erp.model.Student;
import com.erp.services.StudentService;

@Controller
public class FacultyDashController {

	@Autowired
	private StudentService studentService;

	@GetMapping("/faculty-dashboard")
	public String showadminDashboard(Model model) {

		List<Student> s1 = studentService.getAllStudent();
		List<Student> s2 = new ArrayList<>();

		long cnt = 0;
		for (Student stu : s1) {
			if (stu.getCourse() != null && "mcs".equalsIgnoreCase(stu.getCourse().getName())) {
				s2.add(stu);

				cnt++;
			}
		}

		long totalStudents = cnt;
		model.addAttribute("totalStudents", totalStudents);

		model.addAttribute("students", s2);


		return "faculty-links/faculty-dashboard"; // template name, no redirect
	}
	
	@GetMapping("/attendance")
	public String showattendance(Model model) {

		List<Student> s1 = studentService.getAllStudent();
		List<Student> s2 = new ArrayList<>();

		long cnt = 0;
		for (Student stu : s1) {
			if (stu.getCourse() != null && "mcs".equalsIgnoreCase(stu.getCourse().getName())) {
				s2.add(stu);

				cnt++;
			}
		}

		long totalStudents = cnt;
		model.addAttribute("totalStudents", totalStudents);

		model.addAttribute("students", s2);


		return "faculty-links/attendance"; // template name, no redirect
	}

}
