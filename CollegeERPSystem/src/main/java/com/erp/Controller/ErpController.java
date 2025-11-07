package com.erp.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
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

    // ✅ Home page
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // ✅ ADMIN - Student management
    @GetMapping("/student-management")
    public String showStudents(Model model, HttpSession session) {

        if (!isAdmin(session)) return "redirect:/index";

        LocalDate today = LocalDate.now();
        model.addAttribute("today", today.toString());

        List<Student> students = studserv.getAllStudent();
        model.addAttribute("students", students);

        return "admin-links/student-management";
    }

    // ✅ ADMIN Dashboard
    @GetMapping("/admin-dashboard")
    public String showadminDashboard(Model model, HttpSession session) {

        if (!isAdmin(session)) return "redirect:/index";

        long totalStudents = studserv.countStudents();
        long totalCourses = courseServ.countCourses();
        long totalFaculty = facultyService1.countFaculty();

        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("totalCourses", totalCourses);
        model.addAttribute("totalFaculty", totalFaculty);

        model.addAttribute("today", LocalDate.now().toString());
        model.addAttribute("students", studserv.getRecentStudents());
        model.addAttribute("courses", courseServ.getAllcourses());
        model.addAttribute("faculties", facultyService1.getAllfaculties());

        return "admin-links/admin-dashboard";
    }

    // ✅ ADMIN - Attendance listing
    @GetMapping("/attendance-list")
    public String showAttendance(HttpSession session, Model model) {

        if (!isAdmin(session)) return "redirect:/index";

        String user1 = (String) session.getAttribute("user");
        model.addAttribute("user", user1);

        model.addAttribute("today", LocalDate.now().toString());
        model.addAttribute("courses", courseServ.getAllcourses());

        List<Student> allStudents = studserv.getAllStudent();
        List<Student> filteredStudents = new ArrayList<>();

        for (Student stu : allStudents) {
            if (stu.getCourse() != null && "mcs".equalsIgnoreCase(stu.getCourse().getName())) {
                filteredStudents.add(stu);
            }
        }

        Map<Long, String> attendanceMap = new HashMap<>();
        List<Attendance> todayAttendance = attendanceService.getByDate(LocalDate.now());

        for (Attendance att : todayAttendance) {
            if (att.getStudent().getCourse() != null &&
                    "mcs".equalsIgnoreCase(att.getStudent().getCourse().getName())) {
                attendanceMap.put(att.getStudent().getId(), att.getStatus());
            }
        }

        model.addAttribute("students", filteredStudents);
        model.addAttribute("attendanceMap", attendanceMap);
        model.addAttribute("totalStudents", filteredStudents.size());

        return "admin-links/attendance-list";
    }

    // ✅ ADMIN - Attendance by date & course
    @GetMapping("/attendance-list-by-date-course")
    public String getAttendanceByCourseAndDate(@RequestParam("course") String courseName,
                                               @RequestParam("date") LocalDate date,
                                               HttpSession session,
                                               Model model) {

        if (!isAdmin(session)) return "redirect:/index";

        model.addAttribute("courses", courseServ.getAllcourses());
        model.addAttribute("selectedDate", date.toString());
        model.addAttribute("selectedCourse", courseName);
        model.addAttribute("user", session.getAttribute("user"));

        List<Student> studentsByCourse = studserv.getAllStudent().stream()
                .filter(stu -> stu.getCourse() != null &&
                        courseName.equalsIgnoreCase(stu.getCourse().getName()))
                .toList();

        Map<Long, String> attendanceMap = new HashMap<>();
        List<Attendance> attendanceList = attendanceService.getByCourseAndDate(courseName, date);

        for (Attendance att : attendanceList) {
            if (att.getStudent().getCourse() != null &&
                    courseName.equalsIgnoreCase(att.getStudent().getCourse().getName())) {
                attendanceMap.put(att.getStudent().getId(), att.getStatus());
            }
        }

        model.addAttribute("today", LocalDate.now().toString());
        model.addAttribute("students", studentsByCourse);
        model.addAttribute("attendanceMap", attendanceMap);
        model.addAttribute("totalStudents", studentsByCourse.size());

        return "admin-links/attendance-list";
    }

    // ✅ ADMIN - Total attendance list
    @GetMapping("/total-attendance-list")
    public String showAttendanceAllTime(HttpSession session, Model model) {

        if (!isAdmin(session)) return "redirect:/index";

        LocalDate today = LocalDate.now();
        model.addAttribute("today", today.toString());
        model.addAttribute("user", session.getAttribute("user"));

        List<Student> all = studserv.getAllStudent();

        Map<Long, String> attendanceMap = new HashMap<>();
        List<Attendance> todayAttendance = attendanceService.getByDate(today);

        for (Attendance att : todayAttendance) {
            if (att.getStudent().getCourse() != null &&
                    "mcs".equalsIgnoreCase(att.getStudent().getCourse().getName())) {
                attendanceMap.put(att.getStudent().getId(), att.getStatus());
            }
        }

        model.addAttribute("students", all);
        model.addAttribute("attendanceMap", attendanceMap);
        model.addAttribute("totalStudents", all.size());

        return "admin-links/total-attendance-list";
    }

    // ✅ FACULTY - Add student form
    @GetMapping("/add-student")
    public String showStudentForm(Model model, HttpSession session) {

        if (!isFaculty(session)) return "redirect:/index";

        model.addAttribute("student", new Student());
        model.addAttribute("courses", courseServ.getAllcourses());
        model.addAttribute("admin_course", session.getAttribute("admin_course"));
        model.addAttribute("title", "Add New Student");
        model.addAttribute("formAction", "/save-student");
        return "faculty-links/student-form";
    }

    // ✅ ADMIN or FACULTY - Update student
    @GetMapping("/update/{id}")
    public String editStudentForm(@PathVariable Long id, Model model, HttpSession session) {

        if (!isAdminOrFaculty(session)) return "redirect:/index";

        Student student = studserv.getStudentById(id);
        model.addAttribute("student", student);
        model.addAttribute("title", "Update Student");
        model.addAttribute("courses", courseServ.getAllcourses());
        model.addAttribute("formAction", "/save-student");

        return "admin-links/student-form";
    }

    // ✅ ADMIN - Save student
    @PostMapping("/save-student")
    public String saveStudents(@ModelAttribute Student student,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {

        if (!isAdmin(session)) return "redirect:/index";

        long pass = student.getMobile();
        student.setPassword(String.valueOf(pass));

        if (student.getDob() != null) {
            LocalDate birthDate = student.getDob().toLocalDate();
            student.setAge(java.time.Period.between(birthDate, LocalDate.now()).getYears());
        }

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
        return "redirect:/student-management";
    }

    // ✅ ADMIN - Delete student
    @PostMapping("/delete")
    public String deleteStudent(@ModelAttribute Student student,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {

        if (!isAdmin(session)) return "redirect:/index";

        studserv.deleteStudent(student);

        redirectAttributes.addFlashAttribute("message", "Student deleted successfully!");
        return "redirect:/student-management";
    }

    // ✅ STUDENT self-registration
    @GetMapping("/reg-add-student")
    public String regshowStudentForm(Model model, HttpSession session) {

        // ✅ Registration allowed without login
        model.addAttribute("student", new Student());
        model.addAttribute("courses", courseServ.getAllcourses());
        model.addAttribute("title", "Add New Student");
        model.addAttribute("formAction", "/reg-save-student");
        return "faculty-links/student-form";
    }

    @PostMapping("/reg-save-student")
    public String regsaveStudents(@ModelAttribute Student student,
                                  RedirectAttributes redirectAttributes) {

        long pass = student.getMobile();
        student.setPassword(String.valueOf(pass));

        if (student.getDob() != null) {
            LocalDate birthDate = student.getDob().toLocalDate();
            student.setAge(java.time.Period.between(birthDate, LocalDate.now()).getYears());
        }

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

        emailService.sendRegistrationMail(student.getEmail(), student.getName());

        redirectAttributes.addFlashAttribute("message", "Student saved successfully! You can login now 😊");
        return "redirect:/index";
    }

    // ✅ Delete student (registration controller)
    @PostMapping("/reg-delete")
    public String regdeleteStudent(@ModelAttribute Student student,
                                   RedirectAttributes redirectAttributes,
                                   HttpSession session) {

        if (!isAdmin(session)) return "redirect:/index";

        studserv.deleteStudent(student);

        redirectAttributes.addFlashAttribute("message", "Student deleted successfully!");
        return "redirect:/student-management";
    }

    // ✅ ROLE CHECK FUNCTIONS
    private boolean isAdmin(HttpSession session) {
        return session != null &&
                session.getAttribute("loggedInUser") != null &&
                "admin".equals(session.getAttribute("role"));
    }

    private boolean isFaculty(HttpSession session) {
        return session != null &&
                session.getAttribute("loggedInUser") != null &&
                "faculty".equals(session.getAttribute("role"));
    }

    private boolean isAdminOrFaculty(HttpSession session) {
        if (session == null) return false;
        String role = (String) session.getAttribute("role");
        return session.getAttribute("loggedInUser") != null &&
                ("admin".equals(role) || "faculty".equals(role));
    }
}
