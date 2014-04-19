/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eduservices.business.controllers;

import com.eduservices.db.entities.Attended;
import com.eduservices.db.entities.ExamTaken;
import com.eduservices.db.entities.Person;
import com.eduservices.business.models.CertificationRecord;
import com.eduservices.business.models.CourseRecord;
import com.eduservices.business.models.Transcript;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author bjw
 */
public class TranscriptController {
    
    private final EntityManagerFactory emf;
    
    public TranscriptController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public Transcript getTranscript(Person person) {
        Calendar date = Calendar.getInstance();
        String dateGenerated = date.get(Calendar.MONTH) + "/" +
                               date.get(Calendar.DAY_OF_MONTH) + "/" +
                               date.get(Calendar.YEAR);
        String studentID = person.getSsNum();
        String studentName = person.getLastName() + ", " + person.getFirstName();
        Transcript transcript = new Transcript(dateGenerated, studentName, studentID);
        transcript.setCourses(getCourseRecords(person));
        transcript.setCertifications(getCertificationRecords(person));
        return transcript;
    }
    
    private List<CourseRecord> getCourseRecords(Person person) {
        List<CourseRecord> records = new ArrayList<CourseRecord>();
        
        AttendedController attendedController = new AttendedController(this.emf);
        List<Attended> attendedList = attendedController.getAttendedByPerson(person);
        
        for (Attended attended : attendedList) {
            String courseName = attended.getSection().getCourse().getCourseTitle();
            String courseCode = attended.getSection().getCourse().getCourseCode();
            String sectionCode = attended.getSection().getSectionPK().getSectionCode();
            String year = attended.getSection().getSectionPK().getYear() + "";
            String grade = attended.getScore() + "";
            CourseRecord record = new CourseRecord(courseName, courseCode,
                                                   sectionCode, year, grade);
            records.add(record);
        }
        return records;
    }
    
    private List<CertificationRecord> getCertificationRecords(Person person) {
        List<CertificationRecord> records = new ArrayList<CertificationRecord>();
        
        ExamController examController = new ExamController(this.emf);
        List<ExamTaken> examList = examController.getExamsForPerson(person);
        
        for (ExamTaken exam : examList) {
            String certificateName = exam.getExam().getExamType()
                                         .getCertificateCode().getCertificateTitle();
            String certificateCode = exam.getExam().getExamType()
                                         .getCertificateCode().getCertificateCode();
            String examCode = exam.getExamTakenPK().getExamCode();
            Date date = exam.getExam().getExamDate();
            String examDate = date.getMonth() + "/" + date.getDay() + "/" + date.getYear();
            String examScore = exam.getScore() + "";
            Integer daysValid = exam.getExam().getExamType().getCertificateCode().getDaysValid();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, daysValid);
            String validUntil = cal.get(Calendar.MONTH) + "/" +
                                cal.get(Calendar.DAY_OF_MONTH) + "/" +
                                cal.get(Calendar.YEAR);
            CertificationRecord record = new CertificationRecord (
                                            certificateName, certificateCode,
                                            examCode, examDate, examScore,
                                            validUntil);
            records.add(record);
        }   
        return records;
    }
    
}
