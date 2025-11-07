package com.erp.Controller;

import com.erp.model.Marks;
import com.erp.services.MarksService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MarksController {

    private MarksService marksService;

    public MarksController(MarksService marksService) {
        this.marksService = marksService;
    }

    // ✅ FACULTY ONLY — Save marks
    @GetMapping("/exams/marks/save")
    public String saveMarks(@RequestParam("student_id") Long studentId,
                            @RequestParam("student_name") String studentName,
                            @RequestParam("subject_name") String subject,
                            @RequestParam("total_marks") int totalMarks,
                            @RequestParam("obtained_marks") int obtainedMarks,
                            @RequestParam("grade") String grade,
                            HttpSession session) {

        if (!isFaculty(session)) return "redirect:/index";

        Marks marks = new Marks();
        marks.setStudent_name(studentName);
        marks.setSubject_name(subject);
        marks.setTotal_marks(totalMarks);
        marks.setObtained_mark(obtainedMarks);
        marks.setGrade(grade);

        marksService.saveMarks(marks);

        return "redirect:/exams/marks/entry-list";
    }

    // ✅ Helper: Check faculty role
    private boolean isFaculty(HttpSession session) {
        if (session == null) return false;
        Object user = session.getAttribute("loggedInUser");
        String role = (String) session.getAttribute("role");
        return user != null && "faculty".equals(role);
    }
}
