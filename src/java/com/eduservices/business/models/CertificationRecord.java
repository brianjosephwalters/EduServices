/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.business.models;

/**
 *
 * @author bjw
 */
public class CertificationRecord {
    private String certificateName;
    private String certificateCode;
    private String examCode;
    private String examDate;
    private String examScore;
    private String validUntil;

    public CertificationRecord(String certificateName, String certificateCode,
                               String examCode, String examDate, String examScore,
                               String validUntil) {
        this.certificateName = certificateName;
        this.certificateCode = certificateCode;
        this.examCode = examCode;
        this.examDate = examDate;
        this.examScore = examScore;
        this.validUntil = validUntil;
    }
    
    /**
     * @return the certificateName
     */
    public String getCertificateName() {
        return certificateName;
    }

    /**
     * @return the certificateCode
     */
    public String getCertificateCode() {
        return certificateCode;
    }

    /**
     * @return the examCode
     */
    public String getExamCode() {
        return examCode;
    }

    /**
     * @return the examDate
     */
    public String getExamDate() {
        return examDate;
    }

    /**
     * @return the examScore
     */
    public String getExamScore() {
        return examScore;
    }

    /**
     * @return the validUntil
     */
    public String getValidUntil() {
        return validUntil;
    }

    /**
     * @param certificateName the certificateName to set
     */
    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    /**
     * @param certificateCode the certificateCode to set
     */
    public void setCertificateCode(String certificateCode) {
        this.certificateCode = certificateCode;
    }

    /**
     * @param examCode the examCode to set
     */
    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }

    /**
     * @param examDate the examDate to set
     */
    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    /**
     * @param examScore the examScore to set
     */
    public void setExamScore(String examScore) {
        this.examScore = examScore;
    }

    /**
     * @param validUntil the validUntil to set
     */
    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }
}
