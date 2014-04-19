/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.business.models;

/**
 *
 * @author bjw
 */
public class CourseRecord {
    private String courseName;
    private String courseCode;
    private String sectionCode;
    private String year;
    private String grade;

    public CourseRecord(String courseName, String courseCode,
                        String sectionCode, String year, String grade) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.sectionCode = sectionCode;
        this.year = year;
        this.grade = grade;
    }
    /**
     * @return the courseName
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * @return the courseCode
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * @return the sectionCode
     */
    public String getSectionCode() {
        return sectionCode;
    }

    /**
     * @return the year
     */
    public String getYear() {
        return year;
    }

    /**
     * @return the grade
     */
    public String getGrade() {
        return grade;
    }

    /**
     * @param courseName the courseName to set
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * @param courseCode the courseCode to set
     */
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    /**
     * @param sectionCode the sectionCode to set
     */
    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    /**
     * @param year the year to set
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * @param grade the grade to set
     */
    public void setGrade(String grade) {
        this.grade = grade;
    }
}
