package com.demo.model;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="employee_tbl")

public class Employee {
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private int id;
	@Column(name="emp_name")
	private String name;
	private String salary;
	private String company;
	@Column(name="job_role")
	private String role;
	private String image;
	private String status;
	
	
	public Employee() {
		super();
	}
	public Employee(int id, String name, String salary, String company, String role, String image,String status) {
		super();
		this.id = id;
		this.name = name;
		this.salary = salary;
		this.company = company;
		this.role = role;
		this.image = image;
		this.status=status;
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
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", salary=" + salary + ", company=" + company + ", role="
				+ role + ", image=" + image + ", status=" + status + "]";
	}
	
	
	
	
}
