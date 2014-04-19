/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.db.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author bjw
 */
@Entity
@Table(name = "attended")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Attended.findAll", query = "SELECT a FROM Attended a"),
    @NamedQuery(name = "Attended.findByCourseCode", query = "SELECT a FROM Attended a WHERE a.attendedPK.courseCode = :courseCode"),
    @NamedQuery(name = "Attended.findBySectionCode", query = "SELECT a FROM Attended a WHERE a.attendedPK.sectionCode = :sectionCode"),
    @NamedQuery(name = "Attended.findByYear", query = "SELECT a FROM Attended a WHERE a.attendedPK.year = :year"),
    @NamedQuery(name = "Attended.findByPersonCode", query = "SELECT a FROM Attended a WHERE a.attendedPK.personCode = :personCode"),
    @NamedQuery(name = "Attended.findByCompletedDate", query = "SELECT a FROM Attended a WHERE a.completedDate = :completedDate"),
    @NamedQuery(name = "Attended.findByScore", query = "SELECT a FROM Attended a WHERE a.score = :score")})
public class Attended implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AttendedPK attendedPK;
    @Column(name = "completed_date")
    @Temporal(TemporalType.DATE)
    private Date completedDate;
    @Column(name = "score")
    private Short score;
    @JoinColumns({
        @JoinColumn(name = "course_code", referencedColumnName = "course_code", insertable = false, updatable = false),
        @JoinColumn(name = "section_code", referencedColumnName = "section_code", insertable = false, updatable = false),
        @JoinColumn(name = "year", referencedColumnName = "year", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Section section;
    @JoinColumn(name = "person_code", referencedColumnName = "ss_num", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Person person;

    public Attended() {
    }

    public Attended(AttendedPK attendedPK) {
        this.attendedPK = attendedPK;
    }

    public Attended(String courseCode, String sectionCode, short year, String personCode) {
        this.attendedPK = new AttendedPK(courseCode, sectionCode, year, personCode);
    }

    public AttendedPK getAttendedPK() {
        return attendedPK;
    }

    public void setAttendedPK(AttendedPK attendedPK) {
        this.attendedPK = attendedPK;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public Short getScore() {
        return score;
    }

    public void setScore(Short score) {
        this.score = score;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (attendedPK != null ? attendedPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Attended)) {
            return false;
        }
        Attended other = (Attended) object;
        if ((this.attendedPK == null && other.attendedPK != null) || (this.attendedPK != null && !this.attendedPK.equals(other.attendedPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.eduservices.Attended[ attendedPK=" + attendedPK + " ]";
    }
    
}
