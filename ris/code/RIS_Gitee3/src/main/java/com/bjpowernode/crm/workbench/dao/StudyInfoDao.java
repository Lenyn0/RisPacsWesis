package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.StudyInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StudyInfoDao {

    StudyInfo getStudyInfoByAccessionNumber(String accessionNumber);

    int getTotalByCondition6OR4(Map<String, Object> map);

    List<StudyInfo> getReportListByCondition(Map<String, Object> map);

    boolean updateStudyInfoStatus(@Param("accessionNumber") String accessionNumber, @Param("status") String status);

    int getTotalByCondition3(Map<String, Object> map);

    List<StudyInfo> getCheckListByCondition(Map<String, Object> map);

    int getTotalByCondition(Map<String, Object> map);

    List<StudyInfo> getStudyInfoDaoListByCondition(Map<String, Object> map);

    int save(StudyInfo s);

    int delete(String[] ids);

    StudyInfo getStudyInfoByaccessionNumber(String accessionNumber);

    int update(StudyInfo s);

    int updatescheduledProcedureStepStart(StudyInfo s);

    int cancel(StudyInfo s);

    int signin(StudyInfo s);

    List<StudyInfo> getStudyInfoListByCondition(Map<String, Object> map);

    Boolean updateStudyInfoByArtificer(StudyInfo st);

    int getTotal();

    List<Map<String, Object>> getCharts();

    List<Map<String, Object>> getPositiveCharts();

    List<Map<String, Object>> getTechnicianCharts();

    boolean updatetechnicianID(@Param("accessionNumber")String accessionNumber,@Param("technicianID")String technicianID);

    List<Map<String, Object>> getDiagnosticianChartsByYear();

    List<Map<String, Object>> getWorkload_equipment(String startDate, String endDate);

    //Map<String, Object> getPositiveCharts();
    List<Map<String, Object>> getWorkload_technician(@Param("startDate") String startDate,@Param("endDate") String endDate);

    List<Map<String, Object>> getWorkload_clinicianID(@Param("startDate") String startDate,@Param("endDate") String endDate);

    List<Map<String, Object>> getWorkload_Device(@Param("startDate") String startDate,@Param("endDate") String endDate);

    List<Map<String, Object>> getWorkload_positive(@Param("startDate") String startDate,@Param("endDate") String endDate);
}
