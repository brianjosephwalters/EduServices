/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.db.entities;

import com.eduservices.db.entities.Address;
import com.eduservices.db.entities.Attended;
import com.eduservices.db.entities.Certificate;
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
@Table(name = "person")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p"),
    @NamedQuery(name = "Person.findBySsNum", query = "SELECT p FROM Person p WHERE p.ssNum = :ssNum"),
    @NamedQuery(name = "Person.findByLastName", query = "SELECT p FROM Person p WHERE p.lastName = :lastName"),
    @NamedQuery(name = "Person.findByFirstName", query = "SELECT p FROM Person p WHERE p.firstName = :firstName"),
    @NamedQuery(name = "Person.findByGender", query = "SELECT p FROM Person p WHERE p.gender = :gender"),
    @NamedQuery(name = "Person.findByEmail", query = "SELECT p FROM Person p WHERE p.email = :email")})
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ss_num")
    private String ssNum;
    @Basic(optional = false)
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "gender")
    private String gender;
    @Column(name = "email")
    private String email;
    @ManyToMany(mappedBy = "personList")
    private List<Certificate> certificateList;
    @JoinTable(name = "person_phone", joinColumns = {
        @JoinColumn(name = "person_code", referencedColumnName = "ss_num")}, inverseJoinColumns = {
        @JoinColumn(name = "phone_code", referencedColumnName = "phone_code")})
    @ManyToMany
    private List<PhoneNumber> phoneNumberList;
    @ManyToMany(mappedBy = "personList")
    private List<Address> addressList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
    private List<Attended> attendedList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
    private List<ExamTaken> examTakenList;

    public Person() {
    }

    public Person(String ssNum) {
        this.ssNum = ssNum;
    }

    public Person(String ssNum, String lastName) {
        this.ssNum = ssNum;
        this.lastName = lastName;
    }

    public String getSsNum() {
        return ssNum;
    }

    public void setSsNum(String ssNum) {
        this.ssNum = ssNum;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlTransient
    public List<Certificate> getCertificateList() {
        return certificateList;
    }

    public void setCertificateList(List<Certificate> certificateList) {
        this.certificateList = certificateList;
    }

    @XmlTransient
    public List<PhoneNumber> getPhoneNumberList() {
        return phoneNumberList;
    }

    public void setPhoneNumberList(List<PhoneNumber> phoneNumberList) {
        this.phoneNumberList = phoneNumberList;
    }

    @XmlTransient
    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    @XmlTransient
    public List<Attended> getAttendedList() {
        return attendedList;
    }

    public void setAttendedList(List<Attended> attendedList) {
        this.attendedList = attendedList;
    }

    @XmlTransient
    public List<ExamTaken> getExamTakenList() {
        return examTakenList;
    }

    public void setExamTakenList(List<ExamTaken> examTakenList) {
        this.examTakenList = examTakenList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ssNum != null ? ssNum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if ((this.ssNum == null && other.ssNum != null) || (this.ssNum != null && !this.ssNum.equals(other.ssNum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.eduservices.Person[ ssNum=" + ssNum + " ]";
    }
    
}
