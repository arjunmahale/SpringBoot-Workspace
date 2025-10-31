package com.erp.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.erp.model.Attendance;
import com.erp.model.Course;
import com.erp.model.Student;
import com.erp.services.AttendanceService;
import com.erp.services.StudentService;
import com.erp.services.courseService;
import com.erp.state.AttendanceState;

import jakarta.servlet.http.HttpSession;

@Controller
public class FacultyDashController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private courseService courseService;
    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/faculty-dashboard")
    public String showFacultyDashboard(HttpSession session, Model model) {
        String user1 = (String) session.getAttribute("user");
        String admin_course = (String) session.getAttribute("admin_course");

        model.addAttribute("user", user1);

        List<Student> s1 = studentService.getAllStudent();
        List<Student> s2 = new ArrayList<>();

        long cnt = 0;
        for (Student stu : s1) {
            if (stu.getCourse() != null && admin_course.equalsIgnoreCase(stu.getCourse().getName())) {
                s2.add(stu);
                cnt++;
            }
        }
        // ✅ Count of all students who marked attendance (excluding Not Marked)
        long attendanceMarkedCount = attendanceService.countAttendanceMarkedToday();
        model.addAttribute("attendancemarkedcount", attendanceMarkedCount);

        model.addAttribute("totalStudents", cnt);
        model.addAttribute("students", s2);
        model.addAttribute("today", LocalDate.now().toString());

        return "faculty-links/faculty-dashboard";
    }

    @GetMapping("/student-list")
    public String showStudentsToFaculty(HttpSession session, Model model) {
        String user1 = (String) session.getAttribute("user");
        String admin_course = (String) session.getAttribute("admin_course");
        model.addAttribute("admin_course",admin_course);
        model.addAttribute("user", user1);

        List<Student> students = studentService.getAllStudent();
        List<Student> s2 = new ArrayList<>();

        for (Student stu : students) {
            if (stu.getCourse() != null && admin_course.equalsIgnoreCase(stu.getCourse().getName())) {
                s2.add(stu);
            }
        }

        // ✅ Today’s date
        LocalDate today = LocalDate.now();
        model.addAttribute("today", today.toString());

        model.addAttribute("students", s2);
        return "faculty-links/student-list";
    }

 // ✅ Show today's attendance (default view)
    @GetMapping("/attendance")
    public String showAttendance(HttpSession session, Model model) {
        // ✅ Check if logged in
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/";
        }

        String admin_course = (String) session.getAttribute("admin_course");
        String user1 = (String) session.getAttribute("user");

        model.addAttribute("user", user1);
        model.addAttribute("course", admin_course);

        // ✅ Today’s date
        LocalDate today = LocalDate.now();
        model.addAttribute("today", today.toString());

        // ✅ Get students of this faculty's course
        List<Student> courseStudents = studentService.getAllStudent().stream()
                .filter(stu -> stu.getCourse() != null &&
                               admin_course.equalsIgnoreCase(stu.getCourse().getName()))
                .toList();

        // ✅ Attendance map for today
        Map<Long, String> attendanceMap = new HashMap<>();
        List<Attendance> todayAttendance = attendanceService.getByDate(today);

        for (Attendance att : todayAttendance) {
            if (att.getStudent().getCourse() != null &&
                admin_course.equalsIgnoreCase(att.getStudent().getCourse().getName())) {
                attendanceMap.put(att.getStudent().getId(), att.getStatus());
            }
        }

        model.addAttribute("students", courseStudents);
        model.addAttribute("attendanceMap", attendanceMap);
        model.addAttribute("totalStudents", courseStudents.size());

        boolean canMark = AttendanceState.isAttendanceOpen(admin_course);
        model.addAttribute("canMark", canMark);

        return "faculty-links/attendance";
    }


 // ✅ Show attendance by date (restricted to faculty’s course)
    @GetMapping("/attendance-by-date")
    public String getAttendanceByDate(
            @RequestParam("date")LocalDate date,
            HttpSession session,
            Model model) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/";
        }

        String admin_course = (String) session.getAttribute("admin_course");
        String user1 = (String) session.getAttribute("user");

        // ✅ Today’s date
        LocalDate today = LocalDate.now();
        model.addAttribute("today", today.toString());

        model.addAttribute("user", user1);
        model.addAttribute("course", admin_course);
        model.addAttribute("selectedDate", date.toString());

        // ✅ Students of faculty’s course
        List<Student> studentsByCourse = studentService.getAllStudent().stream()
                .filter(stu -> stu.getCourse() != null &&
                               admin_course.equalsIgnoreCase(stu.getCourse().getName()))
                .toList();

        // ✅ Attendance for selected date & faculty’s course
        Map<Long, String> attendanceMap = new HashMap<>();
        List<Attendance> attendanceList = attendanceService.getByCourseAndDate(admin_course, date);

        for (Attendance att : attendanceList) {
            if (att.getStudent().getCourse() != null &&
                admin_course.equalsIgnoreCase(att.getStudent().getCourse().getName())) {
                attendanceMap.put(att.getStudent().getId(), att.getStatus());
            }
        }

        model.addAttribute("students", studentsByCourse);
        model.addAttribute("attendanceMap", attendanceMap);
        model.addAttribute("totalStudents", studentsByCourse.size());

        return "faculty-links/attendance";
    }



    @GetMapping("/allow-attendance")
    public String allowAttendance(HttpSession session) {
        String courseName = (String) session.getAttribute("admin_course");

        if (courseName != null) {
            AttendanceState.openAttendance(courseName);
            session.setAttribute("message", "Attendance is now open for students of course: " + courseName);
        } else {
            session.setAttribute("message", "No course assigned to faculty.");
        }

        return "redirect:/attendance";
    }

    @GetMapping("/attendance/stop")
    public String stopAttendance(HttpSession session) {
        String courseName = (String) session.getAttribute("admin_course");

        if (courseName != null) {
            AttendanceState.closeAttendance(courseName);
            session.setAttribute("message", "Attendance has been closed for course: " + courseName);
        }

        return "redirect:/attendance";
    }

    @GetMapping("/total-attendance")
    public String showAllAttendance(HttpSession session, Model model) {
        String user1 = (String) session.getAttribute("user");
        String admin_course = (String) session.getAttribute("admin_course");

        model.addAttribute("today", LocalDate.now().toString());
        model.addAttribute("user", user1);

        List<Student> s1 = studentService.getAllStudent();
        List<Student> s2 = new ArrayList<>();

        for (Student stu : s1) {
            if (stu.getCourse() != null && admin_course.equalsIgnoreCase(stu.getCourse().getName())) {
                s2.add(stu);
            }
        }

        Map<Long, String> attendanceMap = new HashMap<>();
        List<Attendance> todayAttendance = attendanceService.getByDate(LocalDate.now());

        for (Attendance att : todayAttendance) {
            if (att.getStudent().getCourse() != null &&
                admin_course.equalsIgnoreCase(att.getStudent().getCourse().getName())) {
                attendanceMap.put(att.getStudent().getId(), att.getStatus());
            }
        }

        model.addAttribute("students", s2);
        model.addAttribute("attendanceMap", attendanceMap);
        model.addAttribute("totalStudents", s2.size());

        return "faculty-links/total-attendance";
    }

    @PostMapping("/attendance/save")
    public String saveAttendance(@RequestParam Long studentId,
                                 @RequestParam String status,
                                 @RequestParam String name,
                                 @RequestParam String date,
                                 RedirectAttributes redirectAttributes) {

        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Student not found!");
            return "redirect:/student-attendance";
        }

        Attendance attendance = new Attendance();
        attendance.setDate(LocalDate.parse(date));
        attendance.setStatus(status);
        attendance.setName(student.getName());
        attendance.setCourse(student.getCourse() != null ? student.getCourse().getName() : "N/A");
        attendance.setStudent(student);

        attendanceService.saveAttendance(attendance);

        redirectAttributes.addFlashAttribute("savedStudentId", studentId);
        redirectAttributes.addFlashAttribute("message", "Attendance saved successfully!");
        return "redirect:/student-attendance";
    }
}
