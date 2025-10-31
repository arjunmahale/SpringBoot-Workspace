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

import org.hibernate.engine.jdbc.spi.TypeNullability;
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

    @GetMapping("/exams")
    public String listExams(Model model,HttpSession session) {
        model.addAttribute("exams", examService.getAllExams());

        String user= (String) session.getAttribute("user");
        if(user!=null) {
        	model.addAttribute("user",user);

        }

        // ✅ Today’s date
        LocalDate today = LocalDate.now();
        model.addAttribute("today", today.toString());


        return "faculty-links/exam-list"; // exam listing page
    }
    @GetMapping("/exams/marks")
    public String listExamsMarks(Model model) {
        model.addAttribute("exams", examService.getAllExams());
        return "faculty-links/mark-list"; // exam listing page
    }
    @GetMapping("/exams/marks/entry-list")
    public String listExamsMarksentrylist(Model model,HttpSession session) {
String user1=(String) session.getAttribute("user");



		model.addAttribute("user",user1);
		List<Student> s1 = studentService.getAllStudent();
		List<Student> s2 = new ArrayList<>();


		long cnt = 0;
		for (Student stu : s1) {
			if (stu.getCourse() != null && "mcs".equalsIgnoreCase(stu.getCourse().getName())) {
				s2.add(stu);

				cnt++;
			}
		}

		long totalStudents = cnt;
		model.addAttribute("totalStudents", totalStudents);

		model.addAttribute("students", s2);
    	model.addAttribute("exams", examService.getAllExams());
    	return "faculty-links/marks-entry-list"; // exam listing page
    }
    @GetMapping("/exams/marks/entry")
    public String listExamsMarksentry(Model model,HttpSession session, @RequestParam("student_id") Long studentId,
            @RequestParam("student_name") String studentName) {
    	String user1=(String) session.getAttribute("user");

    	 model.addAttribute("subjects", examService.getAllUniqueSubjects());
    	  model.addAttribute("student_id", studentId);
    	    model.addAttribute("student_name", studentName);

    	model.addAttribute("user",user1);
    	List<Student> s1 = studentService.getAllStudent();
    	List<Student> s2 = new ArrayList<>();


    	long cnt = 0;
    	for (Student stu : s1) {
    		if (stu.getCourse() != null && "mcs".equalsIgnoreCase(stu.getCourse().getName())) {
    			s2.add(stu);

    			cnt++;
    		}
    	}

    	long totalStudents = cnt;
    	model.addAttribute("totalStudents", totalStudents);

    	model.addAttribute("students", s2);
    //	model.addAttribute("exams", examService.getAllExams());
    	return "faculty-links/marks-entry"; // exam listing page
    }

    @GetMapping("exams/new")
    public String showExamForm(HttpSession session, Model model) {
        Exam exam = new Exam();
        model.addAttribute("exam", exam);

        // Get course from session
        String courseName = (String) session.getAttribute("admin_course");
        exam.setCourse(courseName);
        model.addAttribute("course", courseName);
        System.out.println("course name : " + courseName);

        // Populate all courses for dropdown
        List<Course> courses = courseService.getAllcourses();
        model.addAttribute("courses", courses);

        // ✅ Also resolve the Course object for subject dropdown
        if (courseName != null) {
            for (Course c : courses) {
                if (c.getName().equals(courseName)) {
                    model.addAttribute("selectedCourse", c);
                    break;
                }
            }
        }

        model.addAttribute("formAction", "/exams/save");
        model.addAttribute("title", "Schedule New Exam");

        return "faculty-links/exam-form"; // exam form page
    }


    @PostMapping("exams/save")
    public String saveExam(@ModelAttribute Exam exam, RedirectAttributes redirectAttributes) {
        // check if an exam already exists for the same course + subject
        boolean exists = examService.existsByCourseAndSubject(exam.getCourse(), exam.getSubject());

        if (exists) {
            redirectAttributes.addFlashAttribute("error",
                "Exam for subject '" + exam.getSubject() + "' in course '" + exam.getCourse() + "' already exists!");
            return "redirect:/exams";
        }

        // otherwise, save it
        examService.saveExam(exam);
        redirectAttributes.addFlashAttribute("message", "Exam scheduled successfully!");
        return "redirect:/exams";
    }


    @GetMapping("exams/delete/{id}")
    public String deleteExam(@PathVariable Long id,RedirectAttributes redirectAttributes) {
        examService.deleteExam(id);
        redirectAttributes.addFlashAttribute("message", "Exam Deleted successfully!");
        return "redirect:/exams";
    }




    //for admin exam




    @GetMapping("admin/exams")
    public String listExamsadmin(Model model) {
    	model.addAttribute("exams", examService.getAllExams());

        // ✅ Today’s date
        LocalDate today = LocalDate.now();
        model.addAttribute("today", today.toString());

    	return "admin-links/exam-list-admin"; // exam listing page

    }

    @GetMapping("admin/exams/new")
    public String showExamFormadmin(Model model) {
        List<Course> courses = courseService.getAllcourses();
        model.addAttribute("exam", new Exam());
        model.addAttribute("courses", courses);

        // ✅ Pick first course for initial subjects
        if (!courses.isEmpty()) {
            model.addAttribute("course", courses.get(0));
        }

        model.addAttribute("formAction", "/admin/exams/save");
        model.addAttribute("title", "Schedule New Exam");
        return "admin-links/exam-form-admin";
    }


    @PostMapping("admin/exams/save")
    public String saveExamadmin(@ModelAttribute Exam exam, RedirectAttributes redirectAttributes) {
        // check if exam already exists for same course + subject
        boolean exists = examService.existsByCourseAndSubject(exam.getCourse(), exam.getSubject());

        if (exists) {
        	redirectAttributes.addFlashAttribute("error",
        			  "Exam for subject <b>" + exam.getSubject() + "</b> in course <b>" + exam.getCourse() + "</b> already exists!");

            return "redirect:/admin/exams";
        }

        // save exam if not duplicate
        examService.saveExam(exam);
        redirectAttributes.addFlashAttribute("message", "Exam scheduled successfully!");
        return "redirect:/admin/exams";
    }


    @GetMapping("admin/exams/delete/{id}")
    public String deleteExamadmin(@PathVariable Long id , RedirectAttributes redirectAttributes) {
    	examService.deleteExam(id);
    	  redirectAttributes.addFlashAttribute("message", "Exam Deleted successfully!");
    	return "redirect:/admin/exams";
    }
}
