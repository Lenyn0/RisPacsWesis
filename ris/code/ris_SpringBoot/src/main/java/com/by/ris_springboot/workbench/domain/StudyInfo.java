package com.by.ris_springboot.workbench.domain;

import lombok.Data;

@Data
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
}
