package com.erp.Controller;

import com.erp.model.Course;
import com.erp.model.Exam;
import com.erp.model.Student;
import com.erp.services.ExamService;
import com.erp.services.StudentService;
import com.erp.services.courseService;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ExamController {

    @Autowired
    private ExamService examService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private courseService courseService;

    // ✅ FACULTY ONLY - show exams
    @GetMapping("/exams")
    public String listExams(Model model, HttpSession session) {

        if (!isFaculty(session)) return "redirect:/index";

        model.addAttribute("exams", examService.getAllExams());
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("today", LocalDate.now().toString());

        return "faculty-links/exam-list";
    }

    // ✅ FACULTY ONLY - list marks page
    @GetMapping("/exams/marks")
    public String listExamsMarks(Model model, HttpSession session) {

        if (!isFaculty(session)) return "redirect:/index";

        model.addAttribute("exams", examService.getAllExams());
        return "faculty-links/mark-list";
    }

    // ✅ FACULTY ONLY - student list for marks entry
    @GetMapping("/exams/marks/entry-list")
    public String listExamsMarksentrylist(Model model, HttpSession session) {

        if (!isFaculty(session)) return "redirect:/index";

        String admin_course = (String) session.getAttribute("admin_course");

        model.addAttribute("user", session.getAttribute("user"));

        List<Student> all = studentService.getAllStudent();
        List<Student> filtered = new ArrayList<>();

        long cnt = 0;
        for (Student stu : all) {
            if (stu.getCourse() != null &&
                    admin_course.equalsIgnoreCase(stu.getCourse().getName())) {
                filtered.add(stu);
                cnt++;
            }
        }

        model.addAttribute("totalStudents", cnt);
        model.addAttribute("students", filtered);
        model.addAttribute("exams", examService.getAllExams());

        return "faculty-links/marks-entry-list";
    }

    // ✅ FACULTY ONLY - marks entry page
    @GetMapping("/exams/marks/entry")
    public String listExamsMarksentry(Model model,
                                      HttpSession session,
                                      @RequestParam("student_id") Long studentId,
                                      @RequestParam("student_name") String studentName) {

        if (!isFaculty(session)) return "redirect:/index";

        String admin_course = (String) session.getAttribute("admin_course");

        model.addAttribute("subjects", examService.getAllUniqueSubjects());
        model.addAttribute("student_id", studentId);
        model.addAttribute("student_name", studentName);
        model.addAttribute("user", session.getAttribute("user"));

        List<Student> all = studentService.getAllStudent();
        List<Student> filtered = new ArrayList<>();

        long cnt = 0;
        for (Student stu : all) {
            if (stu.getCourse() != null &&
                    admin_course.equalsIgnoreCase(stu.getCourse().getName())) {
                filtered.add(stu);
                cnt++;
            }
        }

        model.addAttribute("totalStudents", cnt);
        model.addAttribute("students", filtered);

        return "faculty-links/marks-entry";
    }

    // ✅ FACULTY ONLY - exam creation form
    @GetMapping("exams/new")
    public String showExamForm(HttpSession session, Model model) {

        if (!isFaculty(session)) return "redirect:/index";

        Exam exam = new Exam();
        model.addAttribute("exam", exam);

        String courseName = (String) session.getAttribute("admin_course");
        exam.setCourse(courseName);

        model.addAttribute("course", courseName);
        model.addAttribute("courses", courseService.getAllcourses());

        // ✅ Pre-select the course
        for (Course c : courseService.getAllcourses()) {
            if (c.getName().equals(courseName)) {
                model.addAttribute("selectedCourse", c);
                break;
            }
        }

        model.addAttribute("formAction", "/exams/save");
        model.addAttribute("title", "Schedule New Exam");

        return "faculty-links/exam-form";
    }

    // ✅ FACULTY ONLY - save exam
    @PostMapping("exams/save")
    public String saveExam(@ModelAttribute Exam exam,
                           RedirectAttributes redirectAttributes,
                           HttpSession session) {

        if (!isFaculty(session)) return "redirect:/index";

        boolean exists = examService.existsByCourseAndSubject(exam.getCourse(), exam.getSubject());

        if (exists) {
            redirectAttributes.addFlashAttribute("error",
                    "Exam for subject '" + exam.getSubject() +
                            "' in course '" + exam.getCourse() + "' already exists!");
            return "redirect:/exams";
        }

        examService.saveExam(exam);
        redirectAttributes.addFlashAttribute("message", "Exam scheduled successfully!");

        return "redirect:/exams";
    }

    // ✅ FACULTY ONLY - delete exam
    @GetMapping("exams/delete/{id}")
    public String deleteExam(@PathVariable Long id,
                             RedirectAttributes redirectAttributes,
                             HttpSession session) {

        if (!isFaculty(session)) return "redirect:/index";

        examService.deleteExam(id);
        redirectAttributes.addFlashAttribute("message", "Exam Deleted successfully!");

        return "redirect:/exams";
    }

    // ✅ ADMIN ONLY - admin exam listing
    @GetMapping("admin/exams")
    public String listExamsadmin(Model model, HttpSession session) {

        if (!isAdmin(session)) return "redirect:/index";

        model.addAttribute("exams", examService.getAllExams());
        model.addAttribute("today", LocalDate.now().toString());

        return "admin-links/exam-list-admin";
    }

    // ✅ ADMIN ONLY - admin exam creation form
    @GetMapping("admin/exams/new")
    public String showExamFormadmin(Model model, HttpSession session) {

        if (!isAdmin(session)) return "redirect:/index";

        List<Course> courses = courseService.getAllcourses();
        model.addAttribute("exam", new Exam());
        model.addAttribute("courses", courses);

        if (!courses.isEmpty()) {
            model.addAttribute("course", courses.get(0));
        }

        model.addAttribute("formAction", "/admin/exams/save");
        model.addAttribute("title", "Schedule New Exam");

        return "admin-links/exam-form-admin";
    }

    // ✅ ADMIN ONLY - save exam
    @PostMapping("admin/exams/save")
    public String saveExamadmin(@ModelAttribute Exam exam,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {

        if (!isAdmin(session)) return "redirect:/index";

        boolean exists = examService.existsByCourseAndSubject(exam.getCourse(), exam.getSubject());

        if (exists) {
            redirectAttributes.addFlashAttribute("error",
                    "Exam for subject <b>" + exam.getSubject() +
                            "</b> in course <b>" + exam.getCourse() + "</b> already exists!");
            return "redirect:/admin/exams";
        }

        examService.saveExam(exam);
        redirectAttributes.addFlashAttribute("message", "Exam scheduled successfully!");

        return "redirect:/admin/exams";
    }

    // ✅ ADMIN ONLY - delete exam
    @GetMapping("admin/exams/delete/{id}")
    public String deleteExamadmin(@PathVariable Long id,
                                  RedirectAttributes redirectAttributes,
                                  HttpSession session) {

        if (!isAdmin(session)) return "redirect:/index";

        examService.deleteExam(id);
        redirectAttributes.addFlashAttribute("message", "Exam Deleted successfully!");

        return "redirect:/admin/exams";
    }

    // ✅ HELPER METHODS
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
}
