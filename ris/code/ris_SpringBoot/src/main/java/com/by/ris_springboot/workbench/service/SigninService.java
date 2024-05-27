package com.by.ris_springboot.workbench.service;



import com.by.ris_springboot.vo.PaginationVO;
import com.by.ris_springboot.workbench.domain.Patient;
import com.by.ris_springboot.workbench.domain.StudyInfo;

import java.util.Map;

public interface SigninService {
    PaginationVO<StudyInfo> pageList(Map<String, Object> map);

    Patient getPatientByaccessionNumber(String accessionNumber);

    StudyInfo getStudyInfoByAccessionNumber(String accessionNumber);

    boolean updatescheduledProcedureStepStart(StudyInfo s);

    boolean cancel(StudyInfo s);

    boolean signin(StudyInfo s);

}
