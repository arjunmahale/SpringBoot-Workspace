package com.erp.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;

    // ✅ One course can have many students
    @OneToMany(mappedBy = "course")
    private List<Student> students = new ArrayList<>();

    @OneToOne(mappedBy = "course")
    // foreign key in course table
    private Faculty faculty;

    public Course() {}

  

    public Course(int id, String name, String description, List<Student> students, Faculty faculty) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.students = students;
		this.faculty = faculty;
	}

	// --- Getters & Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public Faculty getFaculty() {
		return faculty;
	}



	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}



	// Helper method to add a student to course
    public void addStudent(Student student) {
        students.add(student);
        student.setCourse(this);
    }

    // Helper method to remove a student from course
    public void removeStudent(Student student) {
        students.remove(student);
        student.setCourse(null);
    }
}
