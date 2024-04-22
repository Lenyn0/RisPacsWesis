package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.settings.domain.DiseaseDictionary;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Patient;
import com.bjpowernode.crm.workbench.domain.Report;
import com.bjpowernode.crm.workbench.domain.StudyInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ReportService {

    boolean updatetechnicianID(@Param("accessionNumber")String accessionNumber,@Param("technicianID")String technicianID);

    Boolean updateStudyInfoByArtificer(StudyInfo st);

    int save(Report r);

    int updateReportStatus(String id, String reportStatus);

    int updateAuditorID(String id, String auditorID);

    Report getById(String id);

    Report getByIdOrStudyID(String id);

    int update(Report r);

    PaginationVO<Report> pageList_Report(Map<String, Object> map);

    String getReportStatusById(String id);

    StudyInfo getStudyInfoByAccessionNumber(String accessionNumber);

    List<DiseaseDictionary> getListBybodyPart(String bodyparts);

    boolean updateStudyInfoStatus(@Param("accessionNumber") String accessionNumber, @Param("status") String status);

    PaginationVO<StudyInfo> pageList_StudyInfo(Map<String, Object> map);

    Patient getPatientByAccessionNumber(String accessionNumber);

    Patient getPatientByID(String id);

    String getDiseaseDescription(String name);
}
