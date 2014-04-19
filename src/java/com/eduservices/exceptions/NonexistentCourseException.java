/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.exceptions;

/**
 *
 * @author bjw
 */
public class NonexistentCourseException extends Exception {
    String courseCode;
    String courseTitle;
    /**
     * Creates a new instance of <code>NonexistentCourseException</code> without
     * detail message.
     */
    public NonexistentCourseException(String courseCode, String courseTitle) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
    }
    
    public String getCourseCode() {
        return this.courseCode;
    }
    
    public String getCourseTitle() {
        return this.courseTitle;
    }

    @Override
    public String getMessage() {
        return "Course does not exist: " + this.courseCode + ", " + this.courseTitle;
    }
}
