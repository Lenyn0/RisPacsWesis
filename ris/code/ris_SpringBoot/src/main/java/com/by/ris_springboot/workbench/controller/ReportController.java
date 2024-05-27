package com.by.ris_springboot.workbench.controller;


import com.by.ris_springboot.settings.domain.DiseaseDictionary;
import com.by.ris_springboot.vo.PaginationVO;
import com.by.ris_springboot.vo.Result;
import com.by.ris_springboot.vo.Results;
import com.by.ris_springboot.workbench.domain.Patient;
import com.by.ris_springboot.workbench.domain.Report;
import com.by.ris_springboot.workbench.domain.StudyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.by.ris_springboot.workbench.service.ReportService;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("workbench/Report/")
public class ReportController {
    @Autowired
    ReportService reportService;

    private static final AtomicReference<String> dataHolder = new AtomicReference<>("");

    //填写报告页面获取信息
    @GetMapping("pageList.do")
    private Result reportPageList(HttpServletRequest request){
        String studyID = request.getParameter("studyID");
        String patientID = request.getParameter("patientID");
        String patientName = request.getParameter("patientName");
        String status = request.getParameter("status");

        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("studyID", studyID);
        map.put("patientID", patientID);
        map.put("patientName",patientName);
        //需要将状态6转换成待填写。
        map.put("status",status);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        //vo位于src/main/java/com/bjpowernode/crm/vo/PaginationVO.java
        //里面有详细用法简述
        PaginationVO<StudyInfo> paginationVO = reportService.pageList_StudyInfo(map);
        return Results.newSuccessResult(paginationVO);
    }

    //填写报告-获取一条记录的详细信息
    @GetMapping("get_report0.do")
    private Result getReportByStudyId(String studyID) {
        return Results.newSuccessResult(reportService.getByIdOrStudyID(studyID));
    }

    //填写报告-获取一条记录的详细信息
    @GetMapping("get_bodypart_from_Session.do")
    private Result getBodypartFromSession(String body) {
        List<DiseaseDictionary> listBybodyPart = reportService.getListBybodyPart(body);
        return Results.newSuccessResult(listBybodyPart);
    }

    //填写报告-获取一条记录的详细信息-获得病人的年龄，性别
    @GetMapping("get_age_gender.do")
    private Result getPatientByStudyIDOrPatientID(String patientID) {

        Patient patient = reportService.getPatientByID(patientID);
        System.out.println("patient:"+patient);

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("age", patient.getAge());//病人ID
        map.put("gender", patient.getGender());//病人名称
        return Results.newSuccessResult(map);
    }

    //填写报告-获取一条记录的详细信息-获得投照方式，使用耗材，计划的程序步骤的相关描述
    @GetMapping("get_data_from_studyInfo.do")
    private Result getStudyInfoByAccessionNumber(String studyID) {
        StudyInfo studyInfo = reportService.getStudyInfoByAccessionNumber(studyID);
        return Results.newSuccessResult(studyInfo);
    }

    //获取python发送的数据
    @GetMapping("sent_python_message.do")
    private Result sent_python_message() {
        System.out.println("进入发送");
        dataHolder.set(null);
        String data = dataHolder.get();
//        if (data != null && !data.isEmpty()) {
//            // 如果数据不为空，则返回存储的数据
////            response.setContentType("application/json");
////            response.getWriter().write(data);
//            System.out.println(data);
//            return Results.newSuccessResult(data);
//            // 发送完后将内容清空
//            //dataHolder.set(null);
//        } else {
//            // 如果数据为空，则不发送任何内容
//            System.out.println("数据为空，无需发送");
//        }
        return Results.newSuccessResult(data);
    }

}
