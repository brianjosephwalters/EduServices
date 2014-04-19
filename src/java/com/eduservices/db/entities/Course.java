/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.db.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "course")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Course.findAll", query = "SELECT c FROM Course c"),
    @NamedQuery(name = "Course.findByCourseCode", query = "SELECT c FROM Course c WHERE c.courseCode = :courseCode"),
    @NamedQuery(name = "Course.findByCourseTitle", query = "SELECT c FROM Course c WHERE c.courseTitle = :courseTitle"),
    @NamedQuery(name = "Course.findByCourseDescription", query = "SELECT c FROM Course c WHERE c.courseDescription = :courseDescription"),
    @NamedQuery(name = "Course.findByCourseLevel", query = "SELECT c FROM Course c WHERE c.courseLevel = :courseLevel"),
    @NamedQuery(name = "Course.findByStatus", query = "SELECT c FROM Course c WHERE c.status = :status"),
    @NamedQuery(name = "Course.findByRetailPrice", query = "SELECT c FROM Course c WHERE c.retailPrice = :retailPrice")})
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "course_code")
    private String courseCode;
    @Basic(optional = false)
    @Column(name = "course_title")
    private String courseTitle;
    @Column(name = "course_description")
    private String courseDescription;
    @Column(name = "course_level")
    private String courseLevel;
    @Column(name = "status")
    private String status;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "retail_price")
    private BigDecimal retailPrice;
    @ManyToMany(mappedBy = "courseList")
    private List<Certificate> certificateList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private List<Section> sectionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private List<CourseSkill> courseSkillList;

    public Course() {
    }

    public Course(String courseCode) {
        this.courseCode = courseCode;
    }

    public Course(String courseCode, String courseTitle) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(String courseLevel) {
        this.courseLevel = courseLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    @XmlTransient
    public List<Certificate> getCertificateList() {
        return certificateList;
    }

    public void setCertificateList(List<Certificate> certificateList) {
        this.certificateList = certificateList;
    }

    @XmlTransient
    public List<Section> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    @XmlTransient
    public List<CourseSkill> getCourseSkillList() {
        return courseSkillList;
    }

    public void setCourseSkillList(List<CourseSkill> courseSkillList) {
        this.courseSkillList = courseSkillList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (courseCode != null ? courseCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Course)) {
            return false;
        }
        Course other = (Course) object;
        if ((this.courseCode == null && other.courseCode != null) || (this.courseCode != null && !this.courseCode.equals(other.courseCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.eduservices.Course[ courseCode=" + courseCode + " ]";
    }
    
}
