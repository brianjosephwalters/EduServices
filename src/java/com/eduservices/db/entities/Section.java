/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.db.entities;

import com.eduservices.db.entities.Company;
import com.eduservices.db.entities.Attended;
import com.eduservices.db.entities.Course;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
@Table(name = "section")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Section.findAll", query = "SELECT s FROM Section s"),
    @NamedQuery(name = "Section.findByCourseCode", query = "SELECT s FROM Section s WHERE s.sectionPK.courseCode = :courseCode"),
    @NamedQuery(name = "Section.findBySectionCode", query = "SELECT s FROM Section s WHERE s.sectionPK.sectionCode = :sectionCode"),
    @NamedQuery(name = "Section.findByYear", query = "SELECT s FROM Section s WHERE s.sectionPK.year = :year"),
    @NamedQuery(name = "Section.findByCost", query = "SELECT s FROM Section s WHERE s.cost = :cost")})
public class Section implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SectionPK sectionPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cost")
    private BigDecimal cost;
    @ManyToMany(mappedBy = "sectionList")
    private List<Company> companyList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "section")
    private List<Attended> attendedList;
    @JoinColumn(name = "format_code", referencedColumnName = "format_code")
    @ManyToOne
    private Format formatCode;
    @JoinColumn(name = "course_code", referencedColumnName = "course_code", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Course course;

    public Section() {
    }

    public Section(SectionPK sectionPK) {
        this.sectionPK = sectionPK;
    }

    public Section(String courseCode, String sectionCode, short year) {
        this.sectionPK = new SectionPK(courseCode, sectionCode, year);
    }

    public SectionPK getSectionPK() {
        return sectionPK;
    }

    public void setSectionPK(SectionPK sectionPK) {
        this.sectionPK = sectionPK;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @XmlTransient
    public List<Company> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<Company> companyList) {
        this.companyList = companyList;
    }

    @XmlTransient
    public List<Attended> getAttendedList() {
        return attendedList;
    }

    public void setAttendedList(List<Attended> attendedList) {
        this.attendedList = attendedList;
    }

    public Format getFormatCode() {
        return formatCode;
    }

    public void setFormatCode(Format formatCode) {
        this.formatCode = formatCode;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sectionPK != null ? sectionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Section)) {
            return false;
        }
        Section other = (Section) object;
        if ((this.sectionPK == null && other.sectionPK != null) || (this.sectionPK != null && !this.sectionPK.equals(other.sectionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.eduservices.Section[ sectionPK=" + sectionPK + " ]";
    }
    
}
