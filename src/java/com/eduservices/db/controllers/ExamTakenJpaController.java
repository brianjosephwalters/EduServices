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
import com.eduservices.db.entities.Person;
import com.eduservices.db.entities.Exam;
import com.eduservices.db.entities.ExamTaken;
import com.eduservices.db.entities.ExamTakenPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class ExamTakenJpaController implements Serializable {

    public ExamTakenJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ExamTaken examTaken) throws PreexistingEntityException, Exception {
        if (examTaken.getExamTakenPK() == null) {
            examTaken.setExamTakenPK(new ExamTakenPK());
        }
        examTaken.getExamTakenPK().setPersonCode(examTaken.getPerson().getSsNum());
        examTaken.getExamTakenPK().setExamCode(examTaken.getExam().getExamPK().getExamCode());
        examTaken.getExamTakenPK().setExamTypeCode(examTaken.getExam().getExamPK().getExamTypeCode());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Person person = examTaken.getPerson();
            if (person != null) {
                person = em.getReference(person.getClass(), person.getSsNum());
                examTaken.setPerson(person);
            }
            Exam exam = examTaken.getExam();
            if (exam != null) {
                exam = em.getReference(exam.getClass(), exam.getExamPK());
                examTaken.setExam(exam);
            }
            em.persist(examTaken);
            if (person != null) {
                person.getExamTakenList().add(examTaken);
                person = em.merge(person);
            }
            if (exam != null) {
                exam.getExamTakenList().add(examTaken);
                exam = em.merge(exam);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findExamTaken(examTaken.getExamTakenPK()) != null) {
                throw new PreexistingEntityException("ExamTaken " + examTaken + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ExamTaken examTaken) throws NonexistentEntityException, Exception {
        examTaken.getExamTakenPK().setPersonCode(examTaken.getPerson().getSsNum());
        examTaken.getExamTakenPK().setExamCode(examTaken.getExam().getExamPK().getExamCode());
        examTaken.getExamTakenPK().setExamTypeCode(examTaken.getExam().getExamPK().getExamTypeCode());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ExamTaken persistentExamTaken = em.find(ExamTaken.class, examTaken.getExamTakenPK());
            Person personOld = persistentExamTaken.getPerson();
            Person personNew = examTaken.getPerson();
            Exam examOld = persistentExamTaken.getExam();
            Exam examNew = examTaken.getExam();
            if (personNew != null) {
                personNew = em.getReference(personNew.getClass(), personNew.getSsNum());
                examTaken.setPerson(personNew);
            }
            if (examNew != null) {
                examNew = em.getReference(examNew.getClass(), examNew.getExamPK());
                examTaken.setExam(examNew);
            }
            examTaken = em.merge(examTaken);
            if (personOld != null && !personOld.equals(personNew)) {
                personOld.getExamTakenList().remove(examTaken);
                personOld = em.merge(personOld);
            }
            if (personNew != null && !personNew.equals(personOld)) {
                personNew.getExamTakenList().add(examTaken);
                personNew = em.merge(personNew);
            }
            if (examOld != null && !examOld.equals(examNew)) {
                examOld.getExamTakenList().remove(examTaken);
                examOld = em.merge(examOld);
            }
            if (examNew != null && !examNew.equals(examOld)) {
                examNew.getExamTakenList().add(examTaken);
                examNew = em.merge(examNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ExamTakenPK id = examTaken.getExamTakenPK();
                if (findExamTaken(id) == null) {
                    throw new NonexistentEntityException("The examTaken with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ExamTakenPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ExamTaken examTaken;
            try {
                examTaken = em.getReference(ExamTaken.class, id);
                examTaken.getExamTakenPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The examTaken with id " + id + " no longer exists.", enfe);
            }
            Person person = examTaken.getPerson();
            if (person != null) {
                person.getExamTakenList().remove(examTaken);
                person = em.merge(person);
            }
            Exam exam = examTaken.getExam();
            if (exam != null) {
                exam.getExamTakenList().remove(examTaken);
                exam = em.merge(exam);
            }
            em.remove(examTaken);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ExamTaken> findExamTakenEntities() {
        return findExamTakenEntities(true, -1, -1);
    }

    public List<ExamTaken> findExamTakenEntities(int maxResults, int firstResult) {
        return findExamTakenEntities(false, maxResults, firstResult);
    }

    private List<ExamTaken> findExamTakenEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ExamTaken.class));
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

    public ExamTaken findExamTaken(ExamTakenPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ExamTaken.class, id);
        } finally {
            em.close();
        }
    }

    public int getExamTakenCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ExamTaken> rt = cq.from(ExamTaken.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
