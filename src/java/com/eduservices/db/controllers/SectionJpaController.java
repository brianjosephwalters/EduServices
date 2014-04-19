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
import com.eduservices.db.entities.Format;
import com.eduservices.db.entities.Course;
import com.eduservices.db.entities.Company;
import java.util.ArrayList;
import java.util.List;
import com.eduservices.db.entities.Attended;
import com.eduservices.db.entities.Section;
import com.eduservices.db.entities.SectionPK;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class SectionJpaController implements Serializable {

    public SectionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Section section) throws PreexistingEntityException, Exception {
        if (section.getSectionPK() == null) {
            section.setSectionPK(new SectionPK());
        }
        if (section.getCompanyList() == null) {
            section.setCompanyList(new ArrayList<Company>());
        }
        if (section.getAttendedList() == null) {
            section.setAttendedList(new ArrayList<Attended>());
        }
        section.getSectionPK().setCourseCode(section.getCourse().getCourseCode());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Format formatCode = section.getFormatCode();
            if (formatCode != null) {
                formatCode = em.getReference(formatCode.getClass(), formatCode.getFormatCode());
                section.setFormatCode(formatCode);
            }
            Course course = section.getCourse();
            if (course != null) {
                course = em.getReference(course.getClass(), course.getCourseCode());
                section.setCourse(course);
            }
            List<Company> attachedCompanyList = new ArrayList<Company>();
            for (Company companyListCompanyToAttach : section.getCompanyList()) {
                companyListCompanyToAttach = em.getReference(companyListCompanyToAttach.getClass(), companyListCompanyToAttach.getCompanyCode());
                attachedCompanyList.add(companyListCompanyToAttach);
            }
            section.setCompanyList(attachedCompanyList);
            List<Attended> attachedAttendedList = new ArrayList<Attended>();
            for (Attended attendedListAttendedToAttach : section.getAttendedList()) {
                attendedListAttendedToAttach = em.getReference(attendedListAttendedToAttach.getClass(), attendedListAttendedToAttach.getAttendedPK());
                attachedAttendedList.add(attendedListAttendedToAttach);
            }
            section.setAttendedList(attachedAttendedList);
            em.persist(section);
            if (formatCode != null) {
                formatCode.getSectionList().add(section);
                formatCode = em.merge(formatCode);
            }
            if (course != null) {
                course.getSectionList().add(section);
                course = em.merge(course);
            }
            for (Company companyListCompany : section.getCompanyList()) {
                companyListCompany.getSectionList().add(section);
                companyListCompany = em.merge(companyListCompany);
            }
            for (Attended attendedListAttended : section.getAttendedList()) {
                Section oldSectionOfAttendedListAttended = attendedListAttended.getSection();
                attendedListAttended.setSection(section);
                attendedListAttended = em.merge(attendedListAttended);
                if (oldSectionOfAttendedListAttended != null) {
                    oldSectionOfAttendedListAttended.getAttendedList().remove(attendedListAttended);
                    oldSectionOfAttendedListAttended = em.merge(oldSectionOfAttendedListAttended);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSection(section.getSectionPK()) != null) {
                throw new PreexistingEntityException("Section " + section + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Section section) throws IllegalOrphanException, NonexistentEntityException, Exception {
        section.getSectionPK().setCourseCode(section.getCourse().getCourseCode());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Section persistentSection = em.find(Section.class, section.getSectionPK());
            Format formatCodeOld = persistentSection.getFormatCode();
            Format formatCodeNew = section.getFormatCode();
            Course courseOld = persistentSection.getCourse();
            Course courseNew = section.getCourse();
            List<Company> companyListOld = persistentSection.getCompanyList();
            List<Company> companyListNew = section.getCompanyList();
            List<Attended> attendedListOld = persistentSection.getAttendedList();
            List<Attended> attendedListNew = section.getAttendedList();
            List<String> illegalOrphanMessages = null;
            for (Attended attendedListOldAttended : attendedListOld) {
                if (!attendedListNew.contains(attendedListOldAttended)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Attended " + attendedListOldAttended + " since its section field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (formatCodeNew != null) {
                formatCodeNew = em.getReference(formatCodeNew.getClass(), formatCodeNew.getFormatCode());
                section.setFormatCode(formatCodeNew);
            }
            if (courseNew != null) {
                courseNew = em.getReference(courseNew.getClass(), courseNew.getCourseCode());
                section.setCourse(courseNew);
            }
            List<Company> attachedCompanyListNew = new ArrayList<Company>();
            for (Company companyListNewCompanyToAttach : companyListNew) {
                companyListNewCompanyToAttach = em.getReference(companyListNewCompanyToAttach.getClass(), companyListNewCompanyToAttach.getCompanyCode());
                attachedCompanyListNew.add(companyListNewCompanyToAttach);
            }
            companyListNew = attachedCompanyListNew;
            section.setCompanyList(companyListNew);
            List<Attended> attachedAttendedListNew = new ArrayList<Attended>();
            for (Attended attendedListNewAttendedToAttach : attendedListNew) {
                attendedListNewAttendedToAttach = em.getReference(attendedListNewAttendedToAttach.getClass(), attendedListNewAttendedToAttach.getAttendedPK());
                attachedAttendedListNew.add(attendedListNewAttendedToAttach);
            }
            attendedListNew = attachedAttendedListNew;
            section.setAttendedList(attendedListNew);
            section = em.merge(section);
            if (formatCodeOld != null && !formatCodeOld.equals(formatCodeNew)) {
                formatCodeOld.getSectionList().remove(section);
                formatCodeOld = em.merge(formatCodeOld);
            }
            if (formatCodeNew != null && !formatCodeNew.equals(formatCodeOld)) {
                formatCodeNew.getSectionList().add(section);
                formatCodeNew = em.merge(formatCodeNew);
            }
            if (courseOld != null && !courseOld.equals(courseNew)) {
                courseOld.getSectionList().remove(section);
                courseOld = em.merge(courseOld);
            }
            if (courseNew != null && !courseNew.equals(courseOld)) {
                courseNew.getSectionList().add(section);
                courseNew = em.merge(courseNew);
            }
            for (Company companyListOldCompany : companyListOld) {
                if (!companyListNew.contains(companyListOldCompany)) {
                    companyListOldCompany.getSectionList().remove(section);
                    companyListOldCompany = em.merge(companyListOldCompany);
                }
            }
            for (Company companyListNewCompany : companyListNew) {
                if (!companyListOld.contains(companyListNewCompany)) {
                    companyListNewCompany.getSectionList().add(section);
                    companyListNewCompany = em.merge(companyListNewCompany);
                }
            }
            for (Attended attendedListNewAttended : attendedListNew) {
                if (!attendedListOld.contains(attendedListNewAttended)) {
                    Section oldSectionOfAttendedListNewAttended = attendedListNewAttended.getSection();
                    attendedListNewAttended.setSection(section);
                    attendedListNewAttended = em.merge(attendedListNewAttended);
                    if (oldSectionOfAttendedListNewAttended != null && !oldSectionOfAttendedListNewAttended.equals(section)) {
                        oldSectionOfAttendedListNewAttended.getAttendedList().remove(attendedListNewAttended);
                        oldSectionOfAttendedListNewAttended = em.merge(oldSectionOfAttendedListNewAttended);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                SectionPK id = section.getSectionPK();
                if (findSection(id) == null) {
                    throw new NonexistentEntityException("The section with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(SectionPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Section section;
            try {
                section = em.getReference(Section.class, id);
                section.getSectionPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The section with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Attended> attendedListOrphanCheck = section.getAttendedList();
            for (Attended attendedListOrphanCheckAttended : attendedListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Section (" + section + ") cannot be destroyed since the Attended " + attendedListOrphanCheckAttended + " in its attendedList field has a non-nullable section field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Format formatCode = section.getFormatCode();
            if (formatCode != null) {
                formatCode.getSectionList().remove(section);
                formatCode = em.merge(formatCode);
            }
            Course course = section.getCourse();
            if (course != null) {
                course.getSectionList().remove(section);
                course = em.merge(course);
            }
            List<Company> companyList = section.getCompanyList();
            for (Company companyListCompany : companyList) {
                companyListCompany.getSectionList().remove(section);
                companyListCompany = em.merge(companyListCompany);
            }
            em.remove(section);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Section> findSectionEntities() {
        return findSectionEntities(true, -1, -1);
    }

    public List<Section> findSectionEntities(int maxResults, int firstResult) {
        return findSectionEntities(false, maxResults, firstResult);
    }

    private List<Section> findSectionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Section.class));
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

    public Section findSection(SectionPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Section.class, id);
        } finally {
            em.close();
        }
    }

    public int getSectionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Section> rt = cq.from(Section.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
