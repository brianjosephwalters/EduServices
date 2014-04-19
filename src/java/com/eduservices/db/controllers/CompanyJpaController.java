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
import com.eduservices.db.entities.PhoneNumber;
import java.util.ArrayList;
import java.util.List;
import com.eduservices.db.entities.Tool;
import com.eduservices.db.entities.Specialty;
import com.eduservices.db.entities.Address;
import com.eduservices.db.entities.Section;
import com.eduservices.db.entities.Project;
import com.eduservices.db.entities.Certificate;
import com.eduservices.db.entities.Company;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class CompanyJpaController implements Serializable {

    public CompanyJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Company company) throws PreexistingEntityException, Exception {
        if (company.getPhoneNumberList() == null) {
            company.setPhoneNumberList(new ArrayList<PhoneNumber>());
        }
        if (company.getToolList() == null) {
            company.setToolList(new ArrayList<Tool>());
        }
        if (company.getSpecialtyList() == null) {
            company.setSpecialtyList(new ArrayList<Specialty>());
        }
        if (company.getAddressList() == null) {
            company.setAddressList(new ArrayList<Address>());
        }
        if (company.getSectionList() == null) {
            company.setSectionList(new ArrayList<Section>());
        }
        if (company.getProjectList() == null) {
            company.setProjectList(new ArrayList<Project>());
        }
        if (company.getCertificateList() == null) {
            company.setCertificateList(new ArrayList<Certificate>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<PhoneNumber> attachedPhoneNumberList = new ArrayList<PhoneNumber>();
            for (PhoneNumber phoneNumberListPhoneNumberToAttach : company.getPhoneNumberList()) {
                phoneNumberListPhoneNumberToAttach = em.getReference(phoneNumberListPhoneNumberToAttach.getClass(), phoneNumberListPhoneNumberToAttach.getPhoneCode());
                attachedPhoneNumberList.add(phoneNumberListPhoneNumberToAttach);
            }
            company.setPhoneNumberList(attachedPhoneNumberList);
            List<Tool> attachedToolList = new ArrayList<Tool>();
            for (Tool toolListToolToAttach : company.getToolList()) {
                toolListToolToAttach = em.getReference(toolListToolToAttach.getClass(), toolListToolToAttach.getToolCode());
                attachedToolList.add(toolListToolToAttach);
            }
            company.setToolList(attachedToolList);
            List<Specialty> attachedSpecialtyList = new ArrayList<Specialty>();
            for (Specialty specialtyListSpecialtyToAttach : company.getSpecialtyList()) {
                specialtyListSpecialtyToAttach = em.getReference(specialtyListSpecialtyToAttach.getClass(), specialtyListSpecialtyToAttach.getSpecialtyCode());
                attachedSpecialtyList.add(specialtyListSpecialtyToAttach);
            }
            company.setSpecialtyList(attachedSpecialtyList);
            List<Address> attachedAddressList = new ArrayList<Address>();
            for (Address addressListAddressToAttach : company.getAddressList()) {
                addressListAddressToAttach = em.getReference(addressListAddressToAttach.getClass(), addressListAddressToAttach.getAddressCode());
                attachedAddressList.add(addressListAddressToAttach);
            }
            company.setAddressList(attachedAddressList);
            List<Section> attachedSectionList = new ArrayList<Section>();
            for (Section sectionListSectionToAttach : company.getSectionList()) {
                sectionListSectionToAttach = em.getReference(sectionListSectionToAttach.getClass(), sectionListSectionToAttach.getSectionPK());
                attachedSectionList.add(sectionListSectionToAttach);
            }
            company.setSectionList(attachedSectionList);
            List<Project> attachedProjectList = new ArrayList<Project>();
            for (Project projectListProjectToAttach : company.getProjectList()) {
                projectListProjectToAttach = em.getReference(projectListProjectToAttach.getClass(), projectListProjectToAttach.getProjectCode());
                attachedProjectList.add(projectListProjectToAttach);
            }
            company.setProjectList(attachedProjectList);
            List<Certificate> attachedCertificateList = new ArrayList<Certificate>();
            for (Certificate certificateListCertificateToAttach : company.getCertificateList()) {
                certificateListCertificateToAttach = em.getReference(certificateListCertificateToAttach.getClass(), certificateListCertificateToAttach.getCertificateCode());
                attachedCertificateList.add(certificateListCertificateToAttach);
            }
            company.setCertificateList(attachedCertificateList);
            em.persist(company);
            for (PhoneNumber phoneNumberListPhoneNumber : company.getPhoneNumberList()) {
                phoneNumberListPhoneNumber.getCompanyList().add(company);
                phoneNumberListPhoneNumber = em.merge(phoneNumberListPhoneNumber);
            }
            for (Tool toolListTool : company.getToolList()) {
                toolListTool.getCompanyList().add(company);
                toolListTool = em.merge(toolListTool);
            }
            for (Specialty specialtyListSpecialty : company.getSpecialtyList()) {
                specialtyListSpecialty.getCompanyList().add(company);
                specialtyListSpecialty = em.merge(specialtyListSpecialty);
            }
            for (Address addressListAddress : company.getAddressList()) {
                addressListAddress.getCompanyList().add(company);
                addressListAddress = em.merge(addressListAddress);
            }
            for (Section sectionListSection : company.getSectionList()) {
                sectionListSection.getCompanyList().add(company);
                sectionListSection = em.merge(sectionListSection);
            }
            for (Project projectListProject : company.getProjectList()) {
                Company oldCompanyCodeOfProjectListProject = projectListProject.getCompanyCode();
                projectListProject.setCompanyCode(company);
                projectListProject = em.merge(projectListProject);
                if (oldCompanyCodeOfProjectListProject != null) {
                    oldCompanyCodeOfProjectListProject.getProjectList().remove(projectListProject);
                    oldCompanyCodeOfProjectListProject = em.merge(oldCompanyCodeOfProjectListProject);
                }
            }
            for (Certificate certificateListCertificate : company.getCertificateList()) {
                Company oldCompanyCodeOfCertificateListCertificate = certificateListCertificate.getCompanyCode();
                certificateListCertificate.setCompanyCode(company);
                certificateListCertificate = em.merge(certificateListCertificate);
                if (oldCompanyCodeOfCertificateListCertificate != null) {
                    oldCompanyCodeOfCertificateListCertificate.getCertificateList().remove(certificateListCertificate);
                    oldCompanyCodeOfCertificateListCertificate = em.merge(oldCompanyCodeOfCertificateListCertificate);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCompany(company.getCompanyCode()) != null) {
                throw new PreexistingEntityException("Company " + company + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Company company) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Company persistentCompany = em.find(Company.class, company.getCompanyCode());
            List<PhoneNumber> phoneNumberListOld = persistentCompany.getPhoneNumberList();
            List<PhoneNumber> phoneNumberListNew = company.getPhoneNumberList();
            List<Tool> toolListOld = persistentCompany.getToolList();
            List<Tool> toolListNew = company.getToolList();
            List<Specialty> specialtyListOld = persistentCompany.getSpecialtyList();
            List<Specialty> specialtyListNew = company.getSpecialtyList();
            List<Address> addressListOld = persistentCompany.getAddressList();
            List<Address> addressListNew = company.getAddressList();
            List<Section> sectionListOld = persistentCompany.getSectionList();
            List<Section> sectionListNew = company.getSectionList();
            List<Project> projectListOld = persistentCompany.getProjectList();
            List<Project> projectListNew = company.getProjectList();
            List<Certificate> certificateListOld = persistentCompany.getCertificateList();
            List<Certificate> certificateListNew = company.getCertificateList();
            List<String> illegalOrphanMessages = null;
            for (Project projectListOldProject : projectListOld) {
                if (!projectListNew.contains(projectListOldProject)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Project " + projectListOldProject + " since its companyCode field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PhoneNumber> attachedPhoneNumberListNew = new ArrayList<PhoneNumber>();
            for (PhoneNumber phoneNumberListNewPhoneNumberToAttach : phoneNumberListNew) {
                phoneNumberListNewPhoneNumberToAttach = em.getReference(phoneNumberListNewPhoneNumberToAttach.getClass(), phoneNumberListNewPhoneNumberToAttach.getPhoneCode());
                attachedPhoneNumberListNew.add(phoneNumberListNewPhoneNumberToAttach);
            }
            phoneNumberListNew = attachedPhoneNumberListNew;
            company.setPhoneNumberList(phoneNumberListNew);
            List<Tool> attachedToolListNew = new ArrayList<Tool>();
            for (Tool toolListNewToolToAttach : toolListNew) {
                toolListNewToolToAttach = em.getReference(toolListNewToolToAttach.getClass(), toolListNewToolToAttach.getToolCode());
                attachedToolListNew.add(toolListNewToolToAttach);
            }
            toolListNew = attachedToolListNew;
            company.setToolList(toolListNew);
            List<Specialty> attachedSpecialtyListNew = new ArrayList<Specialty>();
            for (Specialty specialtyListNewSpecialtyToAttach : specialtyListNew) {
                specialtyListNewSpecialtyToAttach = em.getReference(specialtyListNewSpecialtyToAttach.getClass(), specialtyListNewSpecialtyToAttach.getSpecialtyCode());
                attachedSpecialtyListNew.add(specialtyListNewSpecialtyToAttach);
            }
            specialtyListNew = attachedSpecialtyListNew;
            company.setSpecialtyList(specialtyListNew);
            List<Address> attachedAddressListNew = new ArrayList<Address>();
            for (Address addressListNewAddressToAttach : addressListNew) {
                addressListNewAddressToAttach = em.getReference(addressListNewAddressToAttach.getClass(), addressListNewAddressToAttach.getAddressCode());
                attachedAddressListNew.add(addressListNewAddressToAttach);
            }
            addressListNew = attachedAddressListNew;
            company.setAddressList(addressListNew);
            List<Section> attachedSectionListNew = new ArrayList<Section>();
            for (Section sectionListNewSectionToAttach : sectionListNew) {
                sectionListNewSectionToAttach = em.getReference(sectionListNewSectionToAttach.getClass(), sectionListNewSectionToAttach.getSectionPK());
                attachedSectionListNew.add(sectionListNewSectionToAttach);
            }
            sectionListNew = attachedSectionListNew;
            company.setSectionList(sectionListNew);
            List<Project> attachedProjectListNew = new ArrayList<Project>();
            for (Project projectListNewProjectToAttach : projectListNew) {
                projectListNewProjectToAttach = em.getReference(projectListNewProjectToAttach.getClass(), projectListNewProjectToAttach.getProjectCode());
                attachedProjectListNew.add(projectListNewProjectToAttach);
            }
            projectListNew = attachedProjectListNew;
            company.setProjectList(projectListNew);
            List<Certificate> attachedCertificateListNew = new ArrayList<Certificate>();
            for (Certificate certificateListNewCertificateToAttach : certificateListNew) {
                certificateListNewCertificateToAttach = em.getReference(certificateListNewCertificateToAttach.getClass(), certificateListNewCertificateToAttach.getCertificateCode());
                attachedCertificateListNew.add(certificateListNewCertificateToAttach);
            }
            certificateListNew = attachedCertificateListNew;
            company.setCertificateList(certificateListNew);
            company = em.merge(company);
            for (PhoneNumber phoneNumberListOldPhoneNumber : phoneNumberListOld) {
                if (!phoneNumberListNew.contains(phoneNumberListOldPhoneNumber)) {
                    phoneNumberListOldPhoneNumber.getCompanyList().remove(company);
                    phoneNumberListOldPhoneNumber = em.merge(phoneNumberListOldPhoneNumber);
                }
            }
            for (PhoneNumber phoneNumberListNewPhoneNumber : phoneNumberListNew) {
                if (!phoneNumberListOld.contains(phoneNumberListNewPhoneNumber)) {
                    phoneNumberListNewPhoneNumber.getCompanyList().add(company);
                    phoneNumberListNewPhoneNumber = em.merge(phoneNumberListNewPhoneNumber);
                }
            }
            for (Tool toolListOldTool : toolListOld) {
                if (!toolListNew.contains(toolListOldTool)) {
                    toolListOldTool.getCompanyList().remove(company);
                    toolListOldTool = em.merge(toolListOldTool);
                }
            }
            for (Tool toolListNewTool : toolListNew) {
                if (!toolListOld.contains(toolListNewTool)) {
                    toolListNewTool.getCompanyList().add(company);
                    toolListNewTool = em.merge(toolListNewTool);
                }
            }
            for (Specialty specialtyListOldSpecialty : specialtyListOld) {
                if (!specialtyListNew.contains(specialtyListOldSpecialty)) {
                    specialtyListOldSpecialty.getCompanyList().remove(company);
                    specialtyListOldSpecialty = em.merge(specialtyListOldSpecialty);
                }
            }
            for (Specialty specialtyListNewSpecialty : specialtyListNew) {
                if (!specialtyListOld.contains(specialtyListNewSpecialty)) {
                    specialtyListNewSpecialty.getCompanyList().add(company);
                    specialtyListNewSpecialty = em.merge(specialtyListNewSpecialty);
                }
            }
            for (Address addressListOldAddress : addressListOld) {
                if (!addressListNew.contains(addressListOldAddress)) {
                    addressListOldAddress.getCompanyList().remove(company);
                    addressListOldAddress = em.merge(addressListOldAddress);
                }
            }
            for (Address addressListNewAddress : addressListNew) {
                if (!addressListOld.contains(addressListNewAddress)) {
                    addressListNewAddress.getCompanyList().add(company);
                    addressListNewAddress = em.merge(addressListNewAddress);
                }
            }
            for (Section sectionListOldSection : sectionListOld) {
                if (!sectionListNew.contains(sectionListOldSection)) {
                    sectionListOldSection.getCompanyList().remove(company);
                    sectionListOldSection = em.merge(sectionListOldSection);
                }
            }
            for (Section sectionListNewSection : sectionListNew) {
                if (!sectionListOld.contains(sectionListNewSection)) {
                    sectionListNewSection.getCompanyList().add(company);
                    sectionListNewSection = em.merge(sectionListNewSection);
                }
            }
            for (Project projectListNewProject : projectListNew) {
                if (!projectListOld.contains(projectListNewProject)) {
                    Company oldCompanyCodeOfProjectListNewProject = projectListNewProject.getCompanyCode();
                    projectListNewProject.setCompanyCode(company);
                    projectListNewProject = em.merge(projectListNewProject);
                    if (oldCompanyCodeOfProjectListNewProject != null && !oldCompanyCodeOfProjectListNewProject.equals(company)) {
                        oldCompanyCodeOfProjectListNewProject.getProjectList().remove(projectListNewProject);
                        oldCompanyCodeOfProjectListNewProject = em.merge(oldCompanyCodeOfProjectListNewProject);
                    }
                }
            }
            for (Certificate certificateListOldCertificate : certificateListOld) {
                if (!certificateListNew.contains(certificateListOldCertificate)) {
                    certificateListOldCertificate.setCompanyCode(null);
                    certificateListOldCertificate = em.merge(certificateListOldCertificate);
                }
            }
            for (Certificate certificateListNewCertificate : certificateListNew) {
                if (!certificateListOld.contains(certificateListNewCertificate)) {
                    Company oldCompanyCodeOfCertificateListNewCertificate = certificateListNewCertificate.getCompanyCode();
                    certificateListNewCertificate.setCompanyCode(company);
                    certificateListNewCertificate = em.merge(certificateListNewCertificate);
                    if (oldCompanyCodeOfCertificateListNewCertificate != null && !oldCompanyCodeOfCertificateListNewCertificate.equals(company)) {
                        oldCompanyCodeOfCertificateListNewCertificate.getCertificateList().remove(certificateListNewCertificate);
                        oldCompanyCodeOfCertificateListNewCertificate = em.merge(oldCompanyCodeOfCertificateListNewCertificate);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = company.getCompanyCode();
                if (findCompany(id) == null) {
                    throw new NonexistentEntityException("The company with id " + id + " no longer exists.");
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
            Company company;
            try {
                company = em.getReference(Company.class, id);
                company.getCompanyCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The company with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Project> projectListOrphanCheck = company.getProjectList();
            for (Project projectListOrphanCheckProject : projectListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Company (" + company + ") cannot be destroyed since the Project " + projectListOrphanCheckProject + " in its projectList field has a non-nullable companyCode field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PhoneNumber> phoneNumberList = company.getPhoneNumberList();
            for (PhoneNumber phoneNumberListPhoneNumber : phoneNumberList) {
                phoneNumberListPhoneNumber.getCompanyList().remove(company);
                phoneNumberListPhoneNumber = em.merge(phoneNumberListPhoneNumber);
            }
            List<Tool> toolList = company.getToolList();
            for (Tool toolListTool : toolList) {
                toolListTool.getCompanyList().remove(company);
                toolListTool = em.merge(toolListTool);
            }
            List<Specialty> specialtyList = company.getSpecialtyList();
            for (Specialty specialtyListSpecialty : specialtyList) {
                specialtyListSpecialty.getCompanyList().remove(company);
                specialtyListSpecialty = em.merge(specialtyListSpecialty);
            }
            List<Address> addressList = company.getAddressList();
            for (Address addressListAddress : addressList) {
                addressListAddress.getCompanyList().remove(company);
                addressListAddress = em.merge(addressListAddress);
            }
            List<Section> sectionList = company.getSectionList();
            for (Section sectionListSection : sectionList) {
                sectionListSection.getCompanyList().remove(company);
                sectionListSection = em.merge(sectionListSection);
            }
            List<Certificate> certificateList = company.getCertificateList();
            for (Certificate certificateListCertificate : certificateList) {
                certificateListCertificate.setCompanyCode(null);
                certificateListCertificate = em.merge(certificateListCertificate);
            }
            em.remove(company);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Company> findCompanyEntities() {
        return findCompanyEntities(true, -1, -1);
    }

    public List<Company> findCompanyEntities(int maxResults, int firstResult) {
        return findCompanyEntities(false, maxResults, firstResult);
    }

    private List<Company> findCompanyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Company.class));
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

    public Company findCompany(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Company.class, id);
        } finally {
            em.close();
        }
    }

    public int getCompanyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Company> rt = cq.from(Company.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
