package com.by.ris_springboot.workbench.service;


import com.by.ris_springboot.vo.PaginationVO;
import com.by.ris_springboot.workbench.domain.Patient;
import com.by.ris_springboot.workbench.domain.StudyInfo;

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
