/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.db.controllers;

import com.eduservices.db.controllers.exceptions.NonexistentEntityException;
import com.eduservices.db.controllers.exceptions.PreexistingEntityException;
import com.eduservices.db.entities.Format;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.eduservices.db.entities.Section;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class FormatJpaController implements Serializable {

    public FormatJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Format format) throws PreexistingEntityException, Exception {
        if (format.getSectionList() == null) {
            format.setSectionList(new ArrayList<Section>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Section> attachedSectionList = new ArrayList<Section>();
            for (Section sectionListSectionToAttach : format.getSectionList()) {
                sectionListSectionToAttach = em.getReference(sectionListSectionToAttach.getClass(), sectionListSectionToAttach.getSectionPK());
                attachedSectionList.add(sectionListSectionToAttach);
            }
            format.setSectionList(attachedSectionList);
            em.persist(format);
            for (Section sectionListSection : format.getSectionList()) {
                Format oldFormatCodeOfSectionListSection = sectionListSection.getFormatCode();
                sectionListSection.setFormatCode(format);
                sectionListSection = em.merge(sectionListSection);
                if (oldFormatCodeOfSectionListSection != null) {
                    oldFormatCodeOfSectionListSection.getSectionList().remove(sectionListSection);
                    oldFormatCodeOfSectionListSection = em.merge(oldFormatCodeOfSectionListSection);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFormat(format.getFormatCode()) != null) {
                throw new PreexistingEntityException("Format " + format + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Format format) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Format persistentFormat = em.find(Format.class, format.getFormatCode());
            List<Section> sectionListOld = persistentFormat.getSectionList();
            List<Section> sectionListNew = format.getSectionList();
            List<Section> attachedSectionListNew = new ArrayList<Section>();
            for (Section sectionListNewSectionToAttach : sectionListNew) {
                sectionListNewSectionToAttach = em.getReference(sectionListNewSectionToAttach.getClass(), sectionListNewSectionToAttach.getSectionPK());
                attachedSectionListNew.add(sectionListNewSectionToAttach);
            }
            sectionListNew = attachedSectionListNew;
            format.setSectionList(sectionListNew);
            format = em.merge(format);
            for (Section sectionListOldSection : sectionListOld) {
                if (!sectionListNew.contains(sectionListOldSection)) {
                    sectionListOldSection.setFormatCode(null);
                    sectionListOldSection = em.merge(sectionListOldSection);
                }
            }
            for (Section sectionListNewSection : sectionListNew) {
                if (!sectionListOld.contains(sectionListNewSection)) {
                    Format oldFormatCodeOfSectionListNewSection = sectionListNewSection.getFormatCode();
                    sectionListNewSection.setFormatCode(format);
                    sectionListNewSection = em.merge(sectionListNewSection);
                    if (oldFormatCodeOfSectionListNewSection != null && !oldFormatCodeOfSectionListNewSection.equals(format)) {
                        oldFormatCodeOfSectionListNewSection.getSectionList().remove(sectionListNewSection);
                        oldFormatCodeOfSectionListNewSection = em.merge(oldFormatCodeOfSectionListNewSection);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = format.getFormatCode();
                if (findFormat(id) == null) {
                    throw new NonexistentEntityException("The format with id " + id + " no longer exists.");
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
            Format format;
            try {
                format = em.getReference(Format.class, id);
                format.getFormatCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The format with id " + id + " no longer exists.", enfe);
            }
            List<Section> sectionList = format.getSectionList();
            for (Section sectionListSection : sectionList) {
                sectionListSection.setFormatCode(null);
                sectionListSection = em.merge(sectionListSection);
            }
            em.remove(format);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Format> findFormatEntities() {
        return findFormatEntities(true, -1, -1);
    }

    public List<Format> findFormatEntities(int maxResults, int firstResult) {
        return findFormatEntities(false, maxResults, firstResult);
    }

    private List<Format> findFormatEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Format.class));
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

    public Format findFormat(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Format.class, id);
        } finally {
            em.close();
        }
    }

    public int getFormatCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Format> rt = cq.from(Format.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
