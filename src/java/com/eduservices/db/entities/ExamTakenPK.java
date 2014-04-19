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
public class ExamTakenPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "person_code")
    private String personCode;
    @Basic(optional = false)
    @Column(name = "exam_code")
    private String examCode;
    @Basic(optional = false)
    @Column(name = "exam_type_code")
    private String examTypeCode;

    public ExamTakenPK() {
    }

    public ExamTakenPK(String personCode, String examCode, String examTypeCode) {
        this.personCode = personCode;
        this.examCode = examCode;
        this.examTypeCode = examTypeCode;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }

    public String getExamTypeCode() {
        return examTypeCode;
    }

    public void setExamTypeCode(String examTypeCode) {
        this.examTypeCode = examTypeCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (personCode != null ? personCode.hashCode() : 0);
        hash += (examCode != null ? examCode.hashCode() : 0);
        hash += (examTypeCode != null ? examTypeCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExamTakenPK)) {
            return false;
        }
        ExamTakenPK other = (ExamTakenPK) object;
        if ((this.personCode == null && other.personCode != null) || (this.personCode != null && !this.personCode.equals(other.personCode))) {
            return false;
        }
        if ((this.examCode == null && other.examCode != null) || (this.examCode != null && !this.examCode.equals(other.examCode))) {
            return false;
        }
        if ((this.examTypeCode == null && other.examTypeCode != null) || (this.examTypeCode != null && !this.examTypeCode.equals(other.examTypeCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.eduservices.ExamTakenPK[ personCode=" + personCode + ", examCode=" + examCode + ", examTypeCode=" + examTypeCode + " ]";
    }
    
}
