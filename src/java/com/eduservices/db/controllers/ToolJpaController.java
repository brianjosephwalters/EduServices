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
import com.eduservices.db.entities.Company;
import java.util.ArrayList;
import java.util.List;
import com.eduservices.db.entities.Certificate;
import com.eduservices.db.entities.Tool;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class ToolJpaController implements Serializable {

    public ToolJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tool tool) throws PreexistingEntityException, Exception {
        if (tool.getCompanyList() == null) {
            tool.setCompanyList(new ArrayList<Company>());
        }
        if (tool.getCertificateList() == null) {
            tool.setCertificateList(new ArrayList<Certificate>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Company> attachedCompanyList = new ArrayList<Company>();
            for (Company companyListCompanyToAttach : tool.getCompanyList()) {
                companyListCompanyToAttach = em.getReference(companyListCompanyToAttach.getClass(), companyListCompanyToAttach.getCompanyCode());
                attachedCompanyList.add(companyListCompanyToAttach);
            }
            tool.setCompanyList(attachedCompanyList);
            List<Certificate> attachedCertificateList = new ArrayList<Certificate>();
            for (Certificate certificateListCertificateToAttach : tool.getCertificateList()) {
                certificateListCertificateToAttach = em.getReference(certificateListCertificateToAttach.getClass(), certificateListCertificateToAttach.getCertificateCode());
                attachedCertificateList.add(certificateListCertificateToAttach);
            }
            tool.setCertificateList(attachedCertificateList);
            em.persist(tool);
            for (Company companyListCompany : tool.getCompanyList()) {
                companyListCompany.getToolList().add(tool);
                companyListCompany = em.merge(companyListCompany);
            }
            for (Certificate certificateListCertificate : tool.getCertificateList()) {
                Tool oldToolCodeOfCertificateListCertificate = certificateListCertificate.getToolCode();
                certificateListCertificate.setToolCode(tool);
                certificateListCertificate = em.merge(certificateListCertificate);
                if (oldToolCodeOfCertificateListCertificate != null) {
                    oldToolCodeOfCertificateListCertificate.getCertificateList().remove(certificateListCertificate);
                    oldToolCodeOfCertificateListCertificate = em.merge(oldToolCodeOfCertificateListCertificate);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTool(tool.getToolCode()) != null) {
                throw new PreexistingEntityException("Tool " + tool + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tool tool) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tool persistentTool = em.find(Tool.class, tool.getToolCode());
            List<Company> companyListOld = persistentTool.getCompanyList();
            List<Company> companyListNew = tool.getCompanyList();
            List<Certificate> certificateListOld = persistentTool.getCertificateList();
            List<Certificate> certificateListNew = tool.getCertificateList();
            List<Company> attachedCompanyListNew = new ArrayList<Company>();
            for (Company companyListNewCompanyToAttach : companyListNew) {
                companyListNewCompanyToAttach = em.getReference(companyListNewCompanyToAttach.getClass(), companyListNewCompanyToAttach.getCompanyCode());
                attachedCompanyListNew.add(companyListNewCompanyToAttach);
            }
            companyListNew = attachedCompanyListNew;
            tool.setCompanyList(companyListNew);
            List<Certificate> attachedCertificateListNew = new ArrayList<Certificate>();
            for (Certificate certificateListNewCertificateToAttach : certificateListNew) {
                certificateListNewCertificateToAttach = em.getReference(certificateListNewCertificateToAttach.getClass(), certificateListNewCertificateToAttach.getCertificateCode());
                attachedCertificateListNew.add(certificateListNewCertificateToAttach);
            }
            certificateListNew = attachedCertificateListNew;
            tool.setCertificateList(certificateListNew);
            tool = em.merge(tool);
            for (Company companyListOldCompany : companyListOld) {
                if (!companyListNew.contains(companyListOldCompany)) {
                    companyListOldCompany.getToolList().remove(tool);
                    companyListOldCompany = em.merge(companyListOldCompany);
                }
            }
            for (Company companyListNewCompany : companyListNew) {
                if (!companyListOld.contains(companyListNewCompany)) {
                    companyListNewCompany.getToolList().add(tool);
                    companyListNewCompany = em.merge(companyListNewCompany);
                }
            }
            for (Certificate certificateListOldCertificate : certificateListOld) {
                if (!certificateListNew.contains(certificateListOldCertificate)) {
                    certificateListOldCertificate.setToolCode(null);
                    certificateListOldCertificate = em.merge(certificateListOldCertificate);
                }
            }
            for (Certificate certificateListNewCertificate : certificateListNew) {
                if (!certificateListOld.contains(certificateListNewCertificate)) {
                    Tool oldToolCodeOfCertificateListNewCertificate = certificateListNewCertificate.getToolCode();
                    certificateListNewCertificate.setToolCode(tool);
                    certificateListNewCertificate = em.merge(certificateListNewCertificate);
                    if (oldToolCodeOfCertificateListNewCertificate != null && !oldToolCodeOfCertificateListNewCertificate.equals(tool)) {
                        oldToolCodeOfCertificateListNewCertificate.getCertificateList().remove(certificateListNewCertificate);
                        oldToolCodeOfCertificateListNewCertificate = em.merge(oldToolCodeOfCertificateListNewCertificate);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = tool.getToolCode();
                if (findTool(id) == null) {
                    throw new NonexistentEntityException("The tool with id " + id + " no longer exists.");
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
            Tool tool;
            try {
                tool = em.getReference(Tool.class, id);
                tool.getToolCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tool with id " + id + " no longer exists.", enfe);
            }
            List<Company> companyList = tool.getCompanyList();
            for (Company companyListCompany : companyList) {
                companyListCompany.getToolList().remove(tool);
                companyListCompany = em.merge(companyListCompany);
            }
            List<Certificate> certificateList = tool.getCertificateList();
            for (Certificate certificateListCertificate : certificateList) {
                certificateListCertificate.setToolCode(null);
                certificateListCertificate = em.merge(certificateListCertificate);
            }
            em.remove(tool);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tool> findToolEntities() {
        return findToolEntities(true, -1, -1);
    }

    public List<Tool> findToolEntities(int maxResults, int firstResult) {
        return findToolEntities(false, maxResults, firstResult);
    }

    private List<Tool> findToolEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tool.class));
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

    public Tool findTool(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tool.class, id);
        } finally {
            em.close();
        }
    }

    public int getToolCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tool> rt = cq.from(Tool.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
