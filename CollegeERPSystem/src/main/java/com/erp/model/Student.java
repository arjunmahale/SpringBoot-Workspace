package com.erp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	 private int id;
	    private String name;
	    private int roll_no;
	    private String email;
	    private long mobile;
	    private String gender;
	    private int age;
	    private String profile_desc;
	    // ✅ Many students can belong to one course
	    @ManyToOne
	    @JoinColumn(name = "course_id") // foreign key column in student table
	    private Course course;
		public Student() {
			super();
		}
		public Student(int id, String name, int roll_no, String email, int mobile, String gender, int age,
				String profile_desc, Course course) {
			super();
			this.id = id;
			this.name = name;
			this.roll_no = roll_no;
			this.email = email;
			this.mobile = mobile;
			this.gender = gender;
			this.age = age;
			this.profile_desc = profile_desc;
			this.course = course;
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
		public int getRoll_no() {
			return roll_no;
		}
		public void setRoll_no(int roll_no) {
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
		@Override
		public String toString() {
			return "Student [id=" + id + ", name=" + name + ", roll_no=" + roll_no + ", email=" + email + ", mobile="
					+ mobile + ", gender=" + gender + ", age=" + age + ", profile_desc=" + profile_desc + ", course="
					+ course + "]";
		}
	    
	    
	    
	    
	
	    
	    
}
