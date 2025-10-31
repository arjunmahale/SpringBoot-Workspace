package com.erp.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.erp.model.Course;
import com.erp.model.Faculty;
import com.erp.model.Login;
import com.erp.model.Student;
import com.erp.services.FacultyService;
import com.erp.services.LoginService;
import com.erp.services.StudentService;
import com.erp.services.courseService;


@Controller
public class LoginController {
	@Autowired
	private LoginService loginServ;

	@Autowired
	private StudentService studentService;

	@Autowired
	private courseService courseService;

	@Autowired
	private FacultyService facultyService;

	@PostMapping("/login")
	public String login(@ModelAttribute Login login,HttpSession session, Model model) {
	    Login dbUser = loginServ.getUserByName(login.getName());



	    System.out.println(dbUser.getId());
	    // LocalDate selectedDate = (date == null) ? today : date;
	    if (dbUser == null) {
	        // Username not found → include entered name
	        model.addAttribute("name", login.getName());
	        model.addAttribute("error", "is not found");
	        return "index";
	    }

	    if (!login.getPassword().equals(dbUser.getPassword())) {
	        // Password wrong → no name
	        model.addAttribute("error", "password is wrong");
	        return "index";
	    }

	    if (!login.getRole().equals(dbUser.getRole())) {
	        // Role wrong → no name
	        model.addAttribute("error", "role is wrong");
	        return "index";
	    }

	    // ✅ Correct login
	    if ("admin".equals(dbUser.getRole())) {
	    	session.setAttribute("loggedInUser", dbUser);
	        session.setAttribute("user", dbUser.getName());
	    	long totalStudents = studentService.countStudents();
	    	long totalCourses = courseService.countCourses();
	    	long totalFaculty = facultyService.countFaculty();
	    	  session.setAttribute("role", dbUser.getRole());

	    	System.out.println("Students: " + totalStudents);
	    	System.out.println("Courses: " + totalCourses);
	    	System.out.println("Faculty: " + totalFaculty);

	    	model.addAttribute("totalStudents", totalStudents);
	    	model.addAttribute("totalCourses", totalCourses);
	    	model.addAttribute("totalFaculty", totalFaculty);

	    	  LocalDate today = LocalDate.now();
	    model.addAttribute("today",today.toString());

	    	   model.addAttribute("students", studentService.getRecentStudents()); // maybe last 5
	    	    model.addAttribute("courses", courseService.getAllcourses());
	    	    model.addAttribute("faculties", facultyService.getAllfaculties());



	        return "/admin-links/admin-dashboard";
	    } else if ("faculty".equals(dbUser.getRole())) {

	    	  // store dbUser in session
	        session.setAttribute("loggedInUser", dbUser);
	        session.setAttribute("user", dbUser.getName());
	        session.setAttribute("role", dbUser.getRole());
	        String role= (String) session.getAttribute("role");
	        System.out.println(role);


	    	//long totalStudents = studentService.countStudents();

	        String course= dbUser.getFaculty().getCourse().getName();


           session.setAttribute("admin_course", course);

	    List<Student> s1 =	studentService.getAllStudent();
	    List<Student> s2 = new ArrayList<>();

	    long cnt=0;
	    for (Student stu : s1) {
	        if (stu.getCourse() != null && course.equalsIgnoreCase(stu.getCourse().getName())) {
	            s2.add(stu);

	            cnt++;
	        }
	    }

	    long totalStudents=cnt;
	    	model.addAttribute("totalStudents", totalStudents);

	    	  LocalDate today = LocalDate.now();
	  	    model.addAttribute("today",today.toString());
	    	model.addAttribute("students", s2);
	    	System.out.println(today.toString());

	    	model.addAttribute("user",dbUser.getName());
	        return "faculty-links/faculty-dashboard";
	    } else if ("student".equals(dbUser.getRole())) {




	    	  LocalDate today = LocalDate.now();
	    model.addAttribute("today",today.toString());

	    	session.setAttribute("loggedInUser", dbUser);
	    	session.setAttribute("student", dbUser.getStudent());
	    	//model.addAttribute("student",dbUser.getStudent());

	        session.setAttribute("user", dbUser.getName());
	        return "redirect:/student-dashboard";
	    }

	    model.addAttribute("error", "Something went wrong");
	    return "index";
	}


	@GetMapping("/register-student")
	public String Registration( Model model)
	{
		 model.addAttribute("student", new Student());
		 model.addAttribute("faculty", new Faculty());
		return "registration";

	}






}
