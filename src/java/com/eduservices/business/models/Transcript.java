/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.business.models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bjw
 */
public class Transcript {
    private String dateGenerated;
    private String studentName;
    private String studentID;
    private List<CourseRecord> courses;
    private List<CertificationRecord> certifications;

    public Transcript(String dateGenerated, String studentName,String studentID) {
        this.dateGenerated = dateGenerated;
        this.studentName = studentName;
        this.studentID = studentID;
        this.courses = new ArrayList<CourseRecord>();
        this.certifications = new ArrayList<CertificationRecord>();
    }
    
    /**
     * @return the dateGenerated
     */
    public String getDateGenerated() {
        return dateGenerated;
    }

    /**
     * @return the studentName
     */
    public String getStudentName() {
        return studentName;
    }

    /**
     * @return the studentID
     */
    public String getStudentID() {
        return studentID;
    }

    /**
     * @return the courses
     */
    public List<CourseRecord> getCourses() {
        return courses;
    }

    /**
     * @return the certifications
     */
    public List<CertificationRecord> getCertifications() {
        return certifications;
    }

    /**
     * @param dateGenerated the dateGenerated to set
     */
    public void setDateGenerated(String dateGenerated) {
        this.dateGenerated = dateGenerated;
    }

    /**
     * @param studentName the studentName to set
     */
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    /**
     * @param studentID the studentID to set
     */
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    /**
     * @param courses the courses to set
     */
    public void setCourses(List<CourseRecord> courses) {
        this.courses = courses;
    }
    
    
    public void addCourse(CourseRecord course) {
        this.courses.add(course);
    }

    /**
     * @param certifications the certifications to set
     */
    public void setCertifications(List<CertificationRecord> certifications) {
        this.certifications = certifications;
    }
    
    
    public void addCertification(CertificationRecord certification) {
        this.certifications.add(certification);
    }
    
}
