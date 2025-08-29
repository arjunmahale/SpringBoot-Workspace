package com.erp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Login {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String password;
    private String role;
    private String email;

    // ✅ One-to-one relation with Student
    @OneToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id") // foreign key
    private Student student;
    
    @OneToOne
    @JoinColumn(name = "faculty_id", referencedColumnName = "id") // foreign key
    private Faculty faculty;

    public Login() {}

    public Login(long id, String name, String password, String role, String email, Student student, Faculty faculty) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.role = role;
		this.email = email;
		this.student = student;
		this.faculty = faculty;
	}

	// Getters & setters
    // ...
    public Student getStudent() {
        return student;
    }
    public void setStudent(Student student) {
        this.student = student;
    }

    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Faculty getFaculty() {
		return faculty;
	}

	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}

	@Override
	public String toString() {
		return "Login [id=" + id + ", name=" + name + ", password=" + password + ", role=" + role + ", email=" + email
				+ ", student=" + student + ", faculty=" + faculty + "]";
	}
}
