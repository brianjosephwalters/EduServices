/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.db.controllers;

import com.eduservices.db.controllers.exceptions.NonexistentEntityException;
import com.eduservices.db.controllers.exceptions.PreexistingEntityException;
import com.eduservices.db.entities.Attended;
import com.eduservices.db.entities.AttendedPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.eduservices.db.entities.Section;
import com.eduservices.db.entities.Person;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class AttendedJpaController implements Serializable {

    public AttendedJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Attended attended) throws PreexistingEntityException, Exception {
        if (attended.getAttendedPK() == null) {
            attended.setAttendedPK(new AttendedPK());
        }
        attended.getAttendedPK().setSectionCode(attended.getSection().getSectionPK().getSectionCode());
        attended.getAttendedPK().setYear(attended.getSection().getSectionPK().getYear());
        attended.getAttendedPK().setCourseCode(attended.getSection().getSectionPK().getCourseCode());
        attended.getAttendedPK().setPersonCode(attended.getPerson().getSsNum());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Section section = attended.getSection();
            if (section != null) {
                section = em.getReference(section.getClass(), section.getSectionPK());
                attended.setSection(section);
            }
            Person person = attended.getPerson();
            if (person != null) {
                person = em.getReference(person.getClass(), person.getSsNum());
                attended.setPerson(person);
            }
            em.persist(attended);
            if (section != null) {
                section.getAttendedList().add(attended);
                section = em.merge(section);
            }
            if (person != null) {
                person.getAttendedList().add(attended);
                person = em.merge(person);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAttended(attended.getAttendedPK()) != null) {
                throw new PreexistingEntityException("Attended " + attended + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Attended attended) throws NonexistentEntityException, Exception {
        attended.getAttendedPK().setSectionCode(attended.getSection().getSectionPK().getSectionCode());
        attended.getAttendedPK().setYear(attended.getSection().getSectionPK().getYear());
        attended.getAttendedPK().setCourseCode(attended.getSection().getSectionPK().getCourseCode());
        attended.getAttendedPK().setPersonCode(attended.getPerson().getSsNum());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Attended persistentAttended = em.find(Attended.class, attended.getAttendedPK());
            Section sectionOld = persistentAttended.getSection();
            Section sectionNew = attended.getSection();
            Person personOld = persistentAttended.getPerson();
            Person personNew = attended.getPerson();
            if (sectionNew != null) {
                sectionNew = em.getReference(sectionNew.getClass(), sectionNew.getSectionPK());
                attended.setSection(sectionNew);
            }
            if (personNew != null) {
                personNew = em.getReference(personNew.getClass(), personNew.getSsNum());
                attended.setPerson(personNew);
            }
            attended = em.merge(attended);
            if (sectionOld != null && !sectionOld.equals(sectionNew)) {
                sectionOld.getAttendedList().remove(attended);
                sectionOld = em.merge(sectionOld);
            }
            if (sectionNew != null && !sectionNew.equals(sectionOld)) {
                sectionNew.getAttendedList().add(attended);
                sectionNew = em.merge(sectionNew);
            }
            if (personOld != null && !personOld.equals(personNew)) {
                personOld.getAttendedList().remove(attended);
                personOld = em.merge(personOld);
            }
            if (personNew != null && !personNew.equals(personOld)) {
                personNew.getAttendedList().add(attended);
                personNew = em.merge(personNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                AttendedPK id = attended.getAttendedPK();
                if (findAttended(id) == null) {
                    throw new NonexistentEntityException("The attended with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(AttendedPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Attended attended;
            try {
                attended = em.getReference(Attended.class, id);
                attended.getAttendedPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The attended with id " + id + " no longer exists.", enfe);
            }
            Section section = attended.getSection();
            if (section != null) {
                section.getAttendedList().remove(attended);
                section = em.merge(section);
            }
            Person person = attended.getPerson();
            if (person != null) {
                person.getAttendedList().remove(attended);
                person = em.merge(person);
            }
            em.remove(attended);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Attended> findAttendedEntities() {
        return findAttendedEntities(true, -1, -1);
    }

    public List<Attended> findAttendedEntities(int maxResults, int firstResult) {
        return findAttendedEntities(false, maxResults, firstResult);
    }

    private List<Attended> findAttendedEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Attended.class));
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

    public Attended findAttended(AttendedPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Attended.class, id);
        } finally {
            em.close();
        }
    }

    public int getAttendedCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Attended> rt = cq.from(Attended.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
