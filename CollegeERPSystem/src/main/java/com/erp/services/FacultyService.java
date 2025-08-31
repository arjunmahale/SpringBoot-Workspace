
package com.erp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.Course;
import com.erp.model.Faculty;
import com.erp.repositories.CoursesRepository;
import com.erp.repositories.FacultyRepository;

import jakarta.transaction.Transactional;
@Service
public class FacultyService {
	@Autowired
	private FacultyRepository facultyRepo;
	
	@Autowired
	private CoursesRepository courseRepo;
	public List<Faculty> getAllfaculties()
	{
		List<Faculty> faculties= facultyRepo.findAll();
		if(faculties ==null) {
			System.out.println("empty list");
		}
		return faculties;
		
	}

	public Faculty savefFaculty(Faculty faculty) {
		// TODO Auto-generated method stub
		return facultyRepo.save(faculty);
		
	}
	
	@Transactional
	public void deleteFaculty(Faculty faculty) {
	    // First, fetch the faculty with courses
	    Faculty existingFaculty = facultyRepo.findById( faculty.getId()).orElse(null);
	    if (existingFaculty != null) {
	        // Set faculty null in related course(s)
	        if (existingFaculty.getCourse() != null) {
	            existingFaculty.getCourse().setFaculty(null);
	            courseRepo.save(existingFaculty.getCourse());
	        }
	        // Now delete faculty
	        facultyRepo.delete(existingFaculty);
	    }
	}


	public Faculty getFacultyById(Long id) {
		// TODO Auto-generated method stub
		Faculty faculty = facultyRepo.findAllById(id);
		return faculty;
	}
	

    public Faculty getFacultyByCourse(Course course) {
        return facultyRepo.findByCourse(course);
    }


    // Count total faculty members
    public long countFaculty() {
        return facultyRepo.count();
    }

}
