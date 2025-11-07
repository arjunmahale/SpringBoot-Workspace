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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.erp.model.Course;
import com.erp.services.StudentService;
import com.erp.services.courseService;

@Controller
public class Coursecontroller {

    @Autowired
    private StudentService studserv;

    @Autowired
    private courseService courseServ;

    // ✅ Only admin allowed — session protected
    @GetMapping("/course-management")
    public String showStudents(Model model, HttpSession session) {

        // ✅ Route protection
        if (!isAdmin(session)) return "redirect:/index";

        LocalDate today = LocalDate.now();
        model.addAttribute("today", today.toString());

        List<Course> courses = courseServ.getAllcourses();
        model.addAttribute("courses", courses);

        return "admin-links/course-management";
    }

    // ✅ Admin only
    @GetMapping("/add-course")
    public String showCourseForm(Model model, HttpSession session) {

        if (!isAdmin(session)) return "redirect:/index";

        model.addAttribute("course", new Course());
        model.addAttribute("title", "Add New Course");
        model.addAttribute("formAction", "/save-course");
        return "admin-links/course-form";
    }

    // ✅ Admin only
    @GetMapping("/update-course/{id}")
    public String editCourseForm(@PathVariable Long id, Model model, HttpSession session) {

        if (!isAdmin(session)) return "redirect:/index";

        Course course = courseServ.getCourseById(id);
        model.addAttribute("course", course);
        model.addAttribute("title", "Update Course");
        model.addAttribute("formAction", "/save-course");
        return "admin-links/course-form";
    }

    // ✅ Admin only
    @PostMapping("/save-course")
    public String saveCourse(@ModelAttribute Course course,
                             RedirectAttributes redirectAttributes,
                             HttpSession session) {

        if (!isAdmin(session)) return "redirect:/index";

        courseServ.savecourse(course);
        redirectAttributes.addFlashAttribute("message", "✅ Course saved successfully!");
        return "redirect:/course-management";
    }

    // ✅ Admin only
    @PostMapping("/delete-course")
    public String deleteCourse(@RequestParam("id") Course course,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {

        if (!isAdmin(session)) return "redirect:/index";

        try {
            courseServ.deletecourse(course);
            redirectAttributes.addFlashAttribute("message", "✅ Course deleted successfully!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "⚠️ Something went wrong while deleting course!");
        }
        return "redirect:/course-management";
    }

    // ✅ Helper method: Admin check
    private boolean isAdmin(HttpSession session) {
        if (session == null) return false;
        Object user = session.getAttribute("loggedInUser");
        Object role = session.getAttribute("role");
        return (user != null && "admin".equals(role));
    }
}
