/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.db.controllers;

import com.eduservices.db.controllers.exceptions.IllegalOrphanException;
import com.eduservices.db.controllers.exceptions.NonexistentEntityException;
import com.eduservices.db.controllers.exceptions.PreexistingEntityException;
import com.eduservices.db.entities.Exam;
import com.eduservices.db.entities.ExamPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.eduservices.db.entities.ExamType;
import com.eduservices.db.entities.ExamTaken;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class ExamJpaController implements Serializable {

    public ExamJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Exam exam) throws PreexistingEntityException, Exception {
        if (exam.getExamPK() == null) {
            exam.setExamPK(new ExamPK());
        }
        if (exam.getExamTakenList() == null) {
            exam.setExamTakenList(new ArrayList<ExamTaken>());
        }
        exam.getExamPK().setExamTypeCode(exam.getExamType().getExamTypeCode());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ExamType examType = exam.getExamType();
            if (examType != null) {
                examType = em.getReference(examType.getClass(), examType.getExamTypeCode());
                exam.setExamType(examType);
            }
            List<ExamTaken> attachedExamTakenList = new ArrayList<ExamTaken>();
            for (ExamTaken examTakenListExamTakenToAttach : exam.getExamTakenList()) {
                examTakenListExamTakenToAttach = em.getReference(examTakenListExamTakenToAttach.getClass(), examTakenListExamTakenToAttach.getExamTakenPK());
                attachedExamTakenList.add(examTakenListExamTakenToAttach);
            }
            exam.setExamTakenList(attachedExamTakenList);
            em.persist(exam);
            if (examType != null) {
                examType.getExamList().add(exam);
                examType = em.merge(examType);
            }
            for (ExamTaken examTakenListExamTaken : exam.getExamTakenList()) {
                Exam oldExamOfExamTakenListExamTaken = examTakenListExamTaken.getExam();
                examTakenListExamTaken.setExam(exam);
                examTakenListExamTaken = em.merge(examTakenListExamTaken);
                if (oldExamOfExamTakenListExamTaken != null) {
                    oldExamOfExamTakenListExamTaken.getExamTakenList().remove(examTakenListExamTaken);
                    oldExamOfExamTakenListExamTaken = em.merge(oldExamOfExamTakenListExamTaken);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findExam(exam.getExamPK()) != null) {
                throw new PreexistingEntityException("Exam " + exam + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Exam exam) throws IllegalOrphanException, NonexistentEntityException, Exception {
        exam.getExamPK().setExamTypeCode(exam.getExamType().getExamTypeCode());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Exam persistentExam = em.find(Exam.class, exam.getExamPK());
            ExamType examTypeOld = persistentExam.getExamType();
            ExamType examTypeNew = exam.getExamType();
            List<ExamTaken> examTakenListOld = persistentExam.getExamTakenList();
            List<ExamTaken> examTakenListNew = exam.getExamTakenList();
            List<String> illegalOrphanMessages = null;
            for (ExamTaken examTakenListOldExamTaken : examTakenListOld) {
                if (!examTakenListNew.contains(examTakenListOldExamTaken)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ExamTaken " + examTakenListOldExamTaken + " since its exam field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (examTypeNew != null) {
                examTypeNew = em.getReference(examTypeNew.getClass(), examTypeNew.getExamTypeCode());
                exam.setExamType(examTypeNew);
            }
            List<ExamTaken> attachedExamTakenListNew = new ArrayList<ExamTaken>();
            for (ExamTaken examTakenListNewExamTakenToAttach : examTakenListNew) {
                examTakenListNewExamTakenToAttach = em.getReference(examTakenListNewExamTakenToAttach.getClass(), examTakenListNewExamTakenToAttach.getExamTakenPK());
                attachedExamTakenListNew.add(examTakenListNewExamTakenToAttach);
            }
            examTakenListNew = attachedExamTakenListNew;
            exam.setExamTakenList(examTakenListNew);
            exam = em.merge(exam);
            if (examTypeOld != null && !examTypeOld.equals(examTypeNew)) {
                examTypeOld.getExamList().remove(exam);
                examTypeOld = em.merge(examTypeOld);
            }
            if (examTypeNew != null && !examTypeNew.equals(examTypeOld)) {
                examTypeNew.getExamList().add(exam);
                examTypeNew = em.merge(examTypeNew);
            }
            for (ExamTaken examTakenListNewExamTaken : examTakenListNew) {
                if (!examTakenListOld.contains(examTakenListNewExamTaken)) {
                    Exam oldExamOfExamTakenListNewExamTaken = examTakenListNewExamTaken.getExam();
                    examTakenListNewExamTaken.setExam(exam);
                    examTakenListNewExamTaken = em.merge(examTakenListNewExamTaken);
                    if (oldExamOfExamTakenListNewExamTaken != null && !oldExamOfExamTakenListNewExamTaken.equals(exam)) {
                        oldExamOfExamTakenListNewExamTaken.getExamTakenList().remove(examTakenListNewExamTaken);
                        oldExamOfExamTakenListNewExamTaken = em.merge(oldExamOfExamTakenListNewExamTaken);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ExamPK id = exam.getExamPK();
                if (findExam(id) == null) {
                    throw new NonexistentEntityException("The exam with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ExamPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Exam exam;
            try {
                exam = em.getReference(Exam.class, id);
                exam.getExamPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The exam with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ExamTaken> examTakenListOrphanCheck = exam.getExamTakenList();
            for (ExamTaken examTakenListOrphanCheckExamTaken : examTakenListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Exam (" + exam + ") cannot be destroyed since the ExamTaken " + examTakenListOrphanCheckExamTaken + " in its examTakenList field has a non-nullable exam field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            ExamType examType = exam.getExamType();
            if (examType != null) {
                examType.getExamList().remove(exam);
                examType = em.merge(examType);
            }
            em.remove(exam);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Exam> findExamEntities() {
        return findExamEntities(true, -1, -1);
    }

    public List<Exam> findExamEntities(int maxResults, int firstResult) {
        return findExamEntities(false, maxResults, firstResult);
    }

    private List<Exam> findExamEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Exam.class));
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

    public Exam findExam(ExamPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Exam.class, id);
        } finally {
            em.close();
        }
    }

    public int getExamCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Exam> rt = cq.from(Exam.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
