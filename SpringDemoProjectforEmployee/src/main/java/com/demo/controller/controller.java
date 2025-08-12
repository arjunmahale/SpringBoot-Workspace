package com.demo.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.model.Employee;
import com.demo.service.Service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;





@Controller
@ResponseBody
public class controller {
	 // Welcome endpoint
    @GetMapping("/")
    public String index() {
        return "Welcome to empployee REST API";
    }


//	
//    @GetMapping("/emp/{name}")
//    public String getMethodName(@PathVariable String name) {
//        return name;
//    }
//    
	
    
    @GetMapping("/emp")
    public ArrayList<Employee> getMethodName() {
    	ArrayList<Employee> emp =Service.getAllEmployee();
		return emp;
    }
    
    @GetMapping("/emp/{id}")
    public Employee getEmployeeById(@PathVariable int id) {
    	Employee emp =Service.getEmployeeById(id);
    	return emp;
    }
    
    @GetMapping("/db")
    public ArrayList<Employee> getEmployeeById() {
   
    			
    			try {
    				//driver register->load driver
    				Class.forName("com.mysql.cj.jdbc.Driver");
    				//make connection
    				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/demo","root","root");
    				
    				//statement creation
    				Statement stmt=con.createStatement();

    				
    				//executequery-> use when all of select
    				//executeupdate->use when update other that ^
    				//execute -> both can be used
    				ResultSet rs=stmt.executeQuery("select * from users");
    				 ArrayList<Employee> list=new ArrayList<Employee>();
    				while(rs.next())
    				{
    					System.out.println(rs.getInt(1)+" "+rs.getString("name")+" "+rs.getString(3)+" "+" "+rs.getString(4));
    					list.add(new Employee(rs.getInt(1),rs.getString("name")));
    				}
    			return list;
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
				return null;
				

    		}

    	
    }
    
    

