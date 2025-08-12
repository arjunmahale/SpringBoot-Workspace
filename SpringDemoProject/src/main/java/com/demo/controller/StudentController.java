package com.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.demo.models.Student;
import com.demo.services.StudentService;

@Controller
@ResponseBody
public class StudentController {

    
    StudentService studentService=new StudentService();
    
    // Welcome endpoint
    @GetMapping("/")
    public String index() {
        return "Welcome to Student REST API";
    }

    // Get all students
    @GetMapping("/student")
    public List<Student> getAllStudent() {
        return studentService.getAllStudent();
    }

    // Get student by ID
    @GetMapping("/student/{id}")
    public Student getStudentById(@PathVariable int id) {
        return studentService.getStudentById(id);
    }
    
    // Get all students
    @PostMapping("/student")
    public ResponseEntity<Student> insertStudent(@RequestBody Student student) {
    	  Student student1= StudentService.insertStudent(student);
		   return  new ResponseEntity<Student>(student1,HttpStatus.ACCEPTED);
    	
      
    }
    @DeleteMapping("/student/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable int id){
    	
     Student student2=StudentService.deleteStudent(id);
    	
     return  new ResponseEntity<Student>(student2,HttpStatus.ACCEPTED);
    	
    }
}
