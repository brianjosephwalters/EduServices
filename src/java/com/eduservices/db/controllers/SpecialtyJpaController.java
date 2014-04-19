/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.db.controllers;

import com.eduservices.db.controllers.exceptions.NonexistentEntityException;
import com.eduservices.db.controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.eduservices.db.entities.Company;
import com.eduservices.db.entities.Specialty;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class SpecialtyJpaController implements Serializable {

    public SpecialtyJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Specialty specialty) throws PreexistingEntityException, Exception {
        if (specialty.getCompanyList() == null) {
            specialty.setCompanyList(new ArrayList<Company>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Company> attachedCompanyList = new ArrayList<Company>();
            for (Company companyListCompanyToAttach : specialty.getCompanyList()) {
                companyListCompanyToAttach = em.getReference(companyListCompanyToAttach.getClass(), companyListCompanyToAttach.getCompanyCode());
                attachedCompanyList.add(companyListCompanyToAttach);
            }
            specialty.setCompanyList(attachedCompanyList);
            em.persist(specialty);
            for (Company companyListCompany : specialty.getCompanyList()) {
                companyListCompany.getSpecialtyList().add(specialty);
                companyListCompany = em.merge(companyListCompany);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSpecialty(specialty.getSpecialtyCode()) != null) {
                throw new PreexistingEntityException("Specialty " + specialty + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Specialty specialty) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Specialty persistentSpecialty = em.find(Specialty.class, specialty.getSpecialtyCode());
            List<Company> companyListOld = persistentSpecialty.getCompanyList();
            List<Company> companyListNew = specialty.getCompanyList();
            List<Company> attachedCompanyListNew = new ArrayList<Company>();
            for (Company companyListNewCompanyToAttach : companyListNew) {
                companyListNewCompanyToAttach = em.getReference(companyListNewCompanyToAttach.getClass(), companyListNewCompanyToAttach.getCompanyCode());
                attachedCompanyListNew.add(companyListNewCompanyToAttach);
            }
            companyListNew = attachedCompanyListNew;
            specialty.setCompanyList(companyListNew);
            specialty = em.merge(specialty);
            for (Company companyListOldCompany : companyListOld) {
                if (!companyListNew.contains(companyListOldCompany)) {
                    companyListOldCompany.getSpecialtyList().remove(specialty);
                    companyListOldCompany = em.merge(companyListOldCompany);
                }
            }
            for (Company companyListNewCompany : companyListNew) {
                if (!companyListOld.contains(companyListNewCompany)) {
                    companyListNewCompany.getSpecialtyList().add(specialty);
                    companyListNewCompany = em.merge(companyListNewCompany);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = specialty.getSpecialtyCode();
                if (findSpecialty(id) == null) {
                    throw new NonexistentEntityException("The specialty with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Specialty specialty;
            try {
                specialty = em.getReference(Specialty.class, id);
                specialty.getSpecialtyCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The specialty with id " + id + " no longer exists.", enfe);
            }
            List<Company> companyList = specialty.getCompanyList();
            for (Company companyListCompany : companyList) {
                companyListCompany.getSpecialtyList().remove(specialty);
                companyListCompany = em.merge(companyListCompany);
            }
            em.remove(specialty);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Specialty> findSpecialtyEntities() {
        return findSpecialtyEntities(true, -1, -1);
    }

    public List<Specialty> findSpecialtyEntities(int maxResults, int firstResult) {
        return findSpecialtyEntities(false, maxResults, firstResult);
    }

    private List<Specialty> findSpecialtyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Specialty.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Specialty findSpecialty(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Specialty.class, id);
        } finally {
            em.close();
        }
    }

    public int getSpecialtyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Specialty> rt = cq.from(Specialty.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
