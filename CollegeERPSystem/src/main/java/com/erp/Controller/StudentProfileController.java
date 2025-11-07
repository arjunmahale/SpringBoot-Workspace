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

    // ✅ STUDENT ONLY — Dashboard
    @GetMapping("student-dashboard")
    public String showStudent(Model model, HttpSession session) {

        if (!isStudent(session)) return "redirect:/index";

        Student student = (Student) session.getAttribute("student");
        model.addAttribute("student", student);

        model.addAttribute("today", LocalDate.now().toString());

        String courseName = (student != null && student.getCourse() != null)
                ? student.getCourse().getName()
                : null;

        boolean isOpen = (courseName != null) && AttendanceState.isAttendanceOpen(courseName);
        model.addAttribute("attendanceOpen", isOpen);

        return "student-links/student-dashboard";
    }

    // ✅ STUDENT ONLY — Attendance page
    @GetMapping("student-attendance")
    public String showStudentattendance(Model model, HttpSession session) {

        if (!isStudent(session)) return "redirect:/index";

        Student student = (Student) session.getAttribute("student");

        model.addAttribute("today", LocalDate.now().toString());

        boolean canMark = false;

        String courseName = (student != null && student.getCourse() != null)
                ? student.getCourse().getName()
                : null;

        if (courseName != null && AttendanceState.isAttendanceOpen(courseName)) {
            canMark = true;
        }

        model.addAttribute("canMark", canMark);
        model.addAttribute("student", student);

        return "student-links/student-attendance";
    }

    // ✅ STUDENT ONLY — Mark attendance
    @PostMapping("/student-attendance/mark")
    public String markAttendance(HttpSession session,
                                 @RequestParam String status,
                                 RedirectAttributes redirectAttributes) {

        if (!isStudent(session)) return "redirect:/index";

        Student student = (Student) session.getAttribute("student");

        if (student == null || student.getCourse() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Student not logged in or course missing.");
            return "redirect:/student-attendance";
        }

        String courseName = student.getCourse().getName();

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

    // ✅ HELPER: Student check
    private boolean isStudent(HttpSession session) {
        if (session == null) return false;

        Object user = session.getAttribute("loggedInUser");
        String role = (String) session.getAttribute("role");

        return user != null && "student".equals(role);
    }
}
