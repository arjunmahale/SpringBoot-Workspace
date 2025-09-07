package com.erp.model;


import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "exams")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    private String description;

    private LocalDate examDate;

    private String time; // e.g., "10:00 AM - 12:00 PM"

    private String course; // Just store course name directly

    // Getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getExamDate() { return examDate; }
    public void setExamDate(LocalDate examDate) { this.examDate = examDate; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
}
