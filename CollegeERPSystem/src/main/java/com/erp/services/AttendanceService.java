package com.erp.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.Attendance;
import com.erp.repositories.AttendanceRepository;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    // ---------------- Attendance DB Logic ----------------

    public List<Attendance> getByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }

 // AttendanceService.java
    public List<Attendance> getByCourseAndDate(String courseName, LocalDate date) {
        return attendanceRepository.findByCourseAndDate(courseName, date);
    }


    public void saveAttendance(Attendance attendance) {
        // ✅ Check if attendance already exists for this student and date
        Attendance existingAttendance = attendanceRepository
                .findByStudentIdAndDate(attendance.getStudent().getId(), attendance.getDate());

        if (existingAttendance != null) {
            // If already exists, update the status
            existingAttendance.setStatus(attendance.getStatus());
            attendanceRepository.save(existingAttendance);
        } else {
            // Else save as new record
            attendanceRepository.save(attendance);
        }
    }

    // ---------------- Time-bound Attendance Window ----------------

    private boolean attendanceOpen = false;
    private LocalDateTime startTime;

    // Called by faculty to start attendance
    public void startAttendanceWindow() {
        attendanceOpen = true;
        startTime = LocalDateTime.now();
    }

    // Faculty can manually stop
    public void stopAttendanceWindow() {
        attendanceOpen = false;
        startTime = null;
    }

    // Check if attendance is still open
    public boolean isAttendanceOpen() {
        if (!attendanceOpen || startTime == null) {
            return false;
        }

        Duration duration = Duration.between(startTime, LocalDateTime.now());

        // ✅ Auto-close after 30 minutes
        if (duration.toMinutes() > 30) {
            stopAttendanceWindow();
            return false;
        }
        return true;
    }

    public long countAttendanceMarkedToday() {
        return attendanceRepository.countByDateAndStatusNot(LocalDate.now(), "Not Marked");
    }
}
