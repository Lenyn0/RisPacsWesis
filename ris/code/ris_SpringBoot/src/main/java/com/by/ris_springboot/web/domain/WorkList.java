package com.by.ris_springboot.web.domain;

public class WorkList {
    String accessionNumber;
    String modality;
    String patientName;
    String patientID;
    String patientBirthDate;
    String patientSex;
    String studyInstanceUID;
    String requestedProcedureDescription;
    String scheduledStationAETitle;
    String scheduledProcedureStepStartDate;
    String scheduledProcedureStepStartTime;
    String scheduledProcedureStepDescription;
    String scheduledProcedureStepID;
    String requestedProcedureID;

    @Override
    public String toString() {
        return "WorkList{" +
                "accessionNumber='" + accessionNumber + '\'' +
                ","+"\n"+"modality='" + modality + '\'' +
                ","+"\n"+ "patientName='" + patientName + '\'' +
                ", "+"\n"+"patientID='" + patientID + '\'' +
                ", "+"\n"+"patientBirthDate='" + patientBirthDate + '\'' +
                ", "+"\n"+"patientSex='" + patientSex + '\'' +
                ", "+"\n"+"studyInstanceUID='" + studyInstanceUID + '\'' +
                ", "+"\n"+"requestedProcedureDescription='" + requestedProcedureDescription + '\'' +
                ", "+"\n"+"scheduledStationAETitle='" + scheduledStationAETitle + '\'' +
                ", "+"\n"+"scheduledProcedureStepStartDate='" + scheduledProcedureStepStartDate + '\'' +
                ", "+"\n"+"scheduledProcedureStepStartTime='" + scheduledProcedureStepStartTime + '\'' +
                ", "+"\n"+"scheduledProcedureStepDescription='" + scheduledProcedureStepDescription + '\'' +
                ", "+"\n"+"scheduledProcedureStepID='" + scheduledProcedureStepID + '\'' +
                ", "+"\n"+"requestedProcedureID='" + requestedProcedureID + '\'' +
                '}';
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getPatientBirthDate() {
        return patientBirthDate;
    }

    public void setPatientBirthDate(String patientBirthDate) {
        this.patientBirthDate = patientBirthDate;
    }

    public String getPatientSex() {
        return patientSex;
    }

    public void setPatientSex(String patientSex) {
        this.patientSex = patientSex;
    }

    public String getStudyInstanceUID() {
        return studyInstanceUID;
    }

    public void setStudyInstanceUID(String studyInstanceUID) {
        this.studyInstanceUID = studyInstanceUID;
    }

    public String getRequestedProcedureDescription() {
        return requestedProcedureDescription;
    }

    public void setRequestedProcedureDescription(String requestedProcedureDescription) {
        this.requestedProcedureDescription = requestedProcedureDescription;
    }

    public String getScheduledStationAETitle() {
        return scheduledStationAETitle;
    }

    public void setScheduledStationAETitle(String scheduledStationAETitle) {
        this.scheduledStationAETitle = scheduledStationAETitle;
    }

    public String getScheduledProcedureStepStartDate() {
        return scheduledProcedureStepStartDate;
    }

    public void setScheduledProcedureStepStartDate(String scheduledProcedureStepStartDate) {
        this.scheduledProcedureStepStartDate = scheduledProcedureStepStartDate;
    }

    public String getScheduledProcedureStepStartTime() {
        return scheduledProcedureStepStartTime;
    }

    public void setScheduledProcedureStepStartTime(String scheduledProcedureStepStartTime) {
        this.scheduledProcedureStepStartTime = scheduledProcedureStepStartTime;
    }

    public String getScheduledProcedureStepDescription() {
        return scheduledProcedureStepDescription;
    }

    public void setScheduledProcedureStepDescription(String scheduledProcedureStepDescription) {
        this.scheduledProcedureStepDescription = scheduledProcedureStepDescription;
    }

    public String getScheduledProcedureStepID() {
        return scheduledProcedureStepID;
    }

    public void setScheduledProcedureStepID(String scheduledProcedureStepID) {
        this.scheduledProcedureStepID = scheduledProcedureStepID;
    }

    public String getRequestedProcedureID() {
        return requestedProcedureID;
    }

    public void setRequestedProcedureID(String requestedProcedureID) {
        this.requestedProcedureID = requestedProcedureID;
    }
}
