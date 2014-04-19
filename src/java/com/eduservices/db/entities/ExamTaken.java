/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.db.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
@Table(name = "exam_taken")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExamTaken.findAll", query = "SELECT e FROM ExamTaken e"),
    @NamedQuery(name = "ExamTaken.findByPersonCode", query = "SELECT e FROM ExamTaken e WHERE e.examTakenPK.personCode = :personCode"),
    @NamedQuery(name = "ExamTaken.findByExamCode", query = "SELECT e FROM ExamTaken e WHERE e.examTakenPK.examCode = :examCode"),
    @NamedQuery(name = "ExamTaken.findByExamTypeCode", query = "SELECT e FROM ExamTaken e WHERE e.examTakenPK.examTypeCode = :examTypeCode"),
    @NamedQuery(name = "ExamTaken.findByScore", query = "SELECT e FROM ExamTaken e WHERE e.score = :score")})
public class ExamTaken implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ExamTakenPK examTakenPK;
    @Column(name = "score")
    private Short score;
    @JoinColumn(name = "person_code", referencedColumnName = "ss_num", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Person person;
    @JoinColumns({
        @JoinColumn(name = "exam_code", referencedColumnName = "exam_code", insertable = false, updatable = false),
        @JoinColumn(name = "exam_type_code", referencedColumnName = "exam_type_code", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Exam exam;

    public ExamTaken() {
    }

    public ExamTaken(ExamTakenPK examTakenPK) {
        this.examTakenPK = examTakenPK;
    }

    public ExamTaken(String personCode, String examCode, String examTypeCode) {
        this.examTakenPK = new ExamTakenPK(personCode, examCode, examTypeCode);
    }

    public ExamTakenPK getExamTakenPK() {
        return examTakenPK;
    }

    public void setExamTakenPK(ExamTakenPK examTakenPK) {
        this.examTakenPK = examTakenPK;
    }

    public Short getScore() {
        return score;
    }

    public void setScore(Short score) {
        this.score = score;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (examTakenPK != null ? examTakenPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExamTaken)) {
            return false;
        }
        ExamTaken other = (ExamTaken) object;
        if ((this.examTakenPK == null && other.examTakenPK != null) || (this.examTakenPK != null && !this.examTakenPK.equals(other.examTakenPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.eduservices.ExamTaken[ examTakenPK=" + examTakenPK + " ]";
    }
    
}
