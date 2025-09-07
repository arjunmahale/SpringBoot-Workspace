package com.erp.Controller;

import com.erp.model.Marks;
import com.erp.services.MarksService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MarksController {

    private  MarksService marksService;

    public MarksController(MarksService marksService) {
        this.marksService = marksService;
    }

    @GetMapping("/exams/marks/save")
    public String saveMarks(
            @RequestParam("student_id") Long studentId,
            @RequestParam("student_name") String studentName,
            @RequestParam("subject_name") String subject,
            @RequestParam("total_marks") int totalMarks,
            @RequestParam("obtained_marks") int obtainedMarks,
            @RequestParam("grade") String grade) {

        Marks marks = new Marks();
       // marks.setStudentId(studentId);
        marks.setStudent_name(studentName);
        marks.setSubject_name(subject);
        marks.setTotal_marks(totalMarks);
        marks.setObtained_mark(obtainedMarks);
        marks.setGrade(grade);

        marksService.saveMarks(marks);

        return "redirect:/exams/marks/entry-list"; // after save redirect to student list
    }
}
