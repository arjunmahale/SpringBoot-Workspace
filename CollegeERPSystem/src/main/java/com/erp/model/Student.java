package com.erp.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String roll_no;
    private String email;
    private long mobile;
    private String gender;
    private int age;
    private Date dob;
    private String profile_desc;

    @ManyToOne
    @JoinColumn(name = "course_id") 
    private Course course;

    private String password;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendanceRecords = new ArrayList<>();


    // ✅ One-to-one relation with Login
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Login login;


    public Student() {}

  
    public Student(long id, String name, String roll_no, String email, long mobile, String gender, int age, Date dob,
			String profile_desc, Course course, String password, List<Attendance> attendanceRecords, Login login) {
		super();
		this.id = id;
		this.name = name;
		this.roll_no = roll_no;
		this.email = email;
		this.mobile = mobile;
		this.gender = gender;
		this.age = age;
		this.dob = dob;
		this.profile_desc = profile_desc;
		this.course = course;
		this.password = password;
		this.attendanceRecords = attendanceRecords;
		this.login = login;
	}


	// Getters & setters
    // ...
    
    
    public Login getLogin() {
        return login;
    }
    public Date getDob() {
		return dob;
	}


	public void setDob(Date dob) {
		this.dob = dob;
	}


	public List<Attendance> getAttendanceRecords() {
		return attendanceRecords;
	}


	public void setAttendanceRecords(List<Attendance> attendanceRecords) {
		this.attendanceRecords = attendanceRecords;
	}


	public void setLogin(Login login) {
        this.login = login;
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

	public String getRoll_no() {
		return roll_no;
	}

	public void setRoll_no(String roll_no) {
		this.roll_no = roll_no;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", roll_no=" + roll_no + ", email=" + email + ", mobile="
				+ mobile + ", gender=" + gender + ", age=" + age + ", dob=" + dob + ", profile_desc=" + profile_desc
				+ ", course=" + course + ", password=" + password + ", attendanceRecords=" + attendanceRecords
				+ ", login=" + login + "]";
	}
}
