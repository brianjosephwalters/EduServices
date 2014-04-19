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
public class SectionPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "course_code")
    private String courseCode;
    @Basic(optional = false)
    @Column(name = "section_code")
    private String sectionCode;
    @Basic(optional = false)
    @Column(name = "year")
    private short year;

    public SectionPK() {
    }

    public SectionPK(String courseCode, String sectionCode, short year) {
        this.courseCode = courseCode;
        this.sectionCode = sectionCode;
        this.year = year;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (courseCode != null ? courseCode.hashCode() : 0);
        hash += (sectionCode != null ? sectionCode.hashCode() : 0);
        hash += (int) year;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SectionPK)) {
            return false;
        }
        SectionPK other = (SectionPK) object;
        if ((this.courseCode == null && other.courseCode != null) || (this.courseCode != null && !this.courseCode.equals(other.courseCode))) {
            return false;
        }
        if ((this.sectionCode == null && other.sectionCode != null) || (this.sectionCode != null && !this.sectionCode.equals(other.sectionCode))) {
            return false;
        }
        if (this.year != other.year) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.eduservices.SectionPK[ courseCode=" + courseCode + ", sectionCode=" + sectionCode + ", year=" + year + " ]";
    }
    
}
