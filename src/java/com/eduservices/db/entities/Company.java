/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.db.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author bjw
 */
@Entity
@Table(name = "company")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Company.findAll", query = "SELECT c FROM Company c"),
    @NamedQuery(name = "Company.findByCompanyCode", query = "SELECT c FROM Company c WHERE c.companyCode = :companyCode"),
    @NamedQuery(name = "Company.findByCompanyName", query = "SELECT c FROM Company c WHERE c.companyName = :companyName"),
    @NamedQuery(name = "Company.findByWebsite", query = "SELECT c FROM Company c WHERE c.website = :website")})
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "company_code")
    private String companyCode;
    @Basic(optional = false)
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "website")
    private String website;
    @JoinTable(name = "company_phone", joinColumns = {
        @JoinColumn(name = "company_code", referencedColumnName = "company_code")}, inverseJoinColumns = {
        @JoinColumn(name = "phone_code", referencedColumnName = "phone_code")})
    @ManyToMany
    private List<PhoneNumber> phoneNumberList;
    @JoinTable(name = "tool_used", joinColumns = {
        @JoinColumn(name = "company_code", referencedColumnName = "company_code")}, inverseJoinColumns = {
        @JoinColumn(name = "tool_code", referencedColumnName = "tool_code")})
    @ManyToMany
    private List<Tool> toolList;
    @JoinTable(name = "company_specialty", joinColumns = {
        @JoinColumn(name = "company_code", referencedColumnName = "company_code")}, inverseJoinColumns = {
        @JoinColumn(name = "specialty_code", referencedColumnName = "specialty_code")})
    @ManyToMany
    private List<Specialty> specialtyList;
    @ManyToMany(mappedBy = "companyList")
    private List<Address> addressList;
    @JoinTable(name = "provides", joinColumns = {
        @JoinColumn(name = "company_code", referencedColumnName = "company_code")}, inverseJoinColumns = {
        @JoinColumn(name = "course_code", referencedColumnName = "course_code"),
        @JoinColumn(name = "section_code", referencedColumnName = "section_code"),
        @JoinColumn(name = "year", referencedColumnName = "year")})
    @ManyToMany
    private List<Section> sectionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "companyCode")
    private List<Project> projectList;
    @OneToMany(mappedBy = "companyCode")
    private List<Certificate> certificateList;

    public Company() {
    }

    public Company(String companyCode) {
        this.companyCode = companyCode;
    }

    public Company(String companyCode, String companyName) {
        this.companyCode = companyCode;
        this.companyName = companyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @XmlTransient
    public List<PhoneNumber> getPhoneNumberList() {
        return phoneNumberList;
    }

    public void setPhoneNumberList(List<PhoneNumber> phoneNumberList) {
        this.phoneNumberList = phoneNumberList;
    }

    @XmlTransient
    public List<Tool> getToolList() {
        return toolList;
    }

    public void setToolList(List<Tool> toolList) {
        this.toolList = toolList;
    }

    @XmlTransient
    public List<Specialty> getSpecialtyList() {
        return specialtyList;
    }

    public void setSpecialtyList(List<Specialty> specialtyList) {
        this.specialtyList = specialtyList;
    }

    @XmlTransient
    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    @XmlTransient
    public List<Section> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    @XmlTransient
    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    @XmlTransient
    public List<Certificate> getCertificateList() {
        return certificateList;
    }

    public void setCertificateList(List<Certificate> certificateList) {
        this.certificateList = certificateList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (companyCode != null ? companyCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Company)) {
            return false;
        }
        Company other = (Company) object;
        if ((this.companyCode == null && other.companyCode != null) || (this.companyCode != null && !this.companyCode.equals(other.companyCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.eduservices.Company[ companyCode=" + companyCode + " ]";
    }
    
}
