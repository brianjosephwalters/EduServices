/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.business.controllers;

import com.eduservices.db.controllers.CourseJpaController;
import com.eduservices.db.controllers.exceptions.IllegalOrphanException;
import com.eduservices.db.controllers.exceptions.NonexistentEntityException;
import com.eduservices.db.controllers.exceptions.PreexistingEntityException;
import com.eduservices.db.entities.Course;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class CourseController {
    private EntityManagerFactory emf;
    
    public CourseController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public List<Course> getAllCourses() {
        return new CourseJpaController(emf).findCourseEntities();
    }
    
    public List<Course> getActiveCourses() {
        EntityManager em = emf.createEntityManager();
        return em.createNamedQuery("Course.findByStatus")
                 .setParameter("status", "active").getResultList();
    }
    
    public List<Course> getInactiveCourses() {
        EntityManager em = emf.createEntityManager();
        return em.createNamedQuery("Course.findByStatus")
                 .setParameter("status", "inactive").getResultList();
    }
    
    public Course getCourse(String courseCode) {
        EntityManager em = emf.createEntityManager();
        return (Course)em.createNamedQuery("Course.findByCourseCode")
                 .setParameter("courseCode", courseCode).getSingleResult();
    }
    
    public void addCourse(Course course) 
            throws PreexistingEntityException, Exception {
        // Change to proper course class.
        new CourseJpaController(emf).create(course);
    }
    
    public void updateCourse(Course course) 
            throws IllegalOrphanException, NonexistentEntityException, Exception {
        new CourseJpaController(emf).edit(course);
    }
    
}
