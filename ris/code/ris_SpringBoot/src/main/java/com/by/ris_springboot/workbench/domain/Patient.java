package com.by.ris_springboot.workbench.domain;

import lombok.Data;

@Data
public class Patient {
    private String id;
    private String name;
    private String namePinYin;
    private String age;
    private String ageType;
    private String gender;
    private String birthDate;
    private String address;
    private String pregnancy;
    private String inpatientDepartment;
    private String inpatientBedNumber;
    private String inpatientNumber;
    private String phoneNumber;
    private String patientType;
    private String idType;
    private String idNumber;
    private String healthCareType;
}
