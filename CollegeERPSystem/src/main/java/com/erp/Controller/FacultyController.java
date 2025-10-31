package com.erp.Controller;

import java.time.LocalDate;
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
import com.erp.utilities.EmailService;

@Controller
public class FacultyController {



    @Autowired
    private FacultyService facultyServ;

  @Autowired
  private courseService courseServ;
  @Autowired
  private EmailService emailService;
  
    // Student management - only list of students
    @GetMapping("/faculty-management")
    public String showFaculties(Model model) {


        // ✅ Today’s date
        LocalDate today = LocalDate.now();
        model.addAttribute("today", today.toString());

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

    // Open form to update Faculty
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
    public String saveFaculty(@ModelAttribute Faculty faculty, RedirectAttributes redirectAttributes) {
        // Set password as mobile number
        long pass = faculty.getMobile();
        faculty.setPassword(String.valueOf(pass));

        // ✅ Calculate age from DOB
        if (faculty.getDob() != null) {
            LocalDate birthDate = faculty.getDob().toLocalDate();
            int calculatedAge = java.time.Period.between(birthDate, LocalDate.now()).getYears();
            faculty.setAge(calculatedAge);
        }

        // ✅ Check if course already assigned to another faculty
        Faculty assignedFaculty = facultyServ.getFacultyByCourse(faculty.getCourse());
        if (assignedFaculty != null && (faculty.getId() == 0 || assignedFaculty.getId() != faculty.getId())) {
            redirectAttributes.addFlashAttribute("error", "Course is already assigned to another faculty! Please use another course or add a new Course.");
            return "redirect:/add-faculty"; // back without saving
        }

        Faculty existing = null;
        if (faculty.getId() != 0) { // Update case
            existing = facultyServ.getFacultyById(faculty.getId());
        }

        Login login;
        if (existing != null && existing.getLogin() != null) {
            // ✅ Reuse existing login for updates
            login = existing.getLogin();
        } else {
            // ✅ Create new login for insert case
            login = new Login();
            login.setRole("faculty");
        }

        // Update login fields from faculty
        login.setName(faculty.getName());
        login.setPassword(faculty.getPassword());
        login.setEmail(faculty.getEmail());

        // Maintain both sides of the relation
        login.setFaculty(faculty);
        faculty.setLogin(login);

        // ✅ Save faculty
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

    //registration controllers
    // Open form to add course
    @GetMapping("/reg-add-faculty")
    public String regshowFacultyForm(Model model) {
        model.addAttribute("faculty", new Faculty()); // empty faculty for new entry
        model.addAttribute("courses", courseServ.getAllcourses());
        model.addAttribute("title","Add New Faculty");
        model.addAttribute("formAction","/reg-save-faculty");
        return "/admin-links/faculty-form"; // ✅ new Thymeleaf template
    }

    // Save or update faculty
    @PostMapping("/reg-save-faculty")
    public String regsaveFaculty(@ModelAttribute Faculty faculty, RedirectAttributes redirectAttributes) {
        // Set password as mobile number
        long pass = faculty.getMobile();
        faculty.setPassword(String.valueOf(pass));

        // ✅ Calculate age from DOB
        if (faculty.getDob() != null) {
            LocalDate birthDate = faculty.getDob().toLocalDate();
            int calculatedAge = java.time.Period.between(birthDate, LocalDate.now()).getYears();
            faculty.setAge(calculatedAge);
        }

        // ✅ Check if course already assigned to another faculty
        Faculty assignedFaculty = facultyServ.getFacultyByCourse(faculty.getCourse());
        if (assignedFaculty != null && (faculty.getId() == 0 || assignedFaculty.getId() != faculty.getId())) {
            redirectAttributes.addFlashAttribute("error", "Course is already assigned to another faculty! Please use another course or add a new Course.");
            return "redirect:/faculty-registration"; // back without saving
        }

        Faculty existing = null;
        if (faculty.getId() != 0) { // Update case
            existing = facultyServ.getFacultyById(faculty.getId());
        }

        Login login;
        if (existing != null && existing.getLogin() != null) {
            // ✅ Reuse existing login for updates
            login = existing.getLogin();
        } else {
            // ✅ Create new login for insert case
            login = new Login();
            login.setRole("faculty");
        }

        // Update login fields from faculty
        login.setName(faculty.getName());
        login.setPassword(faculty.getPassword());
        login.setEmail(faculty.getEmail());

        // Maintain both sides of the relation
        login.setFaculty(faculty);
        faculty.setLogin(login);

        // ✅ Save faculty
        facultyServ.savefFaculty(faculty);

        emailService.sendRegistrationMail(faculty.getEmail(), faculty.getName());
        
        redirectAttributes.addFlashAttribute("message", "Faculty saved successfully!You can login now 😊");
        return "redirect:/index";
    }
}
