package com.erp.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.erp.model.Course;
import com.erp.model.Login;
import com.erp.model.Student;
import com.erp.services.LoginService;
import com.erp.services.StudentService;
import com.erp.services.courseService;

@Controller
public class ErpController {

    @Autowired
    private StudentService studserv;

    @Autowired
    private courseService courseServ;

    @Autowired
    private LoginService loginServ;
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

    // Open form to add student
    @GetMapping("/add-student")
    public String showStudentForm(Model model) {
        model.addAttribute("student", new Student()); // empty student for new entry
        model.addAttribute("courses", courseServ.getAllcourses());
        model.addAttribute("title","Add New Student");
        model.addAttribute("formAction","/save-student");
        return "admin-links/student-form"; // ✅ new Thymeleaf template
    }

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
