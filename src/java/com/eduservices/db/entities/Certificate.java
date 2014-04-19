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
import javax.persistence.ManyToOne;
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
@Table(name = "certificate")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Certificate.findAll", query = "SELECT c FROM Certificate c"),
    @NamedQuery(name = "Certificate.findByCertificateCode", query = "SELECT c FROM Certificate c WHERE c.certificateCode = :certificateCode"),
    @NamedQuery(name = "Certificate.findByCertificateTitle", query = "SELECT c FROM Certificate c WHERE c.certificateTitle = :certificateTitle"),
    @NamedQuery(name = "Certificate.findByCertificateDescription", query = "SELECT c FROM Certificate c WHERE c.certificateDescription = :certificateDescription"),
    @NamedQuery(name = "Certificate.findByDaysValid", query = "SELECT c FROM Certificate c WHERE c.daysValid = :daysValid")})
public class Certificate implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "certificate_code")
    private String certificateCode;
    @Basic(optional = false)
    @Column(name = "certificate_title")
    private String certificateTitle;
    @Column(name = "certificate_description")
    private String certificateDescription;
    @Column(name = "days_valid")
    private Integer daysValid;
    @JoinTable(name = "prepares_for", joinColumns = {
        @JoinColumn(name = "certificate_code", referencedColumnName = "certificate_code")}, inverseJoinColumns = {
        @JoinColumn(name = "course_code", referencedColumnName = "course_code")})
    @ManyToMany
    private List<Course> courseList;
    @JoinTable(name = "earns", joinColumns = {
        @JoinColumn(name = "certificate_code", referencedColumnName = "certificate_code")}, inverseJoinColumns = {
        @JoinColumn(name = "person_code", referencedColumnName = "ss_num")})
    @ManyToMany
    private List<Person> personList;
    @JoinColumn(name = "tool_code", referencedColumnName = "tool_code")
    @ManyToOne
    private Tool toolCode;
    @JoinColumn(name = "company_code", referencedColumnName = "company_code")
    @ManyToOne
    private Company companyCode;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "certificateCode")
    private List<ExamType> examTypeList;

    public Certificate() {
    }

    public Certificate(String certificateCode) {
        this.certificateCode = certificateCode;
    }

    public Certificate(String certificateCode, String certificateTitle) {
        this.certificateCode = certificateCode;
        this.certificateTitle = certificateTitle;
    }

    public String getCertificateCode() {
        return certificateCode;
    }

    public void setCertificateCode(String certificateCode) {
        this.certificateCode = certificateCode;
    }

    public String getCertificateTitle() {
        return certificateTitle;
    }

    public void setCertificateTitle(String certificateTitle) {
        this.certificateTitle = certificateTitle;
    }

    public String getCertificateDescription() {
        return certificateDescription;
    }

    public void setCertificateDescription(String certificateDescription) {
        this.certificateDescription = certificateDescription;
    }

    public Integer getDaysValid() {
        return daysValid;
    }

    public void setDaysValid(Integer daysValid) {
        this.daysValid = daysValid;
    }

    @XmlTransient
    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    @XmlTransient
    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    public Tool getToolCode() {
        return toolCode;
    }

    public void setToolCode(Tool toolCode) {
        this.toolCode = toolCode;
    }

    public Company getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Company companyCode) {
        this.companyCode = companyCode;
    }

    @XmlTransient
    public List<ExamType> getExamTypeList() {
        return examTypeList;
    }

    public void setExamTypeList(List<ExamType> examTypeList) {
        this.examTypeList = examTypeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (certificateCode != null ? certificateCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Certificate)) {
            return false;
        }
        Certificate other = (Certificate) object;
        if ((this.certificateCode == null && other.certificateCode != null) || (this.certificateCode != null && !this.certificateCode.equals(other.certificateCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.eduservices.Certificate[ certificateCode=" + certificateCode + " ]";
    }
    
}
