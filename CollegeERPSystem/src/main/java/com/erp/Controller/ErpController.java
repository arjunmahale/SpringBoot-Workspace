package com.erp.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.erp.model.Attendance;
import com.erp.model.Course;
import com.erp.model.Login;
import com.erp.model.Student;
import com.erp.services.AttendanceService;
import com.erp.services.FacultyService;
import com.erp.services.LoginService;
import com.erp.services.StudentService;
import com.erp.services.courseService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ErpController {

    @Autowired
    private StudentService studserv;

    @Autowired
    private courseService courseServ;

    @Autowired
    private LoginService loginServ;


    @Autowired
    private FacultyService facultyService1;

	@Autowired
	private FacultyService facultyService;

	@Autowired
	private AttendanceService attendanceService;
    // Home page
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Student management - only list of students
    @GetMapping("/student-management")
    public String showStudents(Model model) {

        List<Student> students = studserv.getAllStudent();
        model.addAttribute("students", students);

        return "admin-links/student-management"; // ✅ table page only
    }


    @GetMapping("/admin-dashboard")
    public String showadminDashboard(Model model) {
    	long totalStudents = studserv.countStudents();
    	long totalCourses = courseServ.countCourses();
    	long totalFaculty = facultyService1.countFaculty();

    	model.addAttribute("totalStudents", totalStudents);
    	model.addAttribute("totalCourses", totalCourses);
    	model.addAttribute("totalFaculty", totalFaculty);


    	   model.addAttribute("students", studserv.getRecentStudents()); // maybe last 5
    	    model.addAttribute("courses", courseServ.getAllcourses());
    	    model.addAttribute("faculties", facultyService1.getAllfaculties());



        return "admin-links/admin-dashboard"; // template name, no redirect
    }

    @GetMapping("/attendance-list")
	public String showAttendance(HttpSession session,Model model) {
    	  // Check if user is logged in
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/"; // redirect to login page if not logged in
        }
	    LocalDate today = LocalDate.now();
	    model.addAttribute("today", today.toString());
String user1=(String) session.getAttribute("user");

		model.addAttribute("user",user1);
	    // ✅ Get all students
	    List<Student> s1 = studserv.getAllStudent();
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

	    return "admin-links/attendance-list"; // ✅ return view
	}
    @GetMapping("/total-attendance-list")
    public String showAttendanceAllTime(HttpSession session,Model model) {
    	LocalDate today = LocalDate.now();
    	model.addAttribute("today", today.toString());
    	String user1=(String) session.getAttribute("user");

    	model.addAttribute("user",user1);
    	// ✅ Get all students
    	List<Student> s1 = studserv.getAllStudent();
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

    	model.addAttribute("students", s1);
    	model.addAttribute("attendanceMap", attendanceMap);
    	model.addAttribute("totalStudents", s1.size());

    	return "admin-links/total-attendance-list"; // ✅ return view
    }

    // Open form to add student
    @GetMapping("/add-student")
    public String showStudentForm(Model model) {
        model.addAttribute("student", new Student()); // empty student for new entry
        model.addAttribute("courses", courseServ.getAllcourses());
        model.addAttribute("title","Add New Student");
        model.addAttribute("formAction","/save-student");
        return "admin-links/student-form"; // ✅ new Thymeleaf template
    }

    
    //student update and delete will be managed by faculty only 
    // Open form to update student
    @GetMapping("/update/{id}")
    public String editStudentForm(@PathVariable() Long id, Model model) {
        Student student = studserv.getStudentById(id); // fetch student by ID
        model.addAttribute("student", student);
        model.addAttribute("title", "Update Student");
        model.addAttribute("courses", courseServ.getAllcourses());
        model.addAttribute("formAction","/save-student");
        return "admin-links/student-form"; // same form but prefilled
    }

    // Save or update student
    @PostMapping("/save-student")
    public String saveStudents(@ModelAttribute Student student, RedirectAttributes redirectAttributes) {
        long pass = student.getMobile();
        student.setPassword(String.valueOf(pass));

        // 🔎 check if student already exists
        Student existing = null;
        if (student.getId() != 0) { // means update
            existing = studserv.getStudentById(student.getId());
        }

        Login login;
        if (existing != null && existing.getLogin() != null) {
            // ✅ Reuse existing login
            login = existing.getLogin();
        } else {
            // ✅ Create new login (insert case)
            login = new Login();
            login.setRole("student");
        }

        // Update login fields from student
        login.setName(student.getName());
        login.setPassword(student.getPassword());
        login.setEmail(student.getEmail());

        // Maintain both sides of relation
        login.setStudent(student);
        student.setLogin(login);

        studserv.saveStudent(student); // cascade saves login too

        redirectAttributes.addFlashAttribute("message", "Student saved successfully!");
        return "redirect:/student-management";
    }


    // Delete student
    @PostMapping("/delete")
    public String deleteStudent(@ModelAttribute Student student, RedirectAttributes redirectAttributes) {

        studserv.deleteStudent(student);



        redirectAttributes.addFlashAttribute("message", "Student deleted successfully!");
        return "redirect:/student-management";
    }


//    @GetMapping("/search-student/{id}")
//    public String searchStudent(@PathVariable Long id, Model model) {
//        Student student = studserv.getStudentById(id);
//        if (student != null) {
//            model.addAttribute("students", List.of(student)); // single student as a list
//        } else {
//            model.addAttribute("students", List.of()); // empty list if not found
//        }
//        return "/admin-links/student-management"; // your Thymeleaf template name
//    }

}
