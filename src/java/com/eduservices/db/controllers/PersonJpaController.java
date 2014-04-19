/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.db.controllers;

import com.eduservices.db.controllers.exceptions.IllegalOrphanException;
import com.eduservices.db.controllers.exceptions.NonexistentEntityException;
import com.eduservices.db.controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.eduservices.db.entities.Certificate;
import java.util.ArrayList;
import java.util.List;
import com.eduservices.db.entities.PhoneNumber;
import com.eduservices.db.entities.Address;
import com.eduservices.db.entities.Attended;
import com.eduservices.db.entities.ExamTaken;
import com.eduservices.db.entities.Person;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class PersonJpaController implements Serializable {

    public PersonJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Person person) throws PreexistingEntityException, Exception {
        if (person.getCertificateList() == null) {
            person.setCertificateList(new ArrayList<Certificate>());
        }
        if (person.getPhoneNumberList() == null) {
            person.setPhoneNumberList(new ArrayList<PhoneNumber>());
        }
        if (person.getAddressList() == null) {
            person.setAddressList(new ArrayList<Address>());
        }
        if (person.getAttendedList() == null) {
            person.setAttendedList(new ArrayList<Attended>());
        }
        if (person.getExamTakenList() == null) {
            person.setExamTakenList(new ArrayList<ExamTaken>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Certificate> attachedCertificateList = new ArrayList<Certificate>();
            for (Certificate certificateListCertificateToAttach : person.getCertificateList()) {
                certificateListCertificateToAttach = em.getReference(certificateListCertificateToAttach.getClass(), certificateListCertificateToAttach.getCertificateCode());
                attachedCertificateList.add(certificateListCertificateToAttach);
            }
            person.setCertificateList(attachedCertificateList);
            List<PhoneNumber> attachedPhoneNumberList = new ArrayList<PhoneNumber>();
            for (PhoneNumber phoneNumberListPhoneNumberToAttach : person.getPhoneNumberList()) {
                phoneNumberListPhoneNumberToAttach = em.getReference(phoneNumberListPhoneNumberToAttach.getClass(), phoneNumberListPhoneNumberToAttach.getPhoneCode());
                attachedPhoneNumberList.add(phoneNumberListPhoneNumberToAttach);
            }
            person.setPhoneNumberList(attachedPhoneNumberList);
            List<Address> attachedAddressList = new ArrayList<Address>();
            for (Address addressListAddressToAttach : person.getAddressList()) {
                addressListAddressToAttach = em.getReference(addressListAddressToAttach.getClass(), addressListAddressToAttach.getAddressCode());
                attachedAddressList.add(addressListAddressToAttach);
            }
            person.setAddressList(attachedAddressList);
            List<Attended> attachedAttendedList = new ArrayList<Attended>();
            for (Attended attendedListAttendedToAttach : person.getAttendedList()) {
                attendedListAttendedToAttach = em.getReference(attendedListAttendedToAttach.getClass(), attendedListAttendedToAttach.getAttendedPK());
                attachedAttendedList.add(attendedListAttendedToAttach);
            }
            person.setAttendedList(attachedAttendedList);
            List<ExamTaken> attachedExamTakenList = new ArrayList<ExamTaken>();
            for (ExamTaken examTakenListExamTakenToAttach : person.getExamTakenList()) {
                examTakenListExamTakenToAttach = em.getReference(examTakenListExamTakenToAttach.getClass(), examTakenListExamTakenToAttach.getExamTakenPK());
                attachedExamTakenList.add(examTakenListExamTakenToAttach);
            }
            person.setExamTakenList(attachedExamTakenList);
            em.persist(person);
            for (Certificate certificateListCertificate : person.getCertificateList()) {
                certificateListCertificate.getPersonList().add(person);
                certificateListCertificate = em.merge(certificateListCertificate);
            }
            for (PhoneNumber phoneNumberListPhoneNumber : person.getPhoneNumberList()) {
                phoneNumberListPhoneNumber.getPersonList().add(person);
                phoneNumberListPhoneNumber = em.merge(phoneNumberListPhoneNumber);
            }
            for (Address addressListAddress : person.getAddressList()) {
                addressListAddress.getPersonList().add(person);
                addressListAddress = em.merge(addressListAddress);
            }
            for (Attended attendedListAttended : person.getAttendedList()) {
                Person oldPersonOfAttendedListAttended = attendedListAttended.getPerson();
                attendedListAttended.setPerson(person);
                attendedListAttended = em.merge(attendedListAttended);
                if (oldPersonOfAttendedListAttended != null) {
                    oldPersonOfAttendedListAttended.getAttendedList().remove(attendedListAttended);
                    oldPersonOfAttendedListAttended = em.merge(oldPersonOfAttendedListAttended);
                }
            }
            for (ExamTaken examTakenListExamTaken : person.getExamTakenList()) {
                Person oldPersonOfExamTakenListExamTaken = examTakenListExamTaken.getPerson();
                examTakenListExamTaken.setPerson(person);
                examTakenListExamTaken = em.merge(examTakenListExamTaken);
                if (oldPersonOfExamTakenListExamTaken != null) {
                    oldPersonOfExamTakenListExamTaken.getExamTakenList().remove(examTakenListExamTaken);
                    oldPersonOfExamTakenListExamTaken = em.merge(oldPersonOfExamTakenListExamTaken);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPerson(person.getSsNum()) != null) {
                throw new PreexistingEntityException("Person " + person + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Person person) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Person persistentPerson = em.find(Person.class, person.getSsNum());
            List<Certificate> certificateListOld = persistentPerson.getCertificateList();
            List<Certificate> certificateListNew = person.getCertificateList();
            List<PhoneNumber> phoneNumberListOld = persistentPerson.getPhoneNumberList();
            List<PhoneNumber> phoneNumberListNew = person.getPhoneNumberList();
            List<Address> addressListOld = persistentPerson.getAddressList();
            List<Address> addressListNew = person.getAddressList();
            List<Attended> attendedListOld = persistentPerson.getAttendedList();
            List<Attended> attendedListNew = person.getAttendedList();
            List<ExamTaken> examTakenListOld = persistentPerson.getExamTakenList();
            List<ExamTaken> examTakenListNew = person.getExamTakenList();
            List<String> illegalOrphanMessages = null;
            for (Attended attendedListOldAttended : attendedListOld) {
                if (!attendedListNew.contains(attendedListOldAttended)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Attended " + attendedListOldAttended + " since its person field is not nullable.");
                }
            }
            for (ExamTaken examTakenListOldExamTaken : examTakenListOld) {
                if (!examTakenListNew.contains(examTakenListOldExamTaken)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ExamTaken " + examTakenListOldExamTaken + " since its person field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Certificate> attachedCertificateListNew = new ArrayList<Certificate>();
            for (Certificate certificateListNewCertificateToAttach : certificateListNew) {
                certificateListNewCertificateToAttach = em.getReference(certificateListNewCertificateToAttach.getClass(), certificateListNewCertificateToAttach.getCertificateCode());
                attachedCertificateListNew.add(certificateListNewCertificateToAttach);
            }
            certificateListNew = attachedCertificateListNew;
            person.setCertificateList(certificateListNew);
            List<PhoneNumber> attachedPhoneNumberListNew = new ArrayList<PhoneNumber>();
            for (PhoneNumber phoneNumberListNewPhoneNumberToAttach : phoneNumberListNew) {
                phoneNumberListNewPhoneNumberToAttach = em.getReference(phoneNumberListNewPhoneNumberToAttach.getClass(), phoneNumberListNewPhoneNumberToAttach.getPhoneCode());
                attachedPhoneNumberListNew.add(phoneNumberListNewPhoneNumberToAttach);
            }
            phoneNumberListNew = attachedPhoneNumberListNew;
            person.setPhoneNumberList(phoneNumberListNew);
            List<Address> attachedAddressListNew = new ArrayList<Address>();
            for (Address addressListNewAddressToAttach : addressListNew) {
                addressListNewAddressToAttach = em.getReference(addressListNewAddressToAttach.getClass(), addressListNewAddressToAttach.getAddressCode());
                attachedAddressListNew.add(addressListNewAddressToAttach);
            }
            addressListNew = attachedAddressListNew;
            person.setAddressList(addressListNew);
            List<Attended> attachedAttendedListNew = new ArrayList<Attended>();
            for (Attended attendedListNewAttendedToAttach : attendedListNew) {
                attendedListNewAttendedToAttach = em.getReference(attendedListNewAttendedToAttach.getClass(), attendedListNewAttendedToAttach.getAttendedPK());
                attachedAttendedListNew.add(attendedListNewAttendedToAttach);
            }
            attendedListNew = attachedAttendedListNew;
            person.setAttendedList(attendedListNew);
            List<ExamTaken> attachedExamTakenListNew = new ArrayList<ExamTaken>();
            for (ExamTaken examTakenListNewExamTakenToAttach : examTakenListNew) {
                examTakenListNewExamTakenToAttach = em.getReference(examTakenListNewExamTakenToAttach.getClass(), examTakenListNewExamTakenToAttach.getExamTakenPK());
                attachedExamTakenListNew.add(examTakenListNewExamTakenToAttach);
            }
            examTakenListNew = attachedExamTakenListNew;
            person.setExamTakenList(examTakenListNew);
            person = em.merge(person);
            for (Certificate certificateListOldCertificate : certificateListOld) {
                if (!certificateListNew.contains(certificateListOldCertificate)) {
                    certificateListOldCertificate.getPersonList().remove(person);
                    certificateListOldCertificate = em.merge(certificateListOldCertificate);
                }
            }
            for (Certificate certificateListNewCertificate : certificateListNew) {
                if (!certificateListOld.contains(certificateListNewCertificate)) {
                    certificateListNewCertificate.getPersonList().add(person);
                    certificateListNewCertificate = em.merge(certificateListNewCertificate);
                }
            }
            for (PhoneNumber phoneNumberListOldPhoneNumber : phoneNumberListOld) {
                if (!phoneNumberListNew.contains(phoneNumberListOldPhoneNumber)) {
                    phoneNumberListOldPhoneNumber.getPersonList().remove(person);
                    phoneNumberListOldPhoneNumber = em.merge(phoneNumberListOldPhoneNumber);
                }
            }
            for (PhoneNumber phoneNumberListNewPhoneNumber : phoneNumberListNew) {
                if (!phoneNumberListOld.contains(phoneNumberListNewPhoneNumber)) {
                    phoneNumberListNewPhoneNumber.getPersonList().add(person);
                    phoneNumberListNewPhoneNumber = em.merge(phoneNumberListNewPhoneNumber);
                }
            }
            for (Address addressListOldAddress : addressListOld) {
                if (!addressListNew.contains(addressListOldAddress)) {
                    addressListOldAddress.getPersonList().remove(person);
                    addressListOldAddress = em.merge(addressListOldAddress);
                }
            }
            for (Address addressListNewAddress : addressListNew) {
                if (!addressListOld.contains(addressListNewAddress)) {
                    addressListNewAddress.getPersonList().add(person);
                    addressListNewAddress = em.merge(addressListNewAddress);
                }
            }
            for (Attended attendedListNewAttended : attendedListNew) {
                if (!attendedListOld.contains(attendedListNewAttended)) {
                    Person oldPersonOfAttendedListNewAttended = attendedListNewAttended.getPerson();
                    attendedListNewAttended.setPerson(person);
                    attendedListNewAttended = em.merge(attendedListNewAttended);
                    if (oldPersonOfAttendedListNewAttended != null && !oldPersonOfAttendedListNewAttended.equals(person)) {
                        oldPersonOfAttendedListNewAttended.getAttendedList().remove(attendedListNewAttended);
                        oldPersonOfAttendedListNewAttended = em.merge(oldPersonOfAttendedListNewAttended);
                    }
                }
            }
            for (ExamTaken examTakenListNewExamTaken : examTakenListNew) {
                if (!examTakenListOld.contains(examTakenListNewExamTaken)) {
                    Person oldPersonOfExamTakenListNewExamTaken = examTakenListNewExamTaken.getPerson();
                    examTakenListNewExamTaken.setPerson(person);
                    examTakenListNewExamTaken = em.merge(examTakenListNewExamTaken);
                    if (oldPersonOfExamTakenListNewExamTaken != null && !oldPersonOfExamTakenListNewExamTaken.equals(person)) {
                        oldPersonOfExamTakenListNewExamTaken.getExamTakenList().remove(examTakenListNewExamTaken);
                        oldPersonOfExamTakenListNewExamTaken = em.merge(oldPersonOfExamTakenListNewExamTaken);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = person.getSsNum();
                if (findPerson(id) == null) {
                    throw new NonexistentEntityException("The person with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Person person;
            try {
                person = em.getReference(Person.class, id);
                person.getSsNum();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The person with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Attended> attendedListOrphanCheck = person.getAttendedList();
            for (Attended attendedListOrphanCheckAttended : attendedListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Person (" + person + ") cannot be destroyed since the Attended " + attendedListOrphanCheckAttended + " in its attendedList field has a non-nullable person field.");
            }
            List<ExamTaken> examTakenListOrphanCheck = person.getExamTakenList();
            for (ExamTaken examTakenListOrphanCheckExamTaken : examTakenListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Person (" + person + ") cannot be destroyed since the ExamTaken " + examTakenListOrphanCheckExamTaken + " in its examTakenList field has a non-nullable person field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Certificate> certificateList = person.getCertificateList();
            for (Certificate certificateListCertificate : certificateList) {
                certificateListCertificate.getPersonList().remove(person);
                certificateListCertificate = em.merge(certificateListCertificate);
            }
            List<PhoneNumber> phoneNumberList = person.getPhoneNumberList();
            for (PhoneNumber phoneNumberListPhoneNumber : phoneNumberList) {
                phoneNumberListPhoneNumber.getPersonList().remove(person);
                phoneNumberListPhoneNumber = em.merge(phoneNumberListPhoneNumber);
            }
            List<Address> addressList = person.getAddressList();
            for (Address addressListAddress : addressList) {
                addressListAddress.getPersonList().remove(person);
                addressListAddress = em.merge(addressListAddress);
            }
            em.remove(person);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Person> findPersonEntities() {
        return findPersonEntities(true, -1, -1);
    }

    public List<Person> findPersonEntities(int maxResults, int firstResult) {
        return findPersonEntities(false, maxResults, firstResult);
    }

    private List<Person> findPersonEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Person.class));
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

    public Person findPerson(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Person.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Person> rt = cq.from(Person.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
