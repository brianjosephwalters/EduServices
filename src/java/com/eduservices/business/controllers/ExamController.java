/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.business.controllers;

import com.eduservices.db.controllers.ExamJpaController;
import com.eduservices.db.controllers.ExamTypeJpaController;
import com.eduservices.db.entities.Exam;
import com.eduservices.db.entities.ExamTaken;
import com.eduservices.db.entities.ExamType;
import com.eduservices.db.entities.Person;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class ExamController {
    private final EntityManagerFactory emf;
    
    public ExamController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public List<ExamType> getAllExamTypes() {
        return new ExamTypeJpaController(emf).findExamTypeEntities();
    }
    
    public List<Exam> getAllExams() {
        return new ExamJpaController(emf).findExamEntities();
    }
    
    public List<Exam> getUpcomingExams() {
        EntityManager em = this.emf.createEntityManager();
        Calendar cal = Calendar.getInstance();
        return em.createQuery("SELECT e FROM Exam e WHERE e.examDate >= :examDate", Exam.class)
                 .setParameter("examDate", cal.getTime())
                 .getResultList();
    }
    
    public List<Exam> getPastExams() {
        EntityManager em = this.emf.createEntityManager();
        Calendar cal = Calendar.getInstance();
        return em.createQuery("SELECT e FROM Exam e WHERE e.examDate < :examDate", Exam.class)
                 .setParameter("examDate", cal.getTime())
                 .getResultList();
    }
    
    public List<ExamTaken> getExamsForPerson(Person person) {
        EntityManager em = this.emf.createEntityManager();
        return em.createNamedQuery("ExamTaken.findByPersonCode", ExamTaken.class)
                 .setParameter("personCode", person.getSsNum())
                 .getResultList();
    }
}
