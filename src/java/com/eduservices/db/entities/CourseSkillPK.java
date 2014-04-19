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
public class CourseSkillPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "course_code")
    private String courseCode;
    @Basic(optional = false)
    @Column(name = "skill_code")
    private int skillCode;

    public CourseSkillPK() {
    }

    public CourseSkillPK(String courseCode, int skillCode) {
        this.courseCode = courseCode;
        this.skillCode = skillCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getSkillCode() {
        return skillCode;
    }

    public void setSkillCode(int skillCode) {
        this.skillCode = skillCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (courseCode != null ? courseCode.hashCode() : 0);
        hash += (int) skillCode;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CourseSkillPK)) {
            return false;
        }
        CourseSkillPK other = (CourseSkillPK) object;
        if ((this.courseCode == null && other.courseCode != null) || (this.courseCode != null && !this.courseCode.equals(other.courseCode))) {
            return false;
        }
        if (this.skillCode != other.skillCode) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.eduservices.CourseSkillPK[ courseCode=" + courseCode + ", skillCode=" + skillCode + " ]";
    }
    
}
