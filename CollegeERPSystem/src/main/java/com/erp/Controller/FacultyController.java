package com.erp.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.erp.model.Faculty;
import com.erp.model.Login;
import com.erp.model.Student;
import com.erp.services.FacultyService;
import com.erp.services.courseService;

@Controller
public class FacultyController {

   

    @Autowired
    private FacultyService facultyServ;

  @Autowired
  private courseService courseServ;
    // Student management - only list of students
    @GetMapping("/faculty-management")
    public String showFaculties(Model model) {
    	
      
        List<Faculty> faculties= facultyServ.getAllfaculties();
        model.addAttribute("faculties", faculties);

        return "admin-links/faculty-management"; // ✅ table page only
    }

    // Open form to add course
    @GetMapping("/add-faculty")
    public String showFacultyForm(Model model) {
        model.addAttribute("faculty", new Faculty()); // empty faculty for new entry
        model.addAttribute("courses", courseServ.getAllcourses());
        model.addAttribute("title","Add New Faculty");
        model.addAttribute("formAction","/save-faculty");
        return "/admin-links/faculty-form"; // ✅ new Thymeleaf template
    }

    // Open form to update student
    @GetMapping("/update-faculty/{id}")
    public String editfacultyForm(@PathVariable() Long id, Model model) {
        Faculty faculty = facultyServ.getFacultyById(id); // fetch student by ID
        model.addAttribute("faculty", faculty);
        model.addAttribute("title", "Update faculty");
       model.addAttribute("courses", courseServ.getAllcourses());
        model.addAttribute("formAction","/save-faculty");
        return "/admin-links//faculty-form"; // same form but prefilled
    }

    // Save or update student
    @PostMapping("/save-faculty")
    public String saveStudents(@ModelAttribute Faculty faculty, RedirectAttributes redirectAttributes) {
        long pass = faculty.getMobile();
        faculty.setPassword(String.valueOf(pass));

        // ✅ Check if course already assigned to another faculty
        Faculty assignedFaculty = facultyServ.getFacultyByCourse(faculty.getCourse());
        if (assignedFaculty != null && (faculty.getId() == 0 || assignedFaculty.getId() != faculty.getId())) {
            redirectAttributes.addFlashAttribute("error", "Course is already assigned to another faculty!");
            return "redirect:/faculty-management"; // back without saving
        }

        Faculty existing = null;
        if (faculty.getId() != 0) { // means update
            existing = facultyServ.getFacultyById(faculty.getId());
        }

        Login login;
        if (existing != null && existing.getLogin() != null) {
            // ✅ Reuse existing login
            login = existing.getLogin();
        } else {
            // ✅ Create new login (insert case)
            login = new Login();
            login.setRole("faculty");
        }

        // Update login fields from faculty
        login.setName(faculty.getName());
        login.setPassword(faculty.getPassword());
        login.setEmail(faculty.getEmail());

        // Maintain both sides of relation
        login.setFaculty(faculty);
        faculty.setLogin(login);

        // ✅ Save only if no conflict
        facultyServ.savefFaculty(faculty);

        redirectAttributes.addFlashAttribute("message", "Faculty saved successfully!");
        return "redirect:/faculty-management";
    }

    // Delete student
    @PostMapping("/delete-faculty")
    public String deleteFaculty(@ModelAttribute Faculty faculty, RedirectAttributes redirectAttributes) {
    	facultyServ.deleteFaculty(faculty);
        redirectAttributes.addFlashAttribute("message", "faculty deleted successfully!");
        return "redirect:/faculty-management";
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
