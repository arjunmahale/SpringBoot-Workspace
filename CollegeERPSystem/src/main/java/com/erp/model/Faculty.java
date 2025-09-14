package com.erp.model;

import java.sql.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Faculty {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String email;
	private String department;
	private String subject;
	private long mobile;
	private String gender;
	private int age;
	private Date dob;
	private String profile_desc;
	private String password;
	@OneToOne
	@JoinColumn(name = "course_id")
	private Course course;

	// ✅ One-to-one relation with Login
	@OneToOne(mappedBy = "faculty", cascade = CascadeType.ALL, orphanRemoval = true)
	private Login login;

	public Faculty() {
		super();
	}



	public Faculty(long id, String name, String email, String department, String subject, long mobile, String gender,
			int age, Date dob, String profile_desc, String password, Course course, Login login) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.department = department;
		this.subject = subject;
		this.mobile = mobile;
		this.gender = gender;
		this.age = age;
		this.dob = dob;
		this.profile_desc = profile_desc;
		this.password = password;
		this.course = course;
		this.login = login;
	}



	public Date getDob() {
		return dob;
	}



	public void setDob(Date dob) {
		this.dob = dob;
	}



	public long getMobile() {
		return mobile;
	}

	public void setMobile(long mobile) {
		this.mobile = mobile;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getProfile_desc() {
		return profile_desc;
	}

	public void setProfile_desc(String profile_desc) {
		this.profile_desc = profile_desc;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	
	public String getDepartment() {
		return department;
	}



	public void setDepartment(String department) {
		this.department = department;
	}



	@Override
	public String toString() {
		return "Faculty [id=" + id + ", name=" + name + ", email=" + email + ", department=" + department + ", subject="
				+ subject + ", mobile=" + mobile + ", gender=" + gender + ", age=" + age + ", dob=" + dob
				+ ", profile_desc=" + profile_desc + ", password=" + password + ", course=" + course + ", login="
				+ login + "]";
	}

}
