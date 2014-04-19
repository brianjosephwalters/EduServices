/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.business.controllers;

import com.eduservices.db.controllers.CertificateJpaController;
import com.eduservices.db.entities.Certificate;
import com.eduservices.db.entities.Person;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class CertificateController {
    private EntityManagerFactory emf;
    
    public CertificateController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public List<Certificate> getAllCertificates() {
        return new CertificateJpaController(emf).findCertificateEntities();
    }
    
    public Certificate getCertificate(String certificateCode) {
        EntityManager em = emf.createEntityManager();
        return (Certificate)em.createNamedQuery("Certificate.findByCertificateCode")
                              .setParameter("certificateCode", certificateCode)
                              .getSingleResult();
    }
    
    public List<Certificate> getCertificatesByPerson(Person person) {
        EntityManager em = emf.createEntityManager();
        return em.createQuery("SELECT c " + 
                              "FROM Certificate c, IN(c.personCollection) p " +
                              "WHERE p.personCode = :personCode")
                .setParameter("personCode", person.getSsNum())
                .getResultList();
    }
    
}
