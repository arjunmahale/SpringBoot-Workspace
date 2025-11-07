package com.erp.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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


    // ✅ LOGIN PROCESS (CORRECTED)
    @PostMapping("/login")
    public String login(
            @ModelAttribute Login login,
            HttpServletRequest request,
            Model model) {

        // ✅ Fetch user
//        Login dbUser = loginServ.getUserByName(login.getName());
        Login dbUser = loginServ.getUserByEmail(login.getName());
        if (dbUser == null) {
            model.addAttribute("name", login.getName());
            model.addAttribute("error", "is not found");
            return "index";
        }

        if (!login.getPassword().equals(dbUser.getPassword())) {
            model.addAttribute("error", "password is wrong");
            return "index";
        }

        if (!login.getRole().equals(dbUser.getRole())) {
            model.addAttribute("error", "role is wrong");
            return "index";
        }

        // ✅ Destroy old session safely
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        // ✅ Create clean new session
        HttpSession session = request.getSession(true);

        // ✅ Store required session attributes
        session.setAttribute("loggedInUser", dbUser);
        session.setAttribute("user", dbUser.getName());
        session.setAttribute("role", dbUser.getRole());


        // ✅ ADMIN LOGIN
        if ("admin".equals(dbUser.getRole())) {

            long totalStudents = studentService.countStudents();
            long totalCourses = courseService.countCourses();
            long totalFaculty = facultyService.countFaculty();

            model.addAttribute("totalStudents", totalStudents);
            model.addAttribute("totalCourses", totalCourses);
            model.addAttribute("totalFaculty", totalFaculty);
            model.addAttribute("today", LocalDate.now().toString());
            model.addAttribute("students", studentService.getRecentStudents());
            model.addAttribute("courses", courseService.getAllcourses());
            model.addAttribute("faculties", facultyService.getAllfaculties());

            return "redirect:/admin-dashboard";
        }


        // ✅ FACULTY LOGIN
        if ("faculty".equals(dbUser.getRole())) {

            String course = dbUser.getFaculty().getCourse().getName();
            session.setAttribute("admin_course", course);

            List<Student> all = studentService.getAllStudent();
            List<Student> filtered = new ArrayList<>();

            for (Student stu : all) {
                if (stu.getCourse() != null &&
                    course.equalsIgnoreCase(stu.getCourse().getName())) {
                    filtered.add(stu);
                }
            }

            model.addAttribute("totalStudents", filtered.size());
            model.addAttribute("students", filtered);
            model.addAttribute("today", LocalDate.now().toString());
            model.addAttribute("user", dbUser.getName());

            return "redirect:/faculty-dashboard";
        }


        // ✅ STUDENT LOGIN
        if ("student".equals(dbUser.getRole())) {

            session.setAttribute("student", dbUser.getStudent());
            model.addAttribute("today", LocalDate.now().toString());

            return "redirect:/student-dashboard";
        }

        model.addAttribute("error", "Something went wrong");
        return "index";
    }


    // ✅ PROTECTED PAGE
    @GetMapping("/register-student")
    public String Registration(Model model, HttpSession session) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/index";
        }

        model.addAttribute("student", new Student());
        model.addAttribute("faculty", new Faculty());

        return "registration";
    }
}
