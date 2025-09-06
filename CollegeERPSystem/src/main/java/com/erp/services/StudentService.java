package com.erp.services;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.engine.jdbc.spi.TypeNullability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.Student;
import com.erp.repositories.AttendanceRepository;
import com.erp.repositories.StudentRepository;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studRepo;
	
	@Autowired
	private AttendanceRepository attendanceRepository;

	public List<Student> getAllStudent() {
		
		List<Student> student = studRepo.findAll();
		if (student == null) {
			System.out.println("empty list");
		}
		
		return student;

	}

	public Student saveStudent(Student student) {
		if (student.getId() == 0) { // only for new student
			long count = studRepo.count() + 1; // safer to use long
			String roll = String.format("S%03d", count); // S01, S02, S03...
			student.setRoll_no(roll);
		}
		else {
			student.setRoll_no(student.getRoll_no());
		}
		return studRepo.save(student);
	}

	public void deleteStudent(Student student) {
		
		
        
		studRepo.delete(student);
		;

	}

	public Student getStudentById(Long id) {
		// TODO Auto-generated method stub
		Student student = studRepo.findAllById(id);
		return student;
	}

	public long countStudents() {
	    System.out.println("Inside countStudents()"); // debug
	    return studRepo.count(); // just count rows in DB
	}

    // Get last 5 students added
    public List<Student> getRecentStudents() {
        return studRepo.findAll()
                       .stream()
                       .sorted((s1, s2) -> Long.compare(s2.getId(), s1.getId())) // descending by ID
                       .limit(5)
                       .collect(Collectors.toList());
    }

    
    
    
    //for faculty dashboard
    
    
    public List<Student> getAllStudentByCourse(String name) {
    	List<Student> students = studRepo.findByCourse_Name(name);

        if (students.isEmpty()) {
            System.out.println("No students found for course: " + name);
        }
        return students;
    }

}
