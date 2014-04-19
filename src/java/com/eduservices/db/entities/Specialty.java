/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.db.entities;

import com.eduservices.db.entities.Company;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author bjw
 */
@Entity
@Table(name = "specialty")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Specialty.findAll", query = "SELECT s FROM Specialty s"),
    @NamedQuery(name = "Specialty.findBySpecialtyCode", query = "SELECT s FROM Specialty s WHERE s.specialtyCode = :specialtyCode"),
    @NamedQuery(name = "Specialty.findBySpecialtyName", query = "SELECT s FROM Specialty s WHERE s.specialtyName = :specialtyName")})
public class Specialty implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "specialty_code")
    private String specialtyCode;
    @Basic(optional = false)
    @Column(name = "specialty_name")
    private String specialtyName;
    @ManyToMany(mappedBy = "specialtyList")
    private List<Company> companyList;

    public Specialty() {
    }

    public Specialty(String specialtyCode) {
        this.specialtyCode = specialtyCode;
    }

    public Specialty(String specialtyCode, String specialtyName) {
        this.specialtyCode = specialtyCode;
        this.specialtyName = specialtyName;
    }

    public String getSpecialtyCode() {
        return specialtyCode;
    }

    public void setSpecialtyCode(String specialtyCode) {
        this.specialtyCode = specialtyCode;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }

    @XmlTransient
    public List<Company> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<Company> companyList) {
        this.companyList = companyList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (specialtyCode != null ? specialtyCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Specialty)) {
            return false;
        }
        Specialty other = (Specialty) object;
        if ((this.specialtyCode == null && other.specialtyCode != null) || (this.specialtyCode != null && !this.specialtyCode.equals(other.specialtyCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.eduservices.Specialty[ specialtyCode=" + specialtyCode + " ]";
    }
    
}
