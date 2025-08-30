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
import com.erp.model.Student;
import com.erp.services.StudentService;
import com.erp.services.courseService;

@Controller
public class Coursecontroller {

    @Autowired
    private StudentService studserv;

    @Autowired
    private courseService courseServ;

  
    // Student management - only list of students
    @GetMapping("/course-management")
    public String showStudents(Model model) {
    	
      
        List<Course> courses= courseServ.getAllcourses();
        model.addAttribute("courses", courses);

        return "admin-links/course-management"; // ✅ table page only
    }

    // Open form to add course
    @GetMapping("/add-course")
    public String showStudentForm(Model model) {
        model.addAttribute("course", new Course()); // empty student for new entry
       // model.addAttribute("courses", courseServ.getAllcourses());
        model.addAttribute("title","Add New Course");
        model.addAttribute("formAction","/save-course");
        return "/admin-links/course-form"; // ✅ new Thymeleaf template
    }

    // Open form to update student
    @GetMapping("/update-course/{id}")
    public String editCourseForm(@PathVariable() Long id, Model model) {
        Course course = courseServ.getCourseById(id); // fetch student by ID
        model.addAttribute("course", course);
        model.addAttribute("title", "Update Course");
       // model.addAttribute("courses", courseServ.getAllcourses());
        model.addAttribute("formAction","/save-course");
        return "/admin-links//course-form"; // same form but prefilled
    }

    // Save or update student
    @PostMapping("/save-course")
    public String saveStudents(@ModelAttribute Course course, RedirectAttributes redirectAttributes) {
        courseServ.savecourse(course);
        redirectAttributes.addFlashAttribute("message", "Course added successfully!");
        return "redirect:/course-management";
    }

    // Delete student
 // Delete course
    @PostMapping("/delete-course")
    public String deleteCourse(@ModelAttribute Course course, RedirectAttributes redirectAttributes) {
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
