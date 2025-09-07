package com.erp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Marks {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	String student_name;
	String subject_name;
	int total_marks;
	int obtained_mark;
	
	String grade;

	public Marks() {
		super();
	}

	public Marks(int id, String student_name, String subject_name, int total_marks, int obtained_mark,
			String grade) {
		super();
		this.id = id;
		this.student_name = student_name;
		this.subject_name = subject_name;
		this.total_marks = total_marks;
		this.obtained_mark = obtained_mark;
		this.grade = grade;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStudent_name() {
		return student_name;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

	public String getSubject_name() {
		return subject_name;
	}

	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}

	public int getTotal_marks() {
		return total_marks;
	}

	public void setTotal_marks(int totalMarks) {
		this.total_marks = totalMarks;
	}

	public int getObtained_mark() {
		return obtained_mark;
	}

	public void setObtained_mark(int obtained_mark) {
		this.obtained_mark = obtained_mark;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Override
	public String toString() {
		return "Marks [id=" + id + ", student_name=" + student_name + ", subject_name=" + subject_name
				+ ", total_marks=" + total_marks + ", obtained_mark=" + obtained_mark + ", grade=" + grade + "]";
	}
	
	
	
	
}
