package com.erp.Controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.erp.model.Attendance;
import com.erp.model.Student;
import com.erp.services.StudentService;
import com.erp.state.AttendanceState;
import com.erp.services.AttendanceService;

import jakarta.servlet.http.HttpSession;

@Controller
public class StudentProfileController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("student-dashboard")
    public String showStudent(Model model, HttpSession session) {
        Student student = (Student) session.getAttribute("student");
        model.addAttribute("student", student);

        // ✅ Today’s date
        LocalDate today = LocalDate.now();
        model.addAttribute("today", today.toString());

        String courseName = (student != null && student.getCourse() != null)
                            ? student.getCourse().getName()
                            : null;

        boolean isOpen = (courseName != null) && AttendanceState.isAttendanceOpen(courseName);
        model.addAttribute("attendanceOpen", isOpen);

        return "student-links/student-dashboard";
    }

    @GetMapping("student-attendance")
    public String showStudentattendance(Model model, HttpSession session) {
        Student student = (Student) session.getAttribute("student");

        // ✅ Today’s date
        LocalDate today = LocalDate.now();
        model.addAttribute("today", today.toString());

        boolean canMark = false;
        String courseName = (student != null && student.getCourse() != null)
                            ? student.getCourse().getName()
                            : null;

        if (courseName != null && AttendanceState.isAttendanceOpen(courseName)) {
            canMark = true;
        }

        model.addAttribute("today", LocalDate.now().toString());
        model.addAttribute("canMark", canMark);
        model.addAttribute("student", student);

        return "student-links/student-attendance";
    }

    @PostMapping("/student-attendance/mark")
    public String markAttendance(HttpSession session,
                                 @RequestParam String status,
                                 RedirectAttributes redirectAttributes) {
        Student student = (Student) session.getAttribute("student");
        if (student == null || student.getCourse() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Student not logged in or course missing.");
            return "redirect:/student-attendance";
        }

        String courseName = student.getCourse().getName();

        // ✅ Check if attendance window is open for this course
        if (!AttendanceState.isAttendanceOpen(courseName)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Attendance window closed!");
            return "redirect:/student-attendance";
        }

        Attendance attendance = new Attendance();
        attendance.setDate(LocalDate.now());
        attendance.setStatus(status);
        attendance.setName(student.getName());
        attendance.setCourse(courseName);
        attendance.setStudent(student);

        attendanceService.saveAttendance(attendance);

        redirectAttributes.addFlashAttribute("successMessage", "Attendance marked as " + status);
        return "redirect:/student-attendance";
    }
}
