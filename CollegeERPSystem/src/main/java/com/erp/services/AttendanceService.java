package com.erp.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.Attendance;
import com.erp.repositories.AttendanceRepository;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;
    
    public List<Attendance> getByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }

    public void saveAttendance(Attendance attendance) {
        // ✅ Check if attendance already exists for this student and date
        Attendance existingAttendance = attendanceRepository
                .findByStudentIdAndDate(attendance.getStudent().getId(), attendance.getDate());

        if (existingAttendance!=null) {
            // If already exists, update the status
           // Attendance existing = existingAttendance.get();
        	existingAttendance.setStatus(attendance.getStatus());
            attendanceRepository.save(existingAttendance);
        } else {
            // Else save as new record
            attendanceRepository.save(attendance);
        }
    }
}
