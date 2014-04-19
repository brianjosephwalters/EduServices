/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.db.entities;

import com.eduservices.db.entities.Certificate;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "exam_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExamType.findAll", query = "SELECT e FROM ExamType e"),
    @NamedQuery(name = "ExamType.findByExamTypeCode", query = "SELECT e FROM ExamType e WHERE e.examTypeCode = :examTypeCode"),
    @NamedQuery(name = "ExamType.findByExamTitle", query = "SELECT e FROM ExamType e WHERE e.examTitle = :examTitle")})
public class ExamType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "exam_type_code")
    private String examTypeCode;
    @Basic(optional = false)
    @Column(name = "exam_title")
    private String examTitle;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "examType")
    private List<Exam> examList;
    @JoinColumn(name = "certificate_code", referencedColumnName = "certificate_code")
    @ManyToOne(optional = false)
    private Certificate certificateCode;

    public ExamType() {
    }

    public ExamType(String examTypeCode) {
        this.examTypeCode = examTypeCode;
    }

    public ExamType(String examTypeCode, String examTitle) {
        this.examTypeCode = examTypeCode;
        this.examTitle = examTitle;
    }

    public String getExamTypeCode() {
        return examTypeCode;
    }

    public void setExamTypeCode(String examTypeCode) {
        this.examTypeCode = examTypeCode;
    }

    public String getExamTitle() {
        return examTitle;
    }

    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }

    @XmlTransient
    public List<Exam> getExamList() {
        return examList;
    }

    public void setExamList(List<Exam> examList) {
        this.examList = examList;
    }

    public Certificate getCertificateCode() {
        return certificateCode;
    }

    public void setCertificateCode(Certificate certificateCode) {
        this.certificateCode = certificateCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (examTypeCode != null ? examTypeCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExamType)) {
            return false;
        }
        ExamType other = (ExamType) object;
        if ((this.examTypeCode == null && other.examTypeCode != null) || (this.examTypeCode != null && !this.examTypeCode.equals(other.examTypeCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.eduservices.ExamType[ examTypeCode=" + examTypeCode + " ]";
    }
    
}
