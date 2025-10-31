package com.erp.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;
    private int duration;
    private int credits;

    // ✅ store subjects as comma-separated values (e.g. "Math,Physics,Chemistry")
    @Column(length = 1000)
    private String subjects;

    @OneToMany(mappedBy = "course")
    private List<Student> students = new ArrayList<>();

    @OneToOne(mappedBy = "course")
    private Faculty faculty;

    public Course() {}

    // constructor including subjects
    public Course(int id, String name, String description, int duration, int credits, String subjects,
                  List<Student> students, Faculty faculty) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.credits = credits;
        this.subjects = subjects;
        this.students = students;
        this.faculty = faculty;
    }

    // --- Getters & Setters ---


    public String getSubjects() {
        return subjects;
    }

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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
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

	public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    // Utility method to get subjects as list
    public List<String> getSubjectList() {
        if (subjects == null || subjects.isBlank()) {
            return new ArrayList<>();
        }
        return List.of(subjects.split(","));
    }

	public Course orElseThrow(Object object) {
		// TODO Auto-generated method stub
		return null;
	}
}
