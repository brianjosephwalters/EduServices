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
@Table(name = "phone_number")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PhoneNumber.findAll", query = "SELECT p FROM PhoneNumber p"),
    @NamedQuery(name = "PhoneNumber.findByPhoneCode", query = "SELECT p FROM PhoneNumber p WHERE p.phoneCode = :phoneCode"),
    @NamedQuery(name = "PhoneNumber.findByPhoneType", query = "SELECT p FROM PhoneNumber p WHERE p.phoneType = :phoneType"),
    @NamedQuery(name = "PhoneNumber.findByPhoneNum", query = "SELECT p FROM PhoneNumber p WHERE p.phoneNum = :phoneNum")})
public class PhoneNumber implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "phone_code")
    private String phoneCode;
    @Column(name = "phone_type")
    private String phoneType;
    @Basic(optional = false)
    @Column(name = "phone_num")
    private String phoneNum;
    @ManyToMany(mappedBy = "phoneNumberList")
    private List<Company> companyList;
    @ManyToMany(mappedBy = "phoneNumberList")
    private List<Person> personList;

    public PhoneNumber() {
    }

    public PhoneNumber(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public PhoneNumber(String phoneCode, String phoneNum) {
        this.phoneCode = phoneCode;
        this.phoneNum = phoneNum;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @XmlTransient
    public List<Company> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<Company> companyList) {
        this.companyList = companyList;
    }

    @XmlTransient
    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (phoneCode != null ? phoneCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PhoneNumber)) {
            return false;
        }
        PhoneNumber other = (PhoneNumber) object;
        if ((this.phoneCode == null && other.phoneCode != null) || (this.phoneCode != null && !this.phoneCode.equals(other.phoneCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.eduservices.PhoneNumber[ phoneCode=" + phoneCode + " ]";
    }
    
}
