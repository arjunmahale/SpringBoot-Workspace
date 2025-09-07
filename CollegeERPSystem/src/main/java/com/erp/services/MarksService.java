package com.erp.services;

import com.erp.model.Marks;
import com.erp.repositories.MarksRepository;

import org.springframework.stereotype.Service;

@Service
public class MarksService {

    private final MarksRepository marksRepository;

    public MarksService(MarksRepository marksRepository) {
        this.marksRepository = marksRepository;
    }

    public Marks saveMarks(Marks marks) {
        return marksRepository.save(marks);
    }
}
