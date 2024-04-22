package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Patient;

import java.util.List;

public interface PatientDao {

    int save(Patient p);

    int getCountByAids(String[] accessionNumbers);

    int deleteByAids(String[] accessionNumbers);

    List<String> getPatientNameList(String name);

    Patient getPatientByName(String name);

    int update(Patient p);

    Patient getPatientByaccessionNumber(String accessionNumber);

    Patient getPatientByAccessionNumber(String accessionNumber);

    Patient getPatientByID(String id);
}
