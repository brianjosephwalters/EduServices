/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.business.controllers;

import com.eduservices.db.controllers.PersonJpaController;
import com.eduservices.db.entities.Person;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class PersonController {
    private EntityManagerFactory emf;
    
    public PersonController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public Person getPerson(String id) {
        return new PersonJpaController(emf).findPerson(id);
    }
    
    public List<Person> getAllPersons() {
        return new PersonJpaController(emf).findPersonEntities();
    }
    
    public List<Person> getPerson(String lastName, String firstName) {
        EntityManager em = this.emf.createEntityManager();
        return em.createQuery("SELECT p FROM Person p WHERE p.lastName = :lastName AND p.firstName = :firstName")
                 .setParameter("lastName", lastName)
                 .setParameter("firstName", firstName)
                 .getResultList();
    }
}
