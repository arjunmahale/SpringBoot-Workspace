package com.erp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.erp.model.Faculty;
import com.erp.model.Student;
import com.erp.services.courseService;

import jakarta.servlet.http.HttpSession;

@Controller
public class RegistrationController {

    @GetMapping("/index")
    public String showIndexPage() {
        return "index";
    }

    @Autowired
    private courseService courseServ;

    // ✅ PUBLIC — student registration page
    @GetMapping("/student-registration")
    public String studentForm(Model model) {

        model.addAttribute("student", new Student());
        model.addAttribute("courses", courseServ.getAllcourses());
        model.addAttribute("formAction", "/reg-save-student");

        return "student-registration";
    }

    // ✅ PUBLIC — faculty registration page
    @GetMapping("/faculty-registration")
    public String facultyForm(Model model) {

        model.addAttribute("faculty", new Faculty());
        model.addAttribute("courses", courseServ.getAllcourses());
        model.addAttribute("formAction", "/reg-save-faculty");

        return "faculty-registration";
    }

    // ✅ ADMIN ONLY — admin form page
    @GetMapping("/admin")
    public String adminForm(HttpSession session) {

        if (!isAdmin(session)) return "redirect:/index";

        return "admin-registration";
    }

    // ✅ Session helper
    private boolean isAdmin(HttpSession session) {
        if (session == null) return false;

        Object user = session.getAttribute("loggedInUser");
        String role = (String) session.getAttribute("role");

        return user != null && "admin".equals(role);
    }
}
