package com.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.demo.models.Student;

@Service
public class StudentService {

    // Returns all students
    public List<Student> getAllStudent() {
        List<Student> list = new ArrayList<>();
        list.add(new Student(1, "arjun", "nsk", "arjun@gmail.com"));
        list.add(new Student(2, "vaibhav", "pune", "vaibhav@gmail.com"));
        list.add(new Student(3, "om", "usa", "om@gmail.com"));
        return list;
    }

    // Returns a student by ID
    public Student getStudentById(int id) {
        for (Student s : getAllStudent()) {
            if (s.getId() == id) {
                return s; // Directly return matching student
            }
        }
        return null; // Not found
    }
}
