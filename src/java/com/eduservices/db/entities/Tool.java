/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.db.entities;

import com.eduservices.db.entities.Company;
import com.eduservices.db.entities.Certificate;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "tool")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tool.findAll", query = "SELECT t FROM Tool t"),
    @NamedQuery(name = "Tool.findByToolCode", query = "SELECT t FROM Tool t WHERE t.toolCode = :toolCode"),
    @NamedQuery(name = "Tool.findByToolName", query = "SELECT t FROM Tool t WHERE t.toolName = :toolName"),
    @NamedQuery(name = "Tool.findByToolDescription", query = "SELECT t FROM Tool t WHERE t.toolDescription = :toolDescription")})
public class Tool implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "tool_code")
    private String toolCode;
    @Basic(optional = false)
    @Column(name = "tool_name")
    private String toolName;
    @Column(name = "tool_description")
    private String toolDescription;
    @ManyToMany(mappedBy = "toolList")
    private List<Company> companyList;
    @OneToMany(mappedBy = "toolCode")
    private List<Certificate> certificateList;

    public Tool() {
    }

    public Tool(String toolCode) {
        this.toolCode = toolCode;
    }

    public Tool(String toolCode, String toolName) {
        this.toolCode = toolCode;
        this.toolName = toolName;
    }

    public String getToolCode() {
        return toolCode;
    }

    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getToolDescription() {
        return toolDescription;
    }

    public void setToolDescription(String toolDescription) {
        this.toolDescription = toolDescription;
    }

    @XmlTransient
    public List<Company> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<Company> companyList) {
        this.companyList = companyList;
    }

    @XmlTransient
    public List<Certificate> getCertificateList() {
        return certificateList;
    }

    public void setCertificateList(List<Certificate> certificateList) {
        this.certificateList = certificateList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (toolCode != null ? toolCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tool)) {
            return false;
        }
        Tool other = (Tool) object;
        if ((this.toolCode == null && other.toolCode != null) || (this.toolCode != null && !this.toolCode.equals(other.toolCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.eduservices.Tool[ toolCode=" + toolCode + " ]";
    }
    
}
