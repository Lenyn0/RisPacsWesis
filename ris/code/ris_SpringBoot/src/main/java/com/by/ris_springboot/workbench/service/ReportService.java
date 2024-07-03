package com.by.ris_springboot.workbench.service;


import com.by.ris_springboot.settings.domain.DiseaseDictionary;
import com.by.ris_springboot.vo.PaginationVO;
import com.by.ris_springboot.workbench.domain.Patient;
import com.by.ris_springboot.workbench.domain.Report;
import com.by.ris_springboot.workbench.domain.StudyInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ReportService {

    int save(Report r);
    int update(Report r);
    PaginationVO<Report> pageList_Report(Map<String, Object> map);
    int updateAuditorID(String id, String auditorID);

    int updateReportStatus(String id, String reportStatus);

    PaginationVO<StudyInfo> pageList_StudyInfo(Map<String, Object> map);

    Report getByIdOrStudyID(String id);

    List<DiseaseDictionary> getListBybodyPart(String bodyparts);

    Patient getPatientByID(String id);

    StudyInfo getStudyInfoByAccessionNumber(String accessionNumber);

    Boolean updateStudyInfoByArtificer(StudyInfo st);


    boolean updateStudyInfoStatus(@Param("accessionNumber") String accessionNumber, @Param("status") String status);

    boolean updatetechnicianID(@Param("accessionNumber")String accessionNumber,@Param("technicianID")String technicianID);

    String getReportStatusById(String id);

}
