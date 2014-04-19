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
import java.util.ArrayList;
import java.util.List;
import com.eduservices.db.entities.Person;
import com.eduservices.db.entities.PhoneNumber;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class PhoneNumberJpaController implements Serializable {

    public PhoneNumberJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PhoneNumber phoneNumber) throws PreexistingEntityException, Exception {
        if (phoneNumber.getCompanyList() == null) {
            phoneNumber.setCompanyList(new ArrayList<Company>());
        }
        if (phoneNumber.getPersonList() == null) {
            phoneNumber.setPersonList(new ArrayList<Person>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Company> attachedCompanyList = new ArrayList<Company>();
            for (Company companyListCompanyToAttach : phoneNumber.getCompanyList()) {
                companyListCompanyToAttach = em.getReference(companyListCompanyToAttach.getClass(), companyListCompanyToAttach.getCompanyCode());
                attachedCompanyList.add(companyListCompanyToAttach);
            }
            phoneNumber.setCompanyList(attachedCompanyList);
            List<Person> attachedPersonList = new ArrayList<Person>();
            for (Person personListPersonToAttach : phoneNumber.getPersonList()) {
                personListPersonToAttach = em.getReference(personListPersonToAttach.getClass(), personListPersonToAttach.getSsNum());
                attachedPersonList.add(personListPersonToAttach);
            }
            phoneNumber.setPersonList(attachedPersonList);
            em.persist(phoneNumber);
            for (Company companyListCompany : phoneNumber.getCompanyList()) {
                companyListCompany.getPhoneNumberList().add(phoneNumber);
                companyListCompany = em.merge(companyListCompany);
            }
            for (Person personListPerson : phoneNumber.getPersonList()) {
                personListPerson.getPhoneNumberList().add(phoneNumber);
                personListPerson = em.merge(personListPerson);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPhoneNumber(phoneNumber.getPhoneCode()) != null) {
                throw new PreexistingEntityException("PhoneNumber " + phoneNumber + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PhoneNumber phoneNumber) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PhoneNumber persistentPhoneNumber = em.find(PhoneNumber.class, phoneNumber.getPhoneCode());
            List<Company> companyListOld = persistentPhoneNumber.getCompanyList();
            List<Company> companyListNew = phoneNumber.getCompanyList();
            List<Person> personListOld = persistentPhoneNumber.getPersonList();
            List<Person> personListNew = phoneNumber.getPersonList();
            List<Company> attachedCompanyListNew = new ArrayList<Company>();
            for (Company companyListNewCompanyToAttach : companyListNew) {
                companyListNewCompanyToAttach = em.getReference(companyListNewCompanyToAttach.getClass(), companyListNewCompanyToAttach.getCompanyCode());
                attachedCompanyListNew.add(companyListNewCompanyToAttach);
            }
            companyListNew = attachedCompanyListNew;
            phoneNumber.setCompanyList(companyListNew);
            List<Person> attachedPersonListNew = new ArrayList<Person>();
            for (Person personListNewPersonToAttach : personListNew) {
                personListNewPersonToAttach = em.getReference(personListNewPersonToAttach.getClass(), personListNewPersonToAttach.getSsNum());
                attachedPersonListNew.add(personListNewPersonToAttach);
            }
            personListNew = attachedPersonListNew;
            phoneNumber.setPersonList(personListNew);
            phoneNumber = em.merge(phoneNumber);
            for (Company companyListOldCompany : companyListOld) {
                if (!companyListNew.contains(companyListOldCompany)) {
                    companyListOldCompany.getPhoneNumberList().remove(phoneNumber);
                    companyListOldCompany = em.merge(companyListOldCompany);
                }
            }
            for (Company companyListNewCompany : companyListNew) {
                if (!companyListOld.contains(companyListNewCompany)) {
                    companyListNewCompany.getPhoneNumberList().add(phoneNumber);
                    companyListNewCompany = em.merge(companyListNewCompany);
                }
            }
            for (Person personListOldPerson : personListOld) {
                if (!personListNew.contains(personListOldPerson)) {
                    personListOldPerson.getPhoneNumberList().remove(phoneNumber);
                    personListOldPerson = em.merge(personListOldPerson);
                }
            }
            for (Person personListNewPerson : personListNew) {
                if (!personListOld.contains(personListNewPerson)) {
                    personListNewPerson.getPhoneNumberList().add(phoneNumber);
                    personListNewPerson = em.merge(personListNewPerson);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = phoneNumber.getPhoneCode();
                if (findPhoneNumber(id) == null) {
                    throw new NonexistentEntityException("The phoneNumber with id " + id + " no longer exists.");
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
            PhoneNumber phoneNumber;
            try {
                phoneNumber = em.getReference(PhoneNumber.class, id);
                phoneNumber.getPhoneCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The phoneNumber with id " + id + " no longer exists.", enfe);
            }
            List<Company> companyList = phoneNumber.getCompanyList();
            for (Company companyListCompany : companyList) {
                companyListCompany.getPhoneNumberList().remove(phoneNumber);
                companyListCompany = em.merge(companyListCompany);
            }
            List<Person> personList = phoneNumber.getPersonList();
            for (Person personListPerson : personList) {
                personListPerson.getPhoneNumberList().remove(phoneNumber);
                personListPerson = em.merge(personListPerson);
            }
            em.remove(phoneNumber);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PhoneNumber> findPhoneNumberEntities() {
        return findPhoneNumberEntities(true, -1, -1);
    }

    public List<PhoneNumber> findPhoneNumberEntities(int maxResults, int firstResult) {
        return findPhoneNumberEntities(false, maxResults, firstResult);
    }

    private List<PhoneNumber> findPhoneNumberEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PhoneNumber.class));
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

    public PhoneNumber findPhoneNumber(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PhoneNumber.class, id);
        } finally {
            em.close();
        }
    }

    public int getPhoneNumberCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PhoneNumber> rt = cq.from(PhoneNumber.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
