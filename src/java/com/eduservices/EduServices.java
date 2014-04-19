/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices;

import com.eduservices.business.EduServicesImpl;
import com.eduservices.business.models.Transcript;
import com.eduservices.db.controllers.exceptions.IllegalOrphanException;
import com.eduservices.db.controllers.exceptions.NonexistentEntityException;
import com.eduservices.db.controllers.exceptions.PreexistingEntityException;
import com.eduservices.db.entities.Certificate;
import com.eduservices.db.entities.Course;
import com.eduservices.db.entities.Exam;
import com.eduservices.db.entities.Person;
import com.eduservices.db.entities.Section;
import com.eduservices.exceptions.NonexistentCourseException;
import com.eduservices.exceptions.PreexistingCourseException;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author bjw
 */
@WebService(serviceName = "EduServices")
public class EduServices {
    EduServicesImpl impl;
    
    public EduServices() {
        this.impl = new EduServicesImpl();
    }
    
    /**
     * All of the courses.
     * @return  a list of courses
     */
    @WebMethod(operationName = "getAllCourses")
    public List<Course> getAllCourses() {
        return this.impl.getAllCourses();
    }
    
    /**
     * All active courses.
     * @return  a list of courses
     */
    @WebMethod(operationName = "getActiveCourses")
    public List<Course> getActiveCourses() {
        return this.impl.getActiveCourses();
    }
    
    /**
     * All inactive courses.
     * @return  a list of courses
     */
    @WebMethod(operationName = "getInactiveCourses")
    public List<Course> getInactiveCourses() {
        return this.impl.getInactiveCourses();
    }
    
    /**
     * A course by course code.
     * @param courseCode
     * @return  a course
     */
    @WebMethod(operationName = "getCourse")
    public Course getCourse(
            @WebParam(name = "courseCode") String courseCode) {
        return this.impl.getCourse(courseCode);
    }
    
    /**
     * Add a course.
     * @param course    a course
     * @throws PreexistingCourseException   course already exists
     */
    @WebMethod(operationName = "addCourse")
    public void addCourse  (
            @WebParam(name = "course") Course course) 
            throws PreexistingCourseException {
        try {
            this.impl.addCourse(course);
        } catch (PreexistingEntityException e) {
            Course existingCourse = this.impl.getCourse(course.getCourseCode());
            throw new PreexistingCourseException(existingCourse.getCourseCode(),
                                                 existingCourse.getCourseTitle());
        }
    }
    
    @WebMethod(operationName = "updateCourse")
    public void updateCourse (
            @WebParam(name = "course") Course course)
            throws IllegalOrphanException, NonexistentCourseException {
        try {
            this.impl.updateCourse(course);
        } catch(IllegalOrphanException e) {
            throw e;
        } catch(NonexistentEntityException e) {
            throw new NonexistentCourseException(course.getCourseCode(), 
                                                 course.getCourseTitle());
        }
    }
    
    
    /**
     * All sections.
     * @return  a list of sections
     */
    @WebMethod(operationName = "getAllSections")
    public List<Section> getAllSections() {
        return this.impl.getAllSections();
    }
    
    /**
     * All sections of a course
     * @param courseCode
     * @return  a list of sections
     */
    @WebMethod(operationName = "getSectionsByCourse")
    public List<Section> getSectionsByCourse(
            @WebParam(name = "courseCode") String courseCode) {
        return this.impl.getSectionsByCourse(courseCode);
    }
    
    /**
     * All currently available sections.
     * @return  a list of sections
     */
    @WebMethod(operationName = "getCurrentSections")
    public List<Section> getCurrentSections() {
        return this.impl.getCurrentSections();
    }
    
    /**
     * All past sections.
     * @return  a list of sections.
     */
    @WebMethod(operationName = "getPastSections")
    public List<Section> getPastSections() {
        return this.impl.getPastSections();
    }
    
    /**
     * All of the future sections.
     * @return  a list of sections
     */
    @WebMethod(operationName = "getFutureSections")
    public List<Section> getFutureSections() {
        return this.impl.getFutureSections();
    } 
    
    /**
     * All of the certifications available.
     * @return  a list of certificates
     */
    @WebMethod(operationName = "getAllCertificates")
    public List<Certificate> getAllCertificates() {
        return this.impl.getAllCertificates();
    }
    
    /**
     * A certificate by certificate code.
     * @param certificateCode
     * @return  a certificate
     */
    @WebMethod(operationName = "getCertificate")
    public Certificate getCertificate(
            @WebParam(name = "certificateCode") String certificateCode) {
        return this.impl.getCertificate(certificateCode);
    }
    
    /**
     * All of the certifications a person has earned
     * @param person    a student
     * @return  a list of certificates
     */
    @WebMethod(operationName = "getCertificatesByPerson")
    public List<Certificate> getCertificatesByPerson(@WebParam(name = "person") Person person) {
        return this.impl.getCertificatesByPerson(person);
    }
    
    /**
     * All Examinations offered.
     * @return  a list of exams
     */
    @WebMethod(operationName = "getAllExaminations")
    public List<Exam> getAllExaminations() {
        return this.impl.getAllExaminations();
    }
    
    /**
     * Past Examinations.
     * @return  a list of exams
     */
    @WebMethod(operationName = "getPastExaminations")
    public List<Exam> getPastExaminations() {
        return this.impl.getPastExaminations();
    }
    
    /**
     * Examinations to be offered in the future.
     * @return  a list of exams
     */
    @WebMethod(operationName = "getFutureExaminations")
    public List<Exam> getFutureExaminations() {
        return this.impl.getFutureExaminations();
    }
    
    /**
     * A transcript for a student.
     * @param person    a student
     * @return  a student's transcript
     */
    @WebMethod(operationName = "getTranscript")
    public Transcript getTranscript(@WebParam(name = "person") Person person) {
        return this.impl.getTranscript(person);
    }
    
    @WebMethod(operationName = "dummy") 
    public String dummy() {
        return "Dummy";
    }
}
