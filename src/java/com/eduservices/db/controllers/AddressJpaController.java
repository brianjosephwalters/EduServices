/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.db.controllers;

import com.eduservices.db.controllers.exceptions.NonexistentEntityException;
import com.eduservices.db.controllers.exceptions.PreexistingEntityException;
import com.eduservices.db.entities.Address;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.eduservices.db.entities.Company;
import java.util.ArrayList;
import java.util.List;
import com.eduservices.db.entities.Person;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class AddressJpaController implements Serializable {

    public AddressJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Address address) throws PreexistingEntityException, Exception {
        if (address.getCompanyList() == null) {
            address.setCompanyList(new ArrayList<Company>());
        }
        if (address.getPersonList() == null) {
            address.setPersonList(new ArrayList<Person>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Company> attachedCompanyList = new ArrayList<Company>();
            for (Company companyListCompanyToAttach : address.getCompanyList()) {
                companyListCompanyToAttach = em.getReference(companyListCompanyToAttach.getClass(), companyListCompanyToAttach.getCompanyCode());
                attachedCompanyList.add(companyListCompanyToAttach);
            }
            address.setCompanyList(attachedCompanyList);
            List<Person> attachedPersonList = new ArrayList<Person>();
            for (Person personListPersonToAttach : address.getPersonList()) {
                personListPersonToAttach = em.getReference(personListPersonToAttach.getClass(), personListPersonToAttach.getSsNum());
                attachedPersonList.add(personListPersonToAttach);
            }
            address.setPersonList(attachedPersonList);
            em.persist(address);
            for (Company companyListCompany : address.getCompanyList()) {
                companyListCompany.getAddressList().add(address);
                companyListCompany = em.merge(companyListCompany);
            }
            for (Person personListPerson : address.getPersonList()) {
                personListPerson.getAddressList().add(address);
                personListPerson = em.merge(personListPerson);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAddress(address.getAddressCode()) != null) {
                throw new PreexistingEntityException("Address " + address + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Address address) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Address persistentAddress = em.find(Address.class, address.getAddressCode());
            List<Company> companyListOld = persistentAddress.getCompanyList();
            List<Company> companyListNew = address.getCompanyList();
            List<Person> personListOld = persistentAddress.getPersonList();
            List<Person> personListNew = address.getPersonList();
            List<Company> attachedCompanyListNew = new ArrayList<Company>();
            for (Company companyListNewCompanyToAttach : companyListNew) {
                companyListNewCompanyToAttach = em.getReference(companyListNewCompanyToAttach.getClass(), companyListNewCompanyToAttach.getCompanyCode());
                attachedCompanyListNew.add(companyListNewCompanyToAttach);
            }
            companyListNew = attachedCompanyListNew;
            address.setCompanyList(companyListNew);
            List<Person> attachedPersonListNew = new ArrayList<Person>();
            for (Person personListNewPersonToAttach : personListNew) {
                personListNewPersonToAttach = em.getReference(personListNewPersonToAttach.getClass(), personListNewPersonToAttach.getSsNum());
                attachedPersonListNew.add(personListNewPersonToAttach);
            }
            personListNew = attachedPersonListNew;
            address.setPersonList(personListNew);
            address = em.merge(address);
            for (Company companyListOldCompany : companyListOld) {
                if (!companyListNew.contains(companyListOldCompany)) {
                    companyListOldCompany.getAddressList().remove(address);
                    companyListOldCompany = em.merge(companyListOldCompany);
                }
            }
            for (Company companyListNewCompany : companyListNew) {
                if (!companyListOld.contains(companyListNewCompany)) {
                    companyListNewCompany.getAddressList().add(address);
                    companyListNewCompany = em.merge(companyListNewCompany);
                }
            }
            for (Person personListOldPerson : personListOld) {
                if (!personListNew.contains(personListOldPerson)) {
                    personListOldPerson.getAddressList().remove(address);
                    personListOldPerson = em.merge(personListOldPerson);
                }
            }
            for (Person personListNewPerson : personListNew) {
                if (!personListOld.contains(personListNewPerson)) {
                    personListNewPerson.getAddressList().add(address);
                    personListNewPerson = em.merge(personListNewPerson);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = address.getAddressCode();
                if (findAddress(id) == null) {
                    throw new NonexistentEntityException("The address with id " + id + " no longer exists.");
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
            Address address;
            try {
                address = em.getReference(Address.class, id);
                address.getAddressCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The address with id " + id + " no longer exists.", enfe);
            }
            List<Company> companyList = address.getCompanyList();
            for (Company companyListCompany : companyList) {
                companyListCompany.getAddressList().remove(address);
                companyListCompany = em.merge(companyListCompany);
            }
            List<Person> personList = address.getPersonList();
            for (Person personListPerson : personList) {
                personListPerson.getAddressList().remove(address);
                personListPerson = em.merge(personListPerson);
            }
            em.remove(address);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Address> findAddressEntities() {
        return findAddressEntities(true, -1, -1);
    }

    public List<Address> findAddressEntities(int maxResults, int firstResult) {
        return findAddressEntities(false, maxResults, firstResult);
    }

    private List<Address> findAddressEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Address.class));
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

    public Address findAddress(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Address.class, id);
        } finally {
            em.close();
        }
    }

    public int getAddressCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Address> rt = cq.from(Address.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
