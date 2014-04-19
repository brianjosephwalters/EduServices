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
import com.eduservices.db.entities.Course;
import java.util.ArrayList;
import java.util.List;
import com.eduservices.db.entities.Section;
import com.eduservices.db.entities.CourseSkill;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class CourseJpaController implements Serializable {

    public CourseJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Course course) throws PreexistingEntityException, Exception {
        if (course.getCertificateList() == null) {
            course.setCertificateList(new ArrayList<Certificate>());
        }
        if (course.getSectionList() == null) {
            course.setSectionList(new ArrayList<Section>());
        }
        if (course.getCourseSkillList() == null) {
            course.setCourseSkillList(new ArrayList<CourseSkill>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Certificate> attachedCertificateList = new ArrayList<Certificate>();
            for (Certificate certificateListCertificateToAttach : course.getCertificateList()) {
                certificateListCertificateToAttach = em.getReference(certificateListCertificateToAttach.getClass(), certificateListCertificateToAttach.getCertificateCode());
                attachedCertificateList.add(certificateListCertificateToAttach);
            }
            course.setCertificateList(attachedCertificateList);
            List<Section> attachedSectionList = new ArrayList<Section>();
            for (Section sectionListSectionToAttach : course.getSectionList()) {
                sectionListSectionToAttach = em.getReference(sectionListSectionToAttach.getClass(), sectionListSectionToAttach.getSectionPK());
                attachedSectionList.add(sectionListSectionToAttach);
            }
            course.setSectionList(attachedSectionList);
            List<CourseSkill> attachedCourseSkillList = new ArrayList<CourseSkill>();
            for (CourseSkill courseSkillListCourseSkillToAttach : course.getCourseSkillList()) {
                courseSkillListCourseSkillToAttach = em.getReference(courseSkillListCourseSkillToAttach.getClass(), courseSkillListCourseSkillToAttach.getCourseSkillPK());
                attachedCourseSkillList.add(courseSkillListCourseSkillToAttach);
            }
            course.setCourseSkillList(attachedCourseSkillList);
            em.persist(course);
            for (Certificate certificateListCertificate : course.getCertificateList()) {
                certificateListCertificate.getCourseList().add(course);
                certificateListCertificate = em.merge(certificateListCertificate);
            }
            for (Section sectionListSection : course.getSectionList()) {
                Course oldCourseOfSectionListSection = sectionListSection.getCourse();
                sectionListSection.setCourse(course);
                sectionListSection = em.merge(sectionListSection);
                if (oldCourseOfSectionListSection != null) {
                    oldCourseOfSectionListSection.getSectionList().remove(sectionListSection);
                    oldCourseOfSectionListSection = em.merge(oldCourseOfSectionListSection);
                }
            }
            for (CourseSkill courseSkillListCourseSkill : course.getCourseSkillList()) {
                Course oldCourseOfCourseSkillListCourseSkill = courseSkillListCourseSkill.getCourse();
                courseSkillListCourseSkill.setCourse(course);
                courseSkillListCourseSkill = em.merge(courseSkillListCourseSkill);
                if (oldCourseOfCourseSkillListCourseSkill != null) {
                    oldCourseOfCourseSkillListCourseSkill.getCourseSkillList().remove(courseSkillListCourseSkill);
                    oldCourseOfCourseSkillListCourseSkill = em.merge(oldCourseOfCourseSkillListCourseSkill);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCourse(course.getCourseCode()) != null) {
                throw new PreexistingEntityException("Course " + course + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Course course) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Course persistentCourse = em.find(Course.class, course.getCourseCode());
            List<Certificate> certificateListOld = persistentCourse.getCertificateList();
            List<Certificate> certificateListNew = course.getCertificateList();
            List<Section> sectionListOld = persistentCourse.getSectionList();
            List<Section> sectionListNew = course.getSectionList();
            List<CourseSkill> courseSkillListOld = persistentCourse.getCourseSkillList();
            List<CourseSkill> courseSkillListNew = course.getCourseSkillList();
            List<String> illegalOrphanMessages = null;
            for (Section sectionListOldSection : sectionListOld) {
                if (!sectionListNew.contains(sectionListOldSection)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Section " + sectionListOldSection + " since its course field is not nullable.");
                }
            }
            for (CourseSkill courseSkillListOldCourseSkill : courseSkillListOld) {
                if (!courseSkillListNew.contains(courseSkillListOldCourseSkill)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CourseSkill " + courseSkillListOldCourseSkill + " since its course field is not nullable.");
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
            course.setCertificateList(certificateListNew);
            List<Section> attachedSectionListNew = new ArrayList<Section>();
            for (Section sectionListNewSectionToAttach : sectionListNew) {
                sectionListNewSectionToAttach = em.getReference(sectionListNewSectionToAttach.getClass(), sectionListNewSectionToAttach.getSectionPK());
                attachedSectionListNew.add(sectionListNewSectionToAttach);
            }
            sectionListNew = attachedSectionListNew;
            course.setSectionList(sectionListNew);
            List<CourseSkill> attachedCourseSkillListNew = new ArrayList<CourseSkill>();
            for (CourseSkill courseSkillListNewCourseSkillToAttach : courseSkillListNew) {
                courseSkillListNewCourseSkillToAttach = em.getReference(courseSkillListNewCourseSkillToAttach.getClass(), courseSkillListNewCourseSkillToAttach.getCourseSkillPK());
                attachedCourseSkillListNew.add(courseSkillListNewCourseSkillToAttach);
            }
            courseSkillListNew = attachedCourseSkillListNew;
            course.setCourseSkillList(courseSkillListNew);
            course = em.merge(course);
            for (Certificate certificateListOldCertificate : certificateListOld) {
                if (!certificateListNew.contains(certificateListOldCertificate)) {
                    certificateListOldCertificate.getCourseList().remove(course);
                    certificateListOldCertificate = em.merge(certificateListOldCertificate);
                }
            }
            for (Certificate certificateListNewCertificate : certificateListNew) {
                if (!certificateListOld.contains(certificateListNewCertificate)) {
                    certificateListNewCertificate.getCourseList().add(course);
                    certificateListNewCertificate = em.merge(certificateListNewCertificate);
                }
            }
            for (Section sectionListNewSection : sectionListNew) {
                if (!sectionListOld.contains(sectionListNewSection)) {
                    Course oldCourseOfSectionListNewSection = sectionListNewSection.getCourse();
                    sectionListNewSection.setCourse(course);
                    sectionListNewSection = em.merge(sectionListNewSection);
                    if (oldCourseOfSectionListNewSection != null && !oldCourseOfSectionListNewSection.equals(course)) {
                        oldCourseOfSectionListNewSection.getSectionList().remove(sectionListNewSection);
                        oldCourseOfSectionListNewSection = em.merge(oldCourseOfSectionListNewSection);
                    }
                }
            }
            for (CourseSkill courseSkillListNewCourseSkill : courseSkillListNew) {
                if (!courseSkillListOld.contains(courseSkillListNewCourseSkill)) {
                    Course oldCourseOfCourseSkillListNewCourseSkill = courseSkillListNewCourseSkill.getCourse();
                    courseSkillListNewCourseSkill.setCourse(course);
                    courseSkillListNewCourseSkill = em.merge(courseSkillListNewCourseSkill);
                    if (oldCourseOfCourseSkillListNewCourseSkill != null && !oldCourseOfCourseSkillListNewCourseSkill.equals(course)) {
                        oldCourseOfCourseSkillListNewCourseSkill.getCourseSkillList().remove(courseSkillListNewCourseSkill);
                        oldCourseOfCourseSkillListNewCourseSkill = em.merge(oldCourseOfCourseSkillListNewCourseSkill);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = course.getCourseCode();
                if (findCourse(id) == null) {
                    throw new NonexistentEntityException("The course with id " + id + " no longer exists.");
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
            Course course;
            try {
                course = em.getReference(Course.class, id);
                course.getCourseCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The course with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Section> sectionListOrphanCheck = course.getSectionList();
            for (Section sectionListOrphanCheckSection : sectionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Course (" + course + ") cannot be destroyed since the Section " + sectionListOrphanCheckSection + " in its sectionList field has a non-nullable course field.");
            }
            List<CourseSkill> courseSkillListOrphanCheck = course.getCourseSkillList();
            for (CourseSkill courseSkillListOrphanCheckCourseSkill : courseSkillListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Course (" + course + ") cannot be destroyed since the CourseSkill " + courseSkillListOrphanCheckCourseSkill + " in its courseSkillList field has a non-nullable course field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Certificate> certificateList = course.getCertificateList();
            for (Certificate certificateListCertificate : certificateList) {
                certificateListCertificate.getCourseList().remove(course);
                certificateListCertificate = em.merge(certificateListCertificate);
            }
            em.remove(course);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Course> findCourseEntities() {
        return findCourseEntities(true, -1, -1);
    }

    public List<Course> findCourseEntities(int maxResults, int firstResult) {
        return findCourseEntities(false, maxResults, firstResult);
    }

    private List<Course> findCourseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Course.class));
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

    public Course findCourse(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Course.class, id);
        } finally {
            em.close();
        }
    }

    public int getCourseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Course> rt = cq.from(Course.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
