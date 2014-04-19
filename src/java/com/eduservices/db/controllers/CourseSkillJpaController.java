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
import com.eduservices.db.entities.Course;
import com.eduservices.db.entities.CourseSkill;
import com.eduservices.db.entities.CourseSkillPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class CourseSkillJpaController implements Serializable {

    public CourseSkillJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CourseSkill courseSkill) throws PreexistingEntityException, Exception {
        if (courseSkill.getCourseSkillPK() == null) {
            courseSkill.setCourseSkillPK(new CourseSkillPK());
        }
        courseSkill.getCourseSkillPK().setCourseCode(courseSkill.getCourse().getCourseCode());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Course course = courseSkill.getCourse();
            if (course != null) {
                course = em.getReference(course.getClass(), course.getCourseCode());
                courseSkill.setCourse(course);
            }
            em.persist(courseSkill);
            if (course != null) {
                course.getCourseSkillList().add(courseSkill);
                course = em.merge(course);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCourseSkill(courseSkill.getCourseSkillPK()) != null) {
                throw new PreexistingEntityException("CourseSkill " + courseSkill + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CourseSkill courseSkill) throws NonexistentEntityException, Exception {
        courseSkill.getCourseSkillPK().setCourseCode(courseSkill.getCourse().getCourseCode());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CourseSkill persistentCourseSkill = em.find(CourseSkill.class, courseSkill.getCourseSkillPK());
            Course courseOld = persistentCourseSkill.getCourse();
            Course courseNew = courseSkill.getCourse();
            if (courseNew != null) {
                courseNew = em.getReference(courseNew.getClass(), courseNew.getCourseCode());
                courseSkill.setCourse(courseNew);
            }
            courseSkill = em.merge(courseSkill);
            if (courseOld != null && !courseOld.equals(courseNew)) {
                courseOld.getCourseSkillList().remove(courseSkill);
                courseOld = em.merge(courseOld);
            }
            if (courseNew != null && !courseNew.equals(courseOld)) {
                courseNew.getCourseSkillList().add(courseSkill);
                courseNew = em.merge(courseNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                CourseSkillPK id = courseSkill.getCourseSkillPK();
                if (findCourseSkill(id) == null) {
                    throw new NonexistentEntityException("The courseSkill with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(CourseSkillPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CourseSkill courseSkill;
            try {
                courseSkill = em.getReference(CourseSkill.class, id);
                courseSkill.getCourseSkillPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The courseSkill with id " + id + " no longer exists.", enfe);
            }
            Course course = courseSkill.getCourse();
            if (course != null) {
                course.getCourseSkillList().remove(courseSkill);
                course = em.merge(course);
            }
            em.remove(courseSkill);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CourseSkill> findCourseSkillEntities() {
        return findCourseSkillEntities(true, -1, -1);
    }

    public List<CourseSkill> findCourseSkillEntities(int maxResults, int firstResult) {
        return findCourseSkillEntities(false, maxResults, firstResult);
    }

    private List<CourseSkill> findCourseSkillEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CourseSkill.class));
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

    public CourseSkill findCourseSkill(CourseSkillPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CourseSkill.class, id);
        } finally {
            em.close();
        }
    }

    public int getCourseSkillCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CourseSkill> rt = cq.from(CourseSkill.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
