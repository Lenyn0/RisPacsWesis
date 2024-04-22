package com.bjpowernode.crm.workbench.domain;

public class StudyInfo {
    private String accessionNumber;
    private String status;
    private String patientID;
    private String department;
    private String emergency;
    private String clinicianID;
    private String registrarID;
    private String techicianID;
    private String bodyParts;
    private String modality;
    private String studyDevice;
    private String studyInstanceUID;
    private String requestedProcedureDescription;
    private String scheduledProcedureStepStartDate;
    private String scheduledProcedureStepStartTime;
    private String scheduledProcedureStepDescription;
    private String scheduledProcedureID;
    private String projection;
    private String cancellationReason;
    private String cancellationTime;



    private String cancellationUser;
    private String useConsumables;


    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmergency() {
        return emergency;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public String getClinicianID() {
        return clinicianID;
    }

    public void setClinicianID(String clinicianID) {
        this.clinicianID = clinicianID;
    }

    public String getRegistrarID() {
        return registrarID;
    }

    public void setRegistrarID(String registrarID) {
        this.registrarID = registrarID;
    }

    public String getTechicianID() {
        return techicianID;
    }

    public void setTechicianID(String techicianID) {
        this.techicianID = techicianID;
    }

    public String getBodyParts() {
        return bodyParts;
    }

    public void setBodyParts(String bodyParts) {
        this.bodyParts = bodyParts;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getStudyDevice() {
        return studyDevice;
    }

    public void setStudyDevice(String studyDevice) {
        this.studyDevice = studyDevice;
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

    public String getScheduledProcedureID() {
        return scheduledProcedureID;
    }

    @Override
    public String toString() {
        return "StudyInfo{" +
                "accessionNumber='" + accessionNumber + '\'' +
                ", status='" + status + '\'' +
                ", patientID='" + patientID + '\'' +
                ", department='" + department + '\'' +
                ", emergency='" + emergency + '\'' +
                ", clinicianID='" + clinicianID + '\'' +
                ", registrarID='" + registrarID + '\'' +
                ", techicianID='" + techicianID + '\'' +
                ", bodyParts='" + bodyParts + '\'' +
                ", modality='" + modality + '\'' +
                ", studyDevice='" + studyDevice + '\'' +
                ", studyInstanceUID='" + studyInstanceUID + '\'' +
                ", requestedProcedureDescription='" + requestedProcedureDescription + '\'' +
                ", scheduledProcedureStepStartDate='" + scheduledProcedureStepStartDate + '\'' +
                ", scheduledProcedureStepStartTime='" + scheduledProcedureStepStartTime + '\'' +
                ", scheduledProcedureStepDescription='" + scheduledProcedureStepDescription + '\'' +
                ", scheduledProcedureID='" + scheduledProcedureID + '\'' +
                ", projection='" + projection + '\'' +
                ", cancellationReason='" + cancellationReason + '\'' +
                ", cancellationTime='" + cancellationTime + '\'' +
                ", cancellationUser='" + cancellationUser + '\'' +
                ", useConsumables='" + useConsumables + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public void setScheduledProcedureID(String scheduledProcedureID) {
        this.scheduledProcedureID = scheduledProcedureID;
    }

    public String getProjection() {
        return projection;
    }

    public void setProjection(String projection) {
        this.projection = projection;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getCancellationTime() {
        return cancellationTime;
    }

    public void setCancellationTime(String cancellationTime) {
        this.cancellationTime = cancellationTime;
    }

    public String getCancellationUser() {
        return cancellationUser;
    }

    public void setCancellationUser(String cancellationUser) {
        this.cancellationUser = cancellationUser;
    }

    public String getUseConsumables() {
        return useConsumables;
    }

    public void setUseConsumables(String useConsumables) {
        this.useConsumables = useConsumables;
    }
}
