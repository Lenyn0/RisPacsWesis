package com.by.ris_springboot.workbench.dao;


import com.by.ris_springboot.workbench.domain.Report;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ReportDao {

    List<String> getReportListByCondition1();

    List<String> getReportListByCondition2();

    int save(Report r);

    int updateReportStatus(@Param("id") String id, @Param("reportStatus") String reportStatus);

    int updateAuditorID(@Param("id") String id, @Param("auditorID") String auditorID);

    Report getById(String id);

    int getTotalByCondition(Map<String, Object> map);

    List<Report> getReportListByCondition(Map<String, Object> map);

    Report getByIdOrStudyID(String id);

    int update(Report r);

    String getReportStatusById(String id);

    List<Map<String,Object>> getWorkload(@Param("startDate") String startDate, @Param("endDate") String endDate);

    List<Map<String,Object>> getWorkload_auditor(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
