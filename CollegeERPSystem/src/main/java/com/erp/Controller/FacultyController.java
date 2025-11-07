package com.erp.Controller;

import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpSession;
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
import com.erp.services.FacultyService;
import com.erp.services.courseService;

@Controller
public class FacultyController {

    @Autowired
    private FacultyService facultyServ;

    @Autowired
    private courseService courseServ;


    // ✅ ADMIN ONLY — List all faculty
    @GetMapping("/faculty-management")
    public String showFaculties(Model model, HttpSession session) {

        if (!isAdmin(session)) return "redirect:/index";

        model.addAttribute("today", LocalDate.now().toString());
        model.addAttribute("faculties", facultyServ.getAllfaculties());

        return "admin-links/faculty-management";
    }


    // ✅ ADMIN ONLY — Add faculty page
    @GetMapping("/add-faculty")
    public String showFacultyForm(Model model, HttpSession session) {

        if (!isAdmin(session)) return "redirect:/index";

        model.addAttribute("faculty", new Faculty());
        model.addAttribute("courses", courseServ.getAllcourses());
        model.addAttribute("title", "Add New Faculty");
        model.addAttribute("formAction", "/save-faculty");

        return "/admin-links/faculty-form";
    }


    // ✅ ADMIN ONLY — Update faculty page
    @GetMapping("/update-faculty/{id}")
    public String editfacultyForm(@PathVariable Long id, Model model, HttpSession session) {

        if (!isAdmin(session)) return "redirect:/index";

        Faculty faculty = facultyServ.getFacultyById(id);

        model.addAttribute("faculty", faculty);
        model.addAttribute("title", "Update faculty");
        model.addAttribute("courses", courseServ.getAllcourses());
        model.addAttribute("formAction", "/save-faculty");

        return "/admin-links/faculty-form";
    }


    // ✅ ADMIN ONLY — Save or update faculty
    @PostMapping("/save-faculty")
    public String saveFaculty(@ModelAttribute Faculty faculty,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {

        if (!isAdmin(session)) return "redirect:/index";

        long pass = faculty.getMobile();
        faculty.setPassword(String.valueOf(pass));

        if (faculty.getDob() != null) {
            LocalDate birthDate = faculty.getDob().toLocalDate();
            faculty.setAge(java.time.Period.between(birthDate, LocalDate.now()).getYears());
        }

        Faculty assignedFaculty = facultyServ.getFacultyByCourse(faculty.getCourse());
        if (assignedFaculty != null &&
            (faculty.getId() == 0 || assignedFaculty.getId() != faculty.getId())) {

            redirectAttributes.addFlashAttribute("error",
                "Course is already assigned to another faculty! Please use another course.");
            return "redirect:/add-faculty";
        }

        Faculty existing = (faculty.getId() != 0)
                ? facultyServ.getFacultyById(faculty.getId())
                : null;

        Login login;

        if (existing != null && existing.getLogin() != null) {
            login = existing.getLogin();
        } else {
            login = new Login();
            login.setRole("faculty");
        }

        login.setName(faculty.getName());
        login.setPassword(faculty.getPassword());
        login.setEmail(faculty.getEmail());

        login.setFaculty(faculty);
        faculty.setLogin(login);

        facultyServ.savefFaculty(faculty);

        redirectAttributes.addFlashAttribute("message", "Faculty saved successfully!");
        return "redirect:/faculty-management";
    }


    // ✅ ADMIN ONLY — Delete faculty
    @PostMapping("/delete-faculty")
    public String deleteFaculty(@ModelAttribute Faculty faculty,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {

        if (!isAdmin(session)) return "redirect:/index";

        facultyServ.deleteFaculty(faculty);
        redirectAttributes.addFlashAttribute("message", "Faculty deleted successfully!");
        return "redirect:/faculty-management";
    }


    // ✅ PUBLIC (NO LOGIN REQUIRED) — Faculty Registration Page
    @GetMapping("/reg-add-faculty")
    public String regshowFacultyForm(Model model) {

        model.addAttribute("faculty", new Faculty());
        model.addAttribute("courses", courseServ.getAllcourses());
        model.addAttribute("title", "Add New Faculty");
        model.addAttribute("formAction", "/reg-save-faculty");

        return "/admin-links/faculty-form";
    }


    // ✅ PUBLIC — Save faculty registration
    @PostMapping("/reg-save-faculty")
    public String regsaveFaculty(@ModelAttribute Faculty faculty,
                                 RedirectAttributes redirectAttributes) {

        long pass = faculty.getMobile();
        faculty.setPassword(String.valueOf(pass));

        if (faculty.getDob() != null) {
            LocalDate birthDate = faculty.getDob().toLocalDate();
            faculty.setAge(java.time.Period.between(birthDate, LocalDate.now()).getYears());
        }

        Faculty assignedFaculty = facultyServ.getFacultyByCourse(faculty.getCourse());
        if (assignedFaculty != null &&
            (faculty.getId() == 0 || assignedFaculty.getId() != faculty.getId())) {

            redirectAttributes.addFlashAttribute("error",
                "Course is already assigned to another faculty!");
            return "redirect:/faculty-registration";
        }

        Faculty existing = (faculty.getId() != 0)
                ? facultyServ.getFacultyById(faculty.getId())
                : null;

        Login login;

        if (existing != null && existing.getLogin() != null) {
            login = existing.getLogin();
        } else {
            login = new Login();
            login.setRole("faculty");
        }

        login.setName(faculty.getName());
        login.setPassword(faculty.getPassword());
        login.setEmail(faculty.getEmail());

        login.setFaculty(faculty);
        faculty.setLogin(login);

        facultyServ.savefFaculty(faculty);

        redirectAttributes.addFlashAttribute("message", "Faculty saved successfully! You can login now 😊");
        return "redirect:/index";
    }


    // ✅ HELPER: Admin check
    private boolean isAdmin(HttpSession session) {
        if (session == null) return false;
        Object user = session.getAttribute("loggedInUser");
        String role = (String) session.getAttribute("role");
        return user != null && "admin".equals(role);
    }
}
