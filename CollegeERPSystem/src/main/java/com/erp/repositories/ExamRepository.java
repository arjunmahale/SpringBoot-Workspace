package com.erp.repositories;

import com.erp.model.Exam;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

	  boolean existsByCourseAndSubject(String course, String subject);
	  
	    // ✅ Custom query to get unique subjects
	    @Query("SELECT DISTINCT e.subject FROM Exam e")
	    List<String> findDistinctSubjects();
	    
}
