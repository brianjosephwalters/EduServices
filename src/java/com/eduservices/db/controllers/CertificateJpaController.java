/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.db.controllers;

import com.eduservices.db.controllers.exceptions.IllegalOrphanException;
import com.eduservices.db.controllers.exceptions.NonexistentEntityException;
import com.eduservices.db.controllers.exceptions.PreexistingEntityException;
import com.eduservices.db.entities.Certificate;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.eduservices.db.entities.Tool;
import com.eduservices.db.entities.Company;
import com.eduservices.db.entities.Course;
import java.util.ArrayList;
import java.util.List;
import com.eduservices.db.entities.Person;
import com.eduservices.db.entities.ExamType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class CertificateJpaController implements Serializable {

    public CertificateJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Certificate certificate) throws PreexistingEntityException, Exception {
        if (certificate.getCourseList() == null) {
            certificate.setCourseList(new ArrayList<Course>());
        }
        if (certificate.getPersonList() == null) {
            certificate.setPersonList(new ArrayList<Person>());
        }
        if (certificate.getExamTypeList() == null) {
            certificate.setExamTypeList(new ArrayList<ExamType>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tool toolCode = certificate.getToolCode();
            if (toolCode != null) {
                toolCode = em.getReference(toolCode.getClass(), toolCode.getToolCode());
                certificate.setToolCode(toolCode);
            }
            Company companyCode = certificate.getCompanyCode();
            if (companyCode != null) {
                companyCode = em.getReference(companyCode.getClass(), companyCode.getCompanyCode());
                certificate.setCompanyCode(companyCode);
            }
            List<Course> attachedCourseList = new ArrayList<Course>();
            for (Course courseListCourseToAttach : certificate.getCourseList()) {
                courseListCourseToAttach = em.getReference(courseListCourseToAttach.getClass(), courseListCourseToAttach.getCourseCode());
                attachedCourseList.add(courseListCourseToAttach);
            }
            certificate.setCourseList(attachedCourseList);
            List<Person> attachedPersonList = new ArrayList<Person>();
            for (Person personListPersonToAttach : certificate.getPersonList()) {
                personListPersonToAttach = em.getReference(personListPersonToAttach.getClass(), personListPersonToAttach.getSsNum());
                attachedPersonList.add(personListPersonToAttach);
            }
            certificate.setPersonList(attachedPersonList);
            List<ExamType> attachedExamTypeList = new ArrayList<ExamType>();
            for (ExamType examTypeListExamTypeToAttach : certificate.getExamTypeList()) {
                examTypeListExamTypeToAttach = em.getReference(examTypeListExamTypeToAttach.getClass(), examTypeListExamTypeToAttach.getExamTypeCode());
                attachedExamTypeList.add(examTypeListExamTypeToAttach);
            }
            certificate.setExamTypeList(attachedExamTypeList);
            em.persist(certificate);
            if (toolCode != null) {
                toolCode.getCertificateList().add(certificate);
                toolCode = em.merge(toolCode);
            }
            if (companyCode != null) {
                companyCode.getCertificateList().add(certificate);
                companyCode = em.merge(companyCode);
            }
            for (Course courseListCourse : certificate.getCourseList()) {
                courseListCourse.getCertificateList().add(certificate);
                courseListCourse = em.merge(courseListCourse);
            }
            for (Person personListPerson : certificate.getPersonList()) {
                personListPerson.getCertificateList().add(certificate);
                personListPerson = em.merge(personListPerson);
            }
            for (ExamType examTypeListExamType : certificate.getExamTypeList()) {
                Certificate oldCertificateCodeOfExamTypeListExamType = examTypeListExamType.getCertificateCode();
                examTypeListExamType.setCertificateCode(certificate);
                examTypeListExamType = em.merge(examTypeListExamType);
                if (oldCertificateCodeOfExamTypeListExamType != null) {
                    oldCertificateCodeOfExamTypeListExamType.getExamTypeList().remove(examTypeListExamType);
                    oldCertificateCodeOfExamTypeListExamType = em.merge(oldCertificateCodeOfExamTypeListExamType);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCertificate(certificate.getCertificateCode()) != null) {
                throw new PreexistingEntityException("Certificate " + certificate + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Certificate certificate) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Certificate persistentCertificate = em.find(Certificate.class, certificate.getCertificateCode());
            Tool toolCodeOld = persistentCertificate.getToolCode();
            Tool toolCodeNew = certificate.getToolCode();
            Company companyCodeOld = persistentCertificate.getCompanyCode();
            Company companyCodeNew = certificate.getCompanyCode();
            List<Course> courseListOld = persistentCertificate.getCourseList();
            List<Course> courseListNew = certificate.getCourseList();
            List<Person> personListOld = persistentCertificate.getPersonList();
            List<Person> personListNew = certificate.getPersonList();
            List<ExamType> examTypeListOld = persistentCertificate.getExamTypeList();
            List<ExamType> examTypeListNew = certificate.getExamTypeList();
            List<String> illegalOrphanMessages = null;
            for (ExamType examTypeListOldExamType : examTypeListOld) {
                if (!examTypeListNew.contains(examTypeListOldExamType)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ExamType " + examTypeListOldExamType + " since its certificateCode field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (toolCodeNew != null) {
                toolCodeNew = em.getReference(toolCodeNew.getClass(), toolCodeNew.getToolCode());
                certificate.setToolCode(toolCodeNew);
            }
            if (companyCodeNew != null) {
                companyCodeNew = em.getReference(companyCodeNew.getClass(), companyCodeNew.getCompanyCode());
                certificate.setCompanyCode(companyCodeNew);
            }
            List<Course> attachedCourseListNew = new ArrayList<Course>();
            for (Course courseListNewCourseToAttach : courseListNew) {
                courseListNewCourseToAttach = em.getReference(courseListNewCourseToAttach.getClass(), courseListNewCourseToAttach.getCourseCode());
                attachedCourseListNew.add(courseListNewCourseToAttach);
            }
            courseListNew = attachedCourseListNew;
            certificate.setCourseList(courseListNew);
            List<Person> attachedPersonListNew = new ArrayList<Person>();
            for (Person personListNewPersonToAttach : personListNew) {
                personListNewPersonToAttach = em.getReference(personListNewPersonToAttach.getClass(), personListNewPersonToAttach.getSsNum());
                attachedPersonListNew.add(personListNewPersonToAttach);
            }
            personListNew = attachedPersonListNew;
            certificate.setPersonList(personListNew);
            List<ExamType> attachedExamTypeListNew = new ArrayList<ExamType>();
            for (ExamType examTypeListNewExamTypeToAttach : examTypeListNew) {
                examTypeListNewExamTypeToAttach = em.getReference(examTypeListNewExamTypeToAttach.getClass(), examTypeListNewExamTypeToAttach.getExamTypeCode());
                attachedExamTypeListNew.add(examTypeListNewExamTypeToAttach);
            }
            examTypeListNew = attachedExamTypeListNew;
            certificate.setExamTypeList(examTypeListNew);
            certificate = em.merge(certificate);
            if (toolCodeOld != null && !toolCodeOld.equals(toolCodeNew)) {
                toolCodeOld.getCertificateList().remove(certificate);
                toolCodeOld = em.merge(toolCodeOld);
            }
            if (toolCodeNew != null && !toolCodeNew.equals(toolCodeOld)) {
                toolCodeNew.getCertificateList().add(certificate);
                toolCodeNew = em.merge(toolCodeNew);
            }
            if (companyCodeOld != null && !companyCodeOld.equals(companyCodeNew)) {
                companyCodeOld.getCertificateList().remove(certificate);
                companyCodeOld = em.merge(companyCodeOld);
            }
            if (companyCodeNew != null && !companyCodeNew.equals(companyCodeOld)) {
                companyCodeNew.getCertificateList().add(certificate);
                companyCodeNew = em.merge(companyCodeNew);
            }
            for (Course courseListOldCourse : courseListOld) {
                if (!courseListNew.contains(courseListOldCourse)) {
                    courseListOldCourse.getCertificateList().remove(certificate);
                    courseListOldCourse = em.merge(courseListOldCourse);
                }
            }
            for (Course courseListNewCourse : courseListNew) {
                if (!courseListOld.contains(courseListNewCourse)) {
                    courseListNewCourse.getCertificateList().add(certificate);
                    courseListNewCourse = em.merge(courseListNewCourse);
                }
            }
            for (Person personListOldPerson : personListOld) {
                if (!personListNew.contains(personListOldPerson)) {
                    personListOldPerson.getCertificateList().remove(certificate);
                    personListOldPerson = em.merge(personListOldPerson);
                }
            }
            for (Person personListNewPerson : personListNew) {
                if (!personListOld.contains(personListNewPerson)) {
                    personListNewPerson.getCertificateList().add(certificate);
                    personListNewPerson = em.merge(personListNewPerson);
                }
            }
            for (ExamType examTypeListNewExamType : examTypeListNew) {
                if (!examTypeListOld.contains(examTypeListNewExamType)) {
                    Certificate oldCertificateCodeOfExamTypeListNewExamType = examTypeListNewExamType.getCertificateCode();
                    examTypeListNewExamType.setCertificateCode(certificate);
                    examTypeListNewExamType = em.merge(examTypeListNewExamType);
                    if (oldCertificateCodeOfExamTypeListNewExamType != null && !oldCertificateCodeOfExamTypeListNewExamType.equals(certificate)) {
                        oldCertificateCodeOfExamTypeListNewExamType.getExamTypeList().remove(examTypeListNewExamType);
                        oldCertificateCodeOfExamTypeListNewExamType = em.merge(oldCertificateCodeOfExamTypeListNewExamType);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = certificate.getCertificateCode();
                if (findCertificate(id) == null) {
                    throw new NonexistentEntityException("The certificate with id " + id + " no longer exists.");
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
            Certificate certificate;
            try {
                certificate = em.getReference(Certificate.class, id);
                certificate.getCertificateCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The certificate with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ExamType> examTypeListOrphanCheck = certificate.getExamTypeList();
            for (ExamType examTypeListOrphanCheckExamType : examTypeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Certificate (" + certificate + ") cannot be destroyed since the ExamType " + examTypeListOrphanCheckExamType + " in its examTypeList field has a non-nullable certificateCode field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Tool toolCode = certificate.getToolCode();
            if (toolCode != null) {
                toolCode.getCertificateList().remove(certificate);
                toolCode = em.merge(toolCode);
            }
            Company companyCode = certificate.getCompanyCode();
            if (companyCode != null) {
                companyCode.getCertificateList().remove(certificate);
                companyCode = em.merge(companyCode);
            }
            List<Course> courseList = certificate.getCourseList();
            for (Course courseListCourse : courseList) {
                courseListCourse.getCertificateList().remove(certificate);
                courseListCourse = em.merge(courseListCourse);
            }
            List<Person> personList = certificate.getPersonList();
            for (Person personListPerson : personList) {
                personListPerson.getCertificateList().remove(certificate);
                personListPerson = em.merge(personListPerson);
            }
            em.remove(certificate);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Certificate> findCertificateEntities() {
        return findCertificateEntities(true, -1, -1);
    }

    public List<Certificate> findCertificateEntities(int maxResults, int firstResult) {
        return findCertificateEntities(false, maxResults, firstResult);
    }

    private List<Certificate> findCertificateEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Certificate.class));
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

    public Certificate findCertificate(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Certificate.class, id);
        } finally {
            em.close();
        }
    }

    public int getCertificateCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Certificate> rt = cq.from(Certificate.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
