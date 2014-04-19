/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.db.entities;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author bjw
 */
@Entity
@Table(name = "course_skill")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CourseSkill.findAll", query = "SELECT c FROM CourseSkill c"),
    @NamedQuery(name = "CourseSkill.findByCourseCode", query = "SELECT c FROM CourseSkill c WHERE c.courseSkillPK.courseCode = :courseCode"),
    @NamedQuery(name = "CourseSkill.findBySkillCode", query = "SELECT c FROM CourseSkill c WHERE c.courseSkillPK.skillCode = :skillCode")})
public class CourseSkill implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CourseSkillPK courseSkillPK;
    @JoinColumn(name = "course_code", referencedColumnName = "course_code", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Course course;

    public CourseSkill() {
    }

    public CourseSkill(CourseSkillPK courseSkillPK) {
        this.courseSkillPK = courseSkillPK;
    }

    public CourseSkill(String courseCode, int skillCode) {
        this.courseSkillPK = new CourseSkillPK(courseCode, skillCode);
    }

    public CourseSkillPK getCourseSkillPK() {
        return courseSkillPK;
    }

    public void setCourseSkillPK(CourseSkillPK courseSkillPK) {
        this.courseSkillPK = courseSkillPK;
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
        hash += (courseSkillPK != null ? courseSkillPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CourseSkill)) {
            return false;
        }
        CourseSkill other = (CourseSkill) object;
        if ((this.courseSkillPK == null && other.courseSkillPK != null) || (this.courseSkillPK != null && !this.courseSkillPK.equals(other.courseSkillPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.eduservices.CourseSkill[ courseSkillPK=" + courseSkillPK + " ]";
    }
    
}
