package com.erp.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
import com.erp.services.AttendanceService; // ✅ new service for attendance window

import jakarta.servlet.http.HttpSession;

@Controller
public class StudentProfileController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private AttendanceService attendanceService; // ✅ injected service

    @GetMapping("student-dashboard")
    public String showStudent(Model model, HttpSession session) {

        Student student = (Student) session.getAttribute("student");
        model.addAttribute("student", student);

        // ✅ Pass whether attendance is open or not
        model.addAttribute("attendanceOpen", attendanceService.isAttendanceOpen());

        return "student-links/student-dashboard";
    }

    @GetMapping("student-attendance")
    public String showStudentattendance(Model model, HttpSession session) {
        Student student = (Student) session.getAttribute("student");

        boolean canMark = false;

        if (AttendanceState.attendanceOpen && AttendanceState.attendanceStartTime != null) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(AttendanceState.attendanceStartTime.plusMinutes(30))) {
                canMark = true; // ✅ still within 30 minutes
            } else {
                AttendanceState.attendanceOpen = false; // auto-close after 30 min
            }
        }
        LocalDate today = LocalDate.now();
	     model.addAttribute("today", today.toString());

        model.addAttribute("canMark", canMark);
        model.addAttribute("student", student);

        return "student-links/student-attendance";
    }

    @PostMapping("/student-attendance/mark")
    public String markAttendance(HttpSession session,
                                 @RequestParam String status,
                                 RedirectAttributes redirectAttributes) {
        Student student = (Student) session.getAttribute("student");
        if (student == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Student not logged in.");
            return "redirect:/student-attendance";
        }

        // Check if window is open
        if (!AttendanceState.attendanceOpen ||
            AttendanceState.attendanceStartTime.plusMinutes(30).isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Attendance window closed!");
            return "redirect:/student-attendance";
        }

        Attendance attendance = new Attendance();
        attendance.setDate(LocalDate.now());
        attendance.setStatus(status);
        attendance.setName(student.getName());
        attendance.setCourse(student.getCourse() != null ? student.getCourse().getName() : "N/A");
        attendance.setStudent(student);

        attendanceService.saveAttendance(attendance);

        redirectAttributes.addFlashAttribute("successMessage", "Attendance marked as " + status);
        return "redirect:/student-attendance";
    }


}
