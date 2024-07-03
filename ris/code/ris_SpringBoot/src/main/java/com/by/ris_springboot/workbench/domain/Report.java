package com.by.ris_springboot.workbench.domain;

import lombok.Data;

@Data
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
    private String elapsedTime;
}
