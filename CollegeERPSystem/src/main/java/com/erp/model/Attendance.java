package com.erp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Date of attendance
    private LocalDate date;

    // Status: Present / Absent
    private String status;

    private String name;

    private String course;


    // Relation with Student (Many attendance records belong to one student)
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // ✅ Constructors
    public Attendance() {}



    public Attendance(Long id, LocalDate date, String status, String name, String course, Student student) {
		super();
		this.id = id;
		this.date = date;
		this.status = status;
		this.name = name;
		this.course = course;
		this.student = student;
	}

	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getCourse() {
		return course;
	}



	public void setCourse(String course) {
		this.course = course;
	}



	// ✅ Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}
}
