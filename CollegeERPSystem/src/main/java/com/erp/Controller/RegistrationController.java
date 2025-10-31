package com.erp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.erp.model.Faculty;
import com.erp.model.Student;
import com.erp.services.courseService;

@Controller

public class RegistrationController {
	@GetMapping("/index")
    public String showIndexPage() {
        return "index"; // this will load src/main/resources/templates/index.html
    }

	@Autowired
	private courseService courseServ;
    @GetMapping("/student-registration")
    public String studentForm(Model model) {
    	model.addAttribute("student", new Student());
    	model.addAttribute("courses", courseServ.getAllcourses());
    	model.addAttribute("formAction","/reg-save-student");
        return "student-registration"; // Thymeleaf will look for templates/student-registration.html
    }

    @GetMapping("/faculty-registration")
    public String facultyForm(Model model) {
    	 model.addAttribute("faculty", new Faculty()); // empty faculty for new entry
         model.addAttribute("courses", courseServ.getAllcourses());
         model.addAttribute("formAction","/reg-save-faculty");
        return "faculty-registration";
    }

    @GetMapping("/admin")
    public String adminForm() {
        return "admin-registration";
    }
}
