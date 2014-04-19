/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.db.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author bjw
 */
@Embeddable
public class AttendedPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "course_code")
    private String courseCode;
    @Basic(optional = false)
    @Column(name = "section_code")
    private String sectionCode;
    @Basic(optional = false)
    @Column(name = "year")
    private short year;
    @Basic(optional = false)
    @Column(name = "person_code")
    private String personCode;

    public AttendedPK() {
    }

    public AttendedPK(String courseCode, String sectionCode, short year, String personCode) {
        this.courseCode = courseCode;
        this.sectionCode = sectionCode;
        this.year = year;
        this.personCode = personCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (courseCode != null ? courseCode.hashCode() : 0);
        hash += (sectionCode != null ? sectionCode.hashCode() : 0);
        hash += (int) year;
        hash += (personCode != null ? personCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AttendedPK)) {
            return false;
        }
        AttendedPK other = (AttendedPK) object;
        if ((this.courseCode == null && other.courseCode != null) || (this.courseCode != null && !this.courseCode.equals(other.courseCode))) {
            return false;
        }
        if ((this.sectionCode == null && other.sectionCode != null) || (this.sectionCode != null && !this.sectionCode.equals(other.sectionCode))) {
            return false;
        }
        if (this.year != other.year) {
            return false;
        }
        if ((this.personCode == null && other.personCode != null) || (this.personCode != null && !this.personCode.equals(other.personCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.eduservices.AttendedPK[ courseCode=" + courseCode + ", sectionCode=" + sectionCode + ", year=" + year + ", personCode=" + personCode + " ]";
    }
    
}
