/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.db.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author bjw
 */
@Entity
@Table(name = "exam")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Exam.findAll", query = "SELECT e FROM Exam e"),
    @NamedQuery(name = "Exam.findByExamCode", query = "SELECT e FROM Exam e WHERE e.examPK.examCode = :examCode"),
    @NamedQuery(name = "Exam.findByExamTypeCode", query = "SELECT e FROM Exam e WHERE e.examPK.examTypeCode = :examTypeCode"),
    @NamedQuery(name = "Exam.findByExamDate", query = "SELECT e FROM Exam e WHERE e.examDate = :examDate")})
public class Exam implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ExamPK examPK;
    @Column(name = "exam_date")
    @Temporal(TemporalType.DATE)
    private Date examDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "exam")
    private List<ExamTaken> examTakenList;
    @JoinColumn(name = "exam_type_code", referencedColumnName = "exam_type_code", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ExamType examType;

    public Exam() {
    }

    public Exam(ExamPK examPK) {
        this.examPK = examPK;
    }

    public Exam(String examCode, String examTypeCode) {
        this.examPK = new ExamPK(examCode, examTypeCode);
    }

    public ExamPK getExamPK() {
        return examPK;
    }

    public void setExamPK(ExamPK examPK) {
        this.examPK = examPK;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    @XmlTransient
    public List<ExamTaken> getExamTakenList() {
        return examTakenList;
    }

    public void setExamTakenList(List<ExamTaken> examTakenList) {
        this.examTakenList = examTakenList;
    }

    public ExamType getExamType() {
        return examType;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (examPK != null ? examPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Exam)) {
            return false;
        }
        Exam other = (Exam) object;
        if ((this.examPK == null && other.examPK != null) || (this.examPK != null && !this.examPK.equals(other.examPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.eduservices.Exam[ examPK=" + examPK + " ]";
    }
    
}
