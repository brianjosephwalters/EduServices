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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "format")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Format.findAll", query = "SELECT f FROM Format f"),
    @NamedQuery(name = "Format.findByFormatCode", query = "SELECT f FROM Format f WHERE f.formatCode = :formatCode"),
    @NamedQuery(name = "Format.findByFormatName", query = "SELECT f FROM Format f WHERE f.formatName = :formatName"),
    @NamedQuery(name = "Format.findByFormatDescription", query = "SELECT f FROM Format f WHERE f.formatDescription = :formatDescription"),
    @NamedQuery(name = "Format.findByFees", query = "SELECT f FROM Format f WHERE f.fees = :fees")})
public class Format implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "format_code")
    private String formatCode;
    @Basic(optional = false)
    @Column(name = "format_name")
    private String formatName;
    @Column(name = "format_description")
    private String formatDescription;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "fees")
    private BigDecimal fees;
    @OneToMany(mappedBy = "formatCode")
    private List<Section> sectionList;

    public Format() {
    }

    public Format(String formatCode) {
        this.formatCode = formatCode;
    }

    public Format(String formatCode, String formatName) {
        this.formatCode = formatCode;
        this.formatName = formatName;
    }

    public String getFormatCode() {
        return formatCode;
    }

    public void setFormatCode(String formatCode) {
        this.formatCode = formatCode;
    }

    public String getFormatName() {
        return formatName;
    }

    public void setFormatName(String formatName) {
        this.formatName = formatName;
    }

    public String getFormatDescription() {
        return formatDescription;
    }

    public void setFormatDescription(String formatDescription) {
        this.formatDescription = formatDescription;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

    @XmlTransient
    public List<Section> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (formatCode != null ? formatCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Format)) {
            return false;
        }
        Format other = (Format) object;
        if ((this.formatCode == null && other.formatCode != null) || (this.formatCode != null && !this.formatCode.equals(other.formatCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.eduservices.Format[ formatCode=" + formatCode + " ]";
    }
    
}
