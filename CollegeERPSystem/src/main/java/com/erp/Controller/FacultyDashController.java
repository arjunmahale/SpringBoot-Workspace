package com.erp.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.erp.model.Attendance;
import com.erp.model.Student;
import com.erp.services.AttendanceService;
import com.erp.services.StudentService;

@Controller
public class FacultyDashController {

	@Autowired
	private StudentService studentService;
	
	@Autowired
	private AttendanceService attendanceService;

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

		//model.addAttribute("user",dbUser.getName());
		return "faculty-links/faculty-dashboard"; // template name, no redirect
	}
	
	@GetMapping("/attendance")
	public String showAttendance(Model model) {
	    LocalDate today = LocalDate.now();
	    model.addAttribute("today", today.toString());

	    // ✅ Get all students
	    List<Student> s1 = studentService.getAllStudent();
	    List<Student> s2 = new ArrayList<>();

	    // ✅ Filter students of course "MCS"
	    for (Student stu : s1) {
	        if (stu.getCourse() != null && "mcs".equalsIgnoreCase(stu.getCourse().getName())) {
	            s2.add(stu);
	        }
	    }

	    // ✅ Attendance map (only for students in MCS)
	    Map<Long, String> attendanceMap = new HashMap<>();
	    List<Attendance> todayAttendance = attendanceService.getByDate(today);

	    for (Attendance att : todayAttendance) {
	        if (att.getStudent().getCourse() != null &&
	            "mcs".equalsIgnoreCase(att.getStudent().getCourse().getName())) {
	            attendanceMap.put(att.getStudent().getId(), att.getStatus());
	        }
	    }

	    model.addAttribute("students", s2);
	    model.addAttribute("attendanceMap", attendanceMap);
	    model.addAttribute("totalStudents", s2.size());

	    return "faculty-links/attendance"; // ✅ return view
	}
	
	
	@PostMapping("/attendance/save")
	public String saveAttendance(@RequestParam Long studentId,
	                             @RequestParam String status,
	                             @RequestParam String name,
	                             @RequestParam String date,
	                             RedirectAttributes redirectAttributes) {

	    Student student = studentService.getStudentById(studentId);
	    if (student == null) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Student not found!");
	        return "redirect:/attendance";
	    }

	    Attendance attendance = new Attendance();
	    attendance.setDate(LocalDate.parse(date));
	    attendance.setStatus(status);
	    attendance.setName(student.getName());
	    attendance.setCourse(student.getCourse() != null ? student.getCourse().getName() : "N/A");
	    attendance.setStudent(student);

	    attendanceService.saveAttendance(attendance);

	    redirectAttributes.addFlashAttribute("savedStudentId", studentId);
	    return "redirect:/attendance";
	}



	

}
