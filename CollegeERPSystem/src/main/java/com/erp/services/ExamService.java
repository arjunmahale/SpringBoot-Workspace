package com.erp.services;

import com.erp.model.Exam;
import com.erp.repositories.ExamRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepo;

    public Exam saveExam(Exam exam) {
        return examRepo.save(exam);
    }

    public List<Exam> getAllExams() {
        return examRepo.findAll();
    }

    public Exam getExamById(Long id) {
        return examRepo.findById(id).orElse(null);
    }

    public void deleteExam(Long id) {
        examRepo.deleteById(id);
    }
    public boolean existsByCourseAndSubject(String course, String subject) {
        return examRepo.existsByCourseAndSubject(course, subject);
    }



    // ✅ Method to get all unique subjects
    public List<String> getAllUniqueSubjects() {
        return examRepo.findDistinctSubjects();
    }
    
}
