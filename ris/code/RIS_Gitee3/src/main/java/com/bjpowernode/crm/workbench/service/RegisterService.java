package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Patient;
import com.bjpowernode.crm.workbench.domain.StudyInfo;

import java.util.List;
import java.util.Map;

public interface RegisterService {

    PaginationVO<StudyInfo> pageList(Map<String, Object> map);

    boolean save(Patient p, StudyInfo s);

    boolean delete(String[] accessionNumbers);

    List<String> getPatientNameList(String name);

    Patient getPatientByName(String name);

    Patient getPatientByaccessionNumber(String accessionNumber);

    StudyInfo getStudyInfoByAccessionNumber(String accessionNumber);

    boolean update(Patient p, StudyInfo s);

    boolean appointment(StudyInfo s);
}
