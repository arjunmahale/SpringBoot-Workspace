package com.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.demo.model.Employee;
import com.demo.repository.EmployeeRepository;

@Service
public class EmployeeService {
	@Autowired
	EmployeeRepository empRepo;
	public List<Employee> getEmployees(){
	
		List<Employee> employees=empRepo.findAll();//getting all employeesfrom table
			
		return employees;
}
		public Employee getEmployeeById(int id)
		{
			Employee emp=empRepo.findById(id).get();
		
			
			return emp;
		}
		public Employee saveEmployee(Employee employee)
		{
			employee.setStatus("Added");
			Employee emp=empRepo.save(employee);
			return emp;
		}
		public Employee delete(int id)
		{
			Employee empdel= empRepo.findById(id).get();
			
			empdel.setStatus("deleted");
			Employee emp=empRepo.save(empdel);
			return emp;
		}
}
