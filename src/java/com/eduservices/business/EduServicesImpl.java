/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.business;

import com.eduservices.business.controllers.CertificateController;
import com.eduservices.business.controllers.CourseController;
import com.eduservices.business.controllers.ExamController;
import com.eduservices.business.controllers.SectionController;
import com.eduservices.business.controllers.TranscriptController;
import com.eduservices.business.models.Transcript;
import com.eduservices.db.controllers.exceptions.IllegalOrphanException;
import com.eduservices.db.controllers.exceptions.NonexistentEntityException;
import com.eduservices.db.controllers.exceptions.PreexistingEntityException;
import com.eduservices.db.entities.Certificate;
import com.eduservices.db.entities.Course;
import com.eduservices.db.entities.Exam;
import com.eduservices.db.entities.Person;
import com.eduservices.db.entities.Section;
import java.util.List;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class EduServicesImpl {
    
    private EntityManagerFactory emf;
    
    public EduServicesImpl() {
        this.emf = javax.persistence.Persistence.createEntityManagerFactory("EduServicesPU");
    }
    
    // ******    Courses   *********
    public List<Course> getAllCourses() {
        return new CourseController(emf).getAllCourses();
    }

    public List<Course> getActiveCourses() {
        return new CourseController(emf).getActiveCourses();
    }

    public List<Course> getInactiveCourses() {
        return new CourseController(emf).getInactiveCourses();
    }

    public Course getCourse(String courseCode) {
        return new CourseController(emf).getCourse(courseCode);
    }

    public void addCourse(Course course) 
            throws PreexistingEntityException {
        try {
            new CourseController(emf).addCourse(course);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void updateCourse(Course course)
            throws IllegalOrphanException, NonexistentEntityException {
        try {
            new CourseController(emf).updateCourse(course);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
    // ******    Sections   *********
    public List<Section> getAllSections() {
        return new SectionController(emf).getAllSections();
    }
    
    public List<Section> getSectionsByCourse(String courseCode) {
        return new SectionController(emf).getSectionsByCourse(courseCode);
    }
    
    public List<Section> getCurrentSections() {
        return new SectionController(emf).getCurrentSections();
    }
    
    public List<Section> getFutureSections() {
        return new SectionController(emf).getFutureSections();
    }
    
    public List<Section> getPastSections() {
        return new SectionController(emf).getPastSections();
    }

    // ******    Certificates   *********
    public List<Certificate> getAllCertificates() {
        return new CertificateController(emf).getAllCertificates();
    }
    
    public Certificate getCertificate(String certificateCode) {
        return new CertificateController(emf).getCertificate(certificateCode);
    }
    
    public List<Certificate> getCertificatesByPerson(Person person) {
        return new CertificateController(emf).getCertificatesByPerson(person);
    }
    
    // ******    Exams   *********
    public List<Exam> getAllExaminations() {
        return new ExamController(emf).getAllExams();
    }
    
    public List<Exam> getPastExaminations() {
        return new ExamController(emf).getPastExams();
    }
    
    public List<Exam> getFutureExaminations() {
        return new ExamController(emf).getUpcomingExams();
    }
    
    // ******    Transcripts   *********
    public Transcript getTranscript(Person person) {
        return new TranscriptController(emf).getTranscript(person);
    }
}
