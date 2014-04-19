/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.business.controllers;

import com.eduservices.db.controllers.AttendedJpaController;
import com.eduservices.db.entities.Attended;
import com.eduservices.db.entities.Person;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class AttendedController {
    private EntityManagerFactory emf;
    
    public AttendedController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public List<Attended> getAttendedByPerson(Person person) {
        EntityManager em = this.emf.createEntityManager();
        return em.createNamedQuery("Attended.findByPersonCode", Attended.class)
                 .setParameter("personCode", person.getSsNum())
                 .getResultList();
    }
    
}
