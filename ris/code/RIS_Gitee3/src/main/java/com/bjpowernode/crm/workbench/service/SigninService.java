package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Patient;
import com.bjpowernode.crm.workbench.domain.StudyInfo;

import java.util.Map;

public interface SigninService {
    PaginationVO<StudyInfo> pageList(Map<String, Object> map);

    Patient getPatientByaccessionNumber(String accessionNumber);

    StudyInfo getStudyInfoByAccessionNumber(String accessionNumber);

    boolean updatescheduledProcedureStepStart(StudyInfo s);

    boolean cancel(StudyInfo s);

    boolean signin(StudyInfo s);

}
