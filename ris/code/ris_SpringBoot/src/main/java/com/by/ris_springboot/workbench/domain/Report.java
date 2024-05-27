package com.by.ris_springboot.workbench.domain;

public class Report {
    private String id;
    private String studyID;
    private String patientID;
    private String reportStatus;
    private String createUserID;
    private String auditorID;
    private String imagingFindings;
    private String diagnosticOpinion;
    private String bodyPart;
    private String diseaseName;
    private String diseaseDescription;
    private String createTime;
    private String positive;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudyID() {
        return studyID;
    }

    public void setStudyID(String studyID) {
        this.studyID = studyID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getCreateUserID() {
        return createUserID;
    }

    public void setCreateUserID(String createUserID) {
        this.createUserID = createUserID;
    }

    public String getAuditorID() {
        return auditorID;
    }

    public void setAuditorID(String auditorID) {
        this.auditorID = auditorID;
    }

    public String getImagingFindings() {
        return imagingFindings;
    }

    public void setImagingFindings(String imagingFindings) {
        this.imagingFindings = imagingFindings;
    }

    public String getDiagnosticOpinion() {
        return diagnosticOpinion;
    }

    public void setDiagnosticOpinion(String diagnosticOpinion) {
        this.diagnosticOpinion = diagnosticOpinion;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getDiseaseDescription() {
        return diseaseDescription;
    }

    public void setDiseaseDescription(String diseaseDescription) {
        this.diseaseDescription = diseaseDescription;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPositive() {
        return positive;
    }

    public void setPositive(String positive) {
        this.positive = positive;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id='" + id + '\'' +
                ", studyID='" + studyID + '\'' +
                ", patientID='" + patientID + '\'' +
                ", reportStatus='" + reportStatus + '\'' +
                ", createUserID='" + createUserID + '\'' +
                ", auditorID='" + auditorID + '\'' +
                ", imagingFindings='" + imagingFindings + '\'' +
                ", diagnosticOpinion='" + diagnosticOpinion + '\'' +
                ", bodyPart='" + bodyPart + '\'' +
                ", diseaseName='" + diseaseName + '\'' +
                ", diseaseDescription='" + diseaseDescription + '\'' +
                ", createTime='" + createTime + '\'' +
                ", positive='" + positive + '\'' +
                '}';
    }
}
