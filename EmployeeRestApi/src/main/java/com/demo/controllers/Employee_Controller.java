package com.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.demo.EmployeeRestApiApplication;
import com.demo.model.Employee;
import com.demo.services.EmployeeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@RestController
@RequestMapping("/api/employee")
public class Employee_Controller {

    private final EmployeeRestApiApplication employeeRestApiApplication;
	@Autowired
	EmployeeService empService;


    Employee_Controller(EmployeeRestApiApplication employeeRestApiApplication) {
        this.employeeRestApiApplication = employeeRestApiApplication;
    }


	public String getMethodName(@RequestParam String param) {
		return new String();
	}
	@GetMapping("/all")
	public ResponseEntity<List<Employee>> getAllEmployee(){
		List<Employee> employees= empService.getEmployees();

		return new ResponseEntity<>(employees,HttpStatus.OK);

	}

	@GetMapping("/empbyid/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Integer id){

		try {
			Employee empbyid= empService.getEmployeeById(id);


			return new ResponseEntity<Employee>((Employee) empbyid, HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<Employee>(HttpStatus.NOT_FOUND);
		}



	}

	@PostMapping("/save")
	public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee){


		Employee empl=  empService.saveEmployee(employee);

		return new ResponseEntity<>(empl,HttpStatus.ACCEPTED);

	}


	@GetMapping("/delete/{id}")
	public ResponseEntity<Employee> saveEmployee(@PathVariable Integer id){


		Employee emplo=  empService.delete(id);

		return new ResponseEntity<>(emplo,HttpStatus.OK);

	}



	@PostMapping("/update")
	public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee){


		Employee empl=  empService.updateEmployee(employee);

		return new ResponseEntity<>(empl,HttpStatus.ACCEPTED);

	}
}
