/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.business.controllers;

import com.eduservices.db.controllers.SectionJpaController;
import com.eduservices.db.entities.Section;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class SectionController {
    EntityManagerFactory emf;

    public SectionController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public List<Section> getAllSections() {
        return new SectionJpaController(emf).findSectionEntities();
    }
    
    public List<Section> getSectionsByCourse(String courseCode) {
        EntityManager em = emf.createEntityManager();
        return em.createNamedQuery("Section.findByCourseCode")
                 .setParameter("courseCode", courseCode)
                 .getResultList();
    }
        
    public List<Section> getCurrentSections() {
        EntityManager em = emf.createEntityManager();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        return em.createQuery("SELECT s FROM Section s WHERE s.sectionPK.year = :year", Section.class)
                 .setParameter("year", year).getResultList();
    }
    
    public List<Section> getFutureSections() {
        EntityManager em = emf.createEntityManager();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        return em.createQuery("SELECT s FROM Section s WHERE s.sectionPK.year > :year", Section.class)
                 .setParameter("year", year).getResultList();    
    }
    
    public List<Section> getPastSections() {
        EntityManager em = emf.createEntityManager();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        return em.createQuery("SELECT s FROM Section s WHERE s.sectionPK.year < :year", Section.class)
                 .setParameter("year", year).getResultList();   
    }
    
}
