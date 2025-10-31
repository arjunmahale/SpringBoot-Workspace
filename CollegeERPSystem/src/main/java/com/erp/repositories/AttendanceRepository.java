package com.erp.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.erp.model.Attendance;

import jakarta.transaction.Transactional;


public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	   // Get all attendance records for a given date
    List<Attendance> findByDate(LocalDate date);
	Attendance findByStudentIdAndDate(long id, LocalDate date);


	// AttendanceRepository.java
	@Query("SELECT a FROM Attendance a WHERE a.student.course.name = :courseName AND a.date = :date")
	List<Attendance> findByCourseAndDate(@Param("courseName") String courseName, @Param("date") LocalDate date);
	long countByDateAndStatusNot(LocalDate date, String status);


}
