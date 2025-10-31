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
import com.erp.utilities.EmailService;

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
    private EmailService emailService;

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


        // ✅ Today’s date
        LocalDate today = LocalDate.now();
        model.addAttribute("today", today.toString());

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


        // ✅ Today’s date
        LocalDate today = LocalDate.now();
        model.addAttribute("today", today.toString());

    	   model.addAttribute("students", studserv.getRecentStudents()); // maybe last 5
    	    model.addAttribute("courses", courseServ.getAllcourses());
    	    model.addAttribute("faculties", facultyService1.getAllfaculties());



        return "admin-links/admin-dashboard"; // template name, no redirect
    }

    @GetMapping("/attendance-list")
    public String showAttendance(HttpSession session, Model model) {
        // ✅ Check if user is logged in
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/"; // redirect to login page if not logged in
        }

        String user1 = (String) session.getAttribute("user");
        model.addAttribute("user", user1);

        // ✅ Provide today's date
        LocalDate today = LocalDate.now();
        model.addAttribute("today", today.toString());

        // ✅ Get all courses (assuming you have CourseService)
        List<Course> courses = courseServ.getAllcourses();
        model.addAttribute("courses", courses);

        // ✅ Default students list (can be empty or filtered by a default course like "MCS")
        List<Student> allStudents = studserv.getAllStudent();
        List<Student> filteredStudents = new ArrayList<>();
        for (Student stu : allStudents) {
            if (stu.getCourse() != null && "mcs".equalsIgnoreCase(stu.getCourse().getName())) {
                filteredStudents.add(stu);
            }
        }

        // ✅ Attendance map for today (only MCS initially)
        Map<Long, String> attendanceMap = new HashMap<>();
        List<Attendance> todayAttendance = attendanceService.getByDate(today);

        for (Attendance att : todayAttendance) {
            if (att.getStudent().getCourse() != null &&
                "mcs".equalsIgnoreCase(att.getStudent().getCourse().getName())) {
                attendanceMap.put(att.getStudent().getId(), att.getStatus());
            }
        }

        model.addAttribute("students", filteredStudents);
        model.addAttribute("attendanceMap", attendanceMap);
        model.addAttribute("totalStudents", filteredStudents.size());

        return "admin-links/attendance-list"; // ✅ return view
    }

    //attendance by date and course

    @GetMapping("/attendance-list-by-date-course")
    public String getAttendanceByCourseAndDate(
            @RequestParam("course") String courseName,
            @RequestParam("date") LocalDate date,
            HttpSession session,
            Model model) {

        // ✅ Check login
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/"; // redirect to login page if not logged in
        }
     // ✅ Get all courses (assuming you have CourseService)
        List<Course> courses = courseServ.getAllcourses();
        model.addAttribute("courses", courses);
        model.addAttribute("selectedDate", date.toString());
        model.addAttribute("selectedCourse", courseName);
        model.addAttribute("user", session.getAttribute("user"));

        // ✅ Filter students by selected course
        List<Student> studentsByCourse = studserv.getAllStudent().stream()
                .filter(stu -> stu.getCourse() != null &&
                               courseName.equalsIgnoreCase(stu.getCourse().getName()))
                .toList();

        // ✅ Attendance for selected course & date
        Map<Long, String> attendanceMap = new HashMap<>();
        List<Attendance> attendanceList = attendanceService.getByCourseAndDate(courseName, date);

        for (Attendance att : attendanceList) {
            if (att.getStudent().getCourse() != null &&
                courseName.equalsIgnoreCase(att.getStudent().getCourse().getName())) {
                attendanceMap.put(att.getStudent().getId(), att.getStatus());
            }
        }
        // ✅ Provide today's date
        LocalDate today = LocalDate.now();
        model.addAttribute("today", today.toString());
        // ✅ Add to model
        model.addAttribute("students", studentsByCourse);
        model.addAttribute("attendanceMap", attendanceMap);
        model.addAttribute("totalStudents", studentsByCourse.size());

        return "admin-links/attendance-list"; // view page
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
    public String showStudentForm(Model model,HttpSession session) {
        model.addAttribute("student", new Student()); // empty student for new entry
        model.addAttribute("courses", courseServ.getAllcourses());

         model.addAttribute("admin_course", session.getAttribute("admin_course"));
        model.addAttribute("title","Add New Student");
        model.addAttribute("formAction","/save-student");
        return "faculty-links/student-form"; // ✅ new Thymeleaf template
    }


    //student update and delete will be managed by faculty only
    // Open form to update student
    @GetMapping("/update/{id}")
    public String editStudentForm(@PathVariable() Long id, Model model,HttpSession session) {
    	  String role = (String) session.getAttribute("role");
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

        // ✅ calculate age from dob
        if (student.getDob() != null) {
            LocalDate birthDate = student.getDob().toLocalDate();
            int calculatedAge = java.time.Period.between(birthDate, LocalDate.now()).getYears();
            student.setAge(calculatedAge);
        }

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


    // Open form to add student
    @GetMapping("/faculty/add-student")
    public String showStudentFormfaculty(Model model ,HttpSession session) {
        model.addAttribute("student", new Student()); // empty student for new entry
        model.addAttribute("courses", courseServ.getAllcourses());
        String admin_course= (String) session.getAttribute("admin_course");
        model.addAttribute("admin_course",admin_course);
        model.addAttribute("title","Add New Student");
        model.addAttribute("formAction","/faculty/save-student");
        return "faculty-links/student-form"; // ✅ new Thymeleaf template
    }


    //student update and delete will be managed by faculty only
    // Open form to update student
    @GetMapping("/faculty/update/{id}")
    public String editStudentFormfaculty(@PathVariable() Long id, Model model,HttpSession session) {
    	  String role = (String) session.getAttribute("role");
        Student student = studserv.getStudentById(id); // fetch student by ID
        model.addAttribute("student", student);
        model.addAttribute("title", "Update Student");
        model.addAttribute("courses", courseServ.getAllcourses());
        String admin_course= (String) session.getAttribute("admin_course");
        model.addAttribute("admin_course",admin_course);
        model.addAttribute("formAction","/faculty/save-student");
        return "faculty-links/student-form"; // same form but prefilled
    }

    // Save or update student
    @PostMapping("/faculty/save-student")
    public String saveStudentsfaculty(
            @ModelAttribute Student student,
            @RequestParam("courseName") String courseName,
            RedirectAttributes redirectAttributes) {

        // Set password as mobile number
        long pass = student.getMobile();
        student.setPassword(String.valueOf(pass));

        // ✅ Calculate age
        if (student.getDob() != null) {
            LocalDate birthDate = student.getDob().toLocalDate();
            int calculatedAge = java.time.Period.between(birthDate, LocalDate.now()).getYears();
            student.setAge(calculatedAge);
        }

        // ✅ Attach managed Course from DB
        Course course = courseServ.findByName(courseName);  // You must add findByName in repo/service
        student.setCourse(course);

        // ✅ Handle Login relation
        Student existing = (student.getId() != 0) ? studserv.getStudentById(student.getId()) : null;

        Login login;
        if (existing != null && existing.getLogin() != null) {
            login = existing.getLogin();
        } else {
            login = new Login();
            login.setRole("student");
        }

        login.setName(student.getName());
        login.setPassword(student.getPassword());
        login.setEmail(student.getEmail());

        login.setStudent(student);
        student.setLogin(login);

        studserv.saveStudent(student);

        redirectAttributes.addFlashAttribute("message", "Student saved successfully!");
        return "redirect:/student-list";
    }


    // Delete student
    @PostMapping("/faculty/delete")
    public String deleteStudentfaculty(@ModelAttribute Student student, RedirectAttributes redirectAttributes) {

        studserv.deleteStudent(student);



        redirectAttributes.addFlashAttribute("message", "Student deleted successfully!");
        return "redirect:/student-list";
    }


    //*************************************************************
    //*************************************************************
    //student registration

    // Open form to add student
    @GetMapping("/reg-add-student")
    public String regshowStudentForm(Model model,HttpSession session) {
        model.addAttribute("student", new Student()); // empty student for new entry
        model.addAttribute("courses", courseServ.getAllcourses());

         model.addAttribute("admin_course", session.getAttribute("admin_course"));
        model.addAttribute("title","Add New Student");
        model.addAttribute("formAction","/reg-save-student");
        return "faculty-links/student-form"; // ✅ new Thymeleaf template
    }


    //student update and delete will be managed by faculty only
    // Open form to update student
    @GetMapping("/reg-update/{id}")
    public String regeditStudentForm(@PathVariable() Long id, Model model,HttpSession session) {
    	  String role = (String) session.getAttribute("role");
        Student student = studserv.getStudentById(id); // fetch student by ID
        model.addAttribute("student", student);
        model.addAttribute("title", "Update Student");
        model.addAttribute("courses", courseServ.getAllcourses());
        model.addAttribute("formAction","/reg-save-student");
        return "admin-links/student-form"; // same form but prefilled
    }

    // Save or update student
    @PostMapping("/reg-save-student")
    public String regsaveStudents(@ModelAttribute Student student, RedirectAttributes redirectAttributes) {
        long pass = student.getMobile();
        student.setPassword(String.valueOf(pass));

        // ✅ calculate age from dob
        if (student.getDob() != null) {
            LocalDate birthDate = student.getDob().toLocalDate();
            int calculatedAge = java.time.Period.between(birthDate, LocalDate.now()).getYears();
            student.setAge(calculatedAge);
        }

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

        emailService.sendRegistrationMail(student.getEmail(), student.getName());

        redirectAttributes.addFlashAttribute("message", "Student saved successfully!You can login now 😊");
        return "redirect:/index";
    }

    // Delete student
    @PostMapping("/reg-delete")
    public String regdeleteStudent(@ModelAttribute Student student, RedirectAttributes redirectAttributes) {

        studserv.deleteStudent(student);



        redirectAttributes.addFlashAttribute("message", "Student deleted successfully!");
        return "redirect:/student-management";
    }

}
