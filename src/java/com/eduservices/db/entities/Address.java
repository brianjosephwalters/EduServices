/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.db.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
@Table(name = "address")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Address.findAll", query = "SELECT a FROM Address a"),
    @NamedQuery(name = "Address.findByAddressCode", query = "SELECT a FROM Address a WHERE a.addressCode = :addressCode"),
    @NamedQuery(name = "Address.findByAddressType", query = "SELECT a FROM Address a WHERE a.addressType = :addressType"),
    @NamedQuery(name = "Address.findByStreet1", query = "SELECT a FROM Address a WHERE a.street1 = :street1"),
    @NamedQuery(name = "Address.findByStreet2", query = "SELECT a FROM Address a WHERE a.street2 = :street2"),
    @NamedQuery(name = "Address.findByCity", query = "SELECT a FROM Address a WHERE a.city = :city"),
    @NamedQuery(name = "Address.findByZipcode", query = "SELECT a FROM Address a WHERE a.zipcode = :zipcode")})
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "address_code")
    private String addressCode;
    @Column(name = "address_type")
    private String addressType;
    @Basic(optional = false)
    @Column(name = "street_1")
    private String street1;
    @Column(name = "street_2")
    private String street2;
    @Column(name = "city")
    private String city;
    @Basic(optional = false)
    @Column(name = "zipcode")
    private String zipcode;
    @JoinTable(name = "company_address", joinColumns = {
        @JoinColumn(name = "address_code", referencedColumnName = "address_code")}, inverseJoinColumns = {
        @JoinColumn(name = "company_code", referencedColumnName = "company_code")})
    @ManyToMany
    private List<Company> companyList;
    @JoinTable(name = "person_address", joinColumns = {
        @JoinColumn(name = "address_code", referencedColumnName = "address_code")}, inverseJoinColumns = {
        @JoinColumn(name = "person_code", referencedColumnName = "ss_num")})
    @ManyToMany
    private List<Person> personList;

    public Address() {
    }

    public Address(String addressCode) {
        this.addressCode = addressCode;
    }

    public Address(String addressCode, String street1, String zipcode) {
        this.addressCode = addressCode;
        this.street1 = street1;
        this.zipcode = zipcode;
    }

    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
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
        hash += (addressCode != null ? addressCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Address)) {
            return false;
        }
        Address other = (Address) object;
        if ((this.addressCode == null && other.addressCode != null) || (this.addressCode != null && !this.addressCode.equals(other.addressCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.eduservices.Address[ addressCode=" + addressCode + " ]";
    }
    
}
