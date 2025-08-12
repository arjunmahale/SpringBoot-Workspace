package com.demo.service;

import java.util.ArrayList;

import com.demo.model.Employee;

public class Service {

	public static ArrayList<Employee> getAllEmployee() {
		
		ArrayList<Employee> list=new ArrayList<Employee>();
		list.add(new Employee(1,"ajay"));
		list.add(new Employee(2,"jay"));
		list.add(new Employee(3,"jayesh"));
		return list;
		
	}

	public static Employee getEmployeeById(int id) {
		// TODO Auto-generated method stub
		for(Employee s:getAllEmployee())
		{
			if(s.getId() == id)
			{
				return s;
			}
		}
		return null;
	}
}
