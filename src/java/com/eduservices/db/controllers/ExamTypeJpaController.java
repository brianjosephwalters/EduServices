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
import com.eduservices.db.entities.Exam;
import com.eduservices.db.entities.ExamType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class ExamTypeJpaController implements Serializable {

    public ExamTypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ExamType examType) throws PreexistingEntityException, Exception {
        if (examType.getExamList() == null) {
            examType.setExamList(new ArrayList<Exam>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Certificate certificateCode = examType.getCertificateCode();
            if (certificateCode != null) {
                certificateCode = em.getReference(certificateCode.getClass(), certificateCode.getCertificateCode());
                examType.setCertificateCode(certificateCode);
            }
            List<Exam> attachedExamList = new ArrayList<Exam>();
            for (Exam examListExamToAttach : examType.getExamList()) {
                examListExamToAttach = em.getReference(examListExamToAttach.getClass(), examListExamToAttach.getExamPK());
                attachedExamList.add(examListExamToAttach);
            }
            examType.setExamList(attachedExamList);
            em.persist(examType);
            if (certificateCode != null) {
                certificateCode.getExamTypeList().add(examType);
                certificateCode = em.merge(certificateCode);
            }
            for (Exam examListExam : examType.getExamList()) {
                ExamType oldExamTypeOfExamListExam = examListExam.getExamType();
                examListExam.setExamType(examType);
                examListExam = em.merge(examListExam);
                if (oldExamTypeOfExamListExam != null) {
                    oldExamTypeOfExamListExam.getExamList().remove(examListExam);
                    oldExamTypeOfExamListExam = em.merge(oldExamTypeOfExamListExam);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findExamType(examType.getExamTypeCode()) != null) {
                throw new PreexistingEntityException("ExamType " + examType + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ExamType examType) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ExamType persistentExamType = em.find(ExamType.class, examType.getExamTypeCode());
            Certificate certificateCodeOld = persistentExamType.getCertificateCode();
            Certificate certificateCodeNew = examType.getCertificateCode();
            List<Exam> examListOld = persistentExamType.getExamList();
            List<Exam> examListNew = examType.getExamList();
            List<String> illegalOrphanMessages = null;
            for (Exam examListOldExam : examListOld) {
                if (!examListNew.contains(examListOldExam)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Exam " + examListOldExam + " since its examType field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (certificateCodeNew != null) {
                certificateCodeNew = em.getReference(certificateCodeNew.getClass(), certificateCodeNew.getCertificateCode());
                examType.setCertificateCode(certificateCodeNew);
            }
            List<Exam> attachedExamListNew = new ArrayList<Exam>();
            for (Exam examListNewExamToAttach : examListNew) {
                examListNewExamToAttach = em.getReference(examListNewExamToAttach.getClass(), examListNewExamToAttach.getExamPK());
                attachedExamListNew.add(examListNewExamToAttach);
            }
            examListNew = attachedExamListNew;
            examType.setExamList(examListNew);
            examType = em.merge(examType);
            if (certificateCodeOld != null && !certificateCodeOld.equals(certificateCodeNew)) {
                certificateCodeOld.getExamTypeList().remove(examType);
                certificateCodeOld = em.merge(certificateCodeOld);
            }
            if (certificateCodeNew != null && !certificateCodeNew.equals(certificateCodeOld)) {
                certificateCodeNew.getExamTypeList().add(examType);
                certificateCodeNew = em.merge(certificateCodeNew);
            }
            for (Exam examListNewExam : examListNew) {
                if (!examListOld.contains(examListNewExam)) {
                    ExamType oldExamTypeOfExamListNewExam = examListNewExam.getExamType();
                    examListNewExam.setExamType(examType);
                    examListNewExam = em.merge(examListNewExam);
                    if (oldExamTypeOfExamListNewExam != null && !oldExamTypeOfExamListNewExam.equals(examType)) {
                        oldExamTypeOfExamListNewExam.getExamList().remove(examListNewExam);
                        oldExamTypeOfExamListNewExam = em.merge(oldExamTypeOfExamListNewExam);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = examType.getExamTypeCode();
                if (findExamType(id) == null) {
                    throw new NonexistentEntityException("The examType with id " + id + " no longer exists.");
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
            ExamType examType;
            try {
                examType = em.getReference(ExamType.class, id);
                examType.getExamTypeCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The examType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Exam> examListOrphanCheck = examType.getExamList();
            for (Exam examListOrphanCheckExam : examListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ExamType (" + examType + ") cannot be destroyed since the Exam " + examListOrphanCheckExam + " in its examList field has a non-nullable examType field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Certificate certificateCode = examType.getCertificateCode();
            if (certificateCode != null) {
                certificateCode.getExamTypeList().remove(examType);
                certificateCode = em.merge(certificateCode);
            }
            em.remove(examType);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ExamType> findExamTypeEntities() {
        return findExamTypeEntities(true, -1, -1);
    }

    public List<ExamType> findExamTypeEntities(int maxResults, int firstResult) {
        return findExamTypeEntities(false, maxResults, firstResult);
    }

    private List<ExamType> findExamTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ExamType.class));
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

    public ExamType findExamType(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ExamType.class, id);
        } finally {
            em.close();
        }
    }

    public int getExamTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ExamType> rt = cq.from(ExamType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
