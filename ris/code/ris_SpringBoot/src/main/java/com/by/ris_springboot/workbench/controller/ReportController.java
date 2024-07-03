package com.by.ris_springboot.workbench.controller;


import com.by.ris_springboot.settings.domain.DiseaseDictionary;
import com.by.ris_springboot.settings.service.UserService;
import com.by.ris_springboot.utils.DateTimeUtil;
import com.by.ris_springboot.utils.UUIDUtil;
import com.by.ris_springboot.vo.PaginationVO;
import com.by.ris_springboot.vo.Result;
import com.by.ris_springboot.vo.Results;
import com.by.ris_springboot.workbench.domain.Patient;
import com.by.ris_springboot.workbench.domain.Report;
import com.by.ris_springboot.workbench.domain.StudyInfo;
import com.by.ris_springboot.workbench.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.by.ris_springboot.workbench.service.ReportService;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

@RestController
@RequestMapping("workbench/Report/")
public class ReportController {
    @Autowired
    ReportService reportService;
    @Autowired
    UserService userService;
    @Autowired
    RegisterService registerService;

    private static final AtomicReference<String> dataHolder = new AtomicReference<>("");

    //报告审查-待审核报告列表
    @GetMapping("pageList_review.do")
    private Result reviewPageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到审核报告信息列表的操作");

        String id = request.getParameter("id");//报告号
        String studyID = request.getParameter("studyID");//检查号
        String reportStatus = request.getParameter("reportStatus");//状态
        String status = "";
        /*const wait01 = "等待审核";
                        const wait02 = "审核完成";
                        const wait03 = "等待修改";
                        const wait04 = "完成打印";*/
        if(reportStatus.equals("等待审核"))status="1";
        else if(reportStatus.equals("等待修改"))status="2";
        else if(reportStatus.equals("审核完成"))status="3";
        else if(reportStatus.equals("完成打印"))status="4";

        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id", id);
        map.put("studyID",studyID);
        map.put("reportStatus",status);
        //需要将状态6转换成待填写。
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        //ReportService reportService = (ReportService) ServiceFactory.getService(new ReportServiceImpl());

        //vo位于src/main/java/com/bjpowernode/crm/vo/PaginationVO.java
        //里面有详细用法简述
        PaginationVO<Report> paginationVO = reportService.pageList_Report(map);
        System.out.println("输出成功");

        //vo--> {"total":100,"dataList":[{待写报告1},{2},{3}]}
        //PrintJson.printJsonObj(response, paginationVO);
        return Results.newSuccessResult(paginationVO);
    }

    //报告审查-获取一条记录的详细信息
    @GetMapping ("get_report.do")
    private Result getReportByIdOrStudyID(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据id查找这条报告的详细信息");
        String studyID= request.getParameter("studyID"); //studyID=id或studyID
        //ReportService re = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
        Report report = reportService.getByIdOrStudyID(studyID);
        System.out.println(report);

        System.out.println("将病人号转化成为病人名，并获得病人年龄和性别");
        String patientID= request.getParameter("patientID");
        System.out.println("病人号:"+patientID);
        //ReportService rs1 = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
        Patient patient =  reportService.getPatientByID(patientID);
        System.out.println("病人信息："+patient);

        System.out.println("将疾病名称ID转化为名称");
        String DiseaseName = report.getDiseaseName(); //获取疾病名称


        //获得三个参数，投照方式....
        System.out.println("进入查询投照方式，使用耗材，计划的程序步骤的相关描述。");
        System.out.println("studyID:"+studyID);
        //ReportService rs2 = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
        StudyInfo studyInfo = reportService.getStudyInfoByAccessionNumber(studyID);
        System.out.println("studyInfo:"+studyInfo);

        //把上面的数据封装到一个map中
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id", report.getId());//报告号
        System.out.println("id到底是啥："+report.getId());
        map.put("studyID", report.getStudyID());//检查号
        map.put("createUserID",report.getCreateUserID());//报告医生ID
        map.put("patientID", patient.getName());//病人名称
        map.put("age", patient.getAge());//病人年龄
        map.put("gender", patient.getGender());//病人性别
        map.put("bodyPart",report.getBodyPart());//疾病部位
        map.put("diseaseName",DiseaseName);//疾病名称
        map.put("positive",report.getPositive());//是否阳性

        //id="projection"使用耗材useConsumables,scheduledProcedureStepDescription
        map.put("projection",studyInfo.getProjection());//投照方式
        map.put("useConsumables",studyInfo.getUseConsumables());//使用耗材
        map.put("scheduledProcedureStepDescription",studyInfo.getScheduledProcedureStepDescription());//计划程序的相关步骤

        //diseaseDescription,imagingFindings,diagnosticOpinion疾病描述，影像学所见，诊断意见
        map.put("diseaseDescription",report.getDiseaseDescription());//
        map.put("imagingFindings",report.getImagingFindings());//
        map.put("diagnosticOpinion",report.getDiagnosticOpinion());//

        map.put("elapsedTime",report.getElapsedTime());//

        //PrintJson.printJsonObj(response,map);
        return Results.newSuccessResult(map);
    }

    @PostMapping("reject.do")
    private Result updateReportStatusReject(HttpServletRequest request, HttpServletResponse response) {

        String reportStatus="2";
        String status="6";

        //修改report表中的状态 需要从前端传修改后的状态
        //ReportService re = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
        String id= request.getParameter("id");
        //String reportStatus = request.getParameter("reportStatus");
        System.out.println("id为："+id+" 修改后的reportStatus为："+reportStatus);
        int flag1 = reportService.updateReportStatus(id,reportStatus);
        System.out.println("flag1:"+flag1);

        if(status.equals("7"))
        //修改study_info表中的状态 需要从前端传修改后的状态 错，不能改studyInfo
        {
            //ReportService rs1 = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
            String studyID=request.getParameter("studyID");
            //String status = request.getParameter("status");
            System.out.println("studyID为："+studyID+" 修改后的status为："+status);
            boolean flag2 = reportService.updateStudyInfoStatus(studyID,status);
            System.out.println("flag2:"+flag2);
        }


        //获取审核医生id
        //UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        //String auditorName= request.getParameter("auditorName"); //从前端获取审核医生姓名
        String auditorName= request.getParameter("auditorID"); //从前端获取审核医生姓名
        System.out.println("审核医生姓名: "+auditorName);
        String auditorID = userService.getIdByName(auditorName);

        //修改tbl_report表中的审核医生ID
        //ReportService re2 = (ReportService)ServiceFactory.getService(new ReportServiceImpl());
        int flag3 = reportService.updateAuditorID(id,auditorID);
        System.out.println("flag3: "+flag3);

        boolean flag = false;
        if(flag1==1 && flag3 == 1) flag = true;
        //PrintJson.printJsonFlag(response,flag);
        return Results.newSuccessResult(null);
    }

    @PostMapping("pass.do")
    private Result updateReportStatusPass(HttpServletRequest request, HttpServletResponse response) {

        String reportStatus="3";
        String status="7";

        //修改report表中的状态 需要从前端传修改后的状态
        //ReportService re = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
        String id= request.getParameter("id");
        //String reportStatus = request.getParameter("reportStatus");
        System.out.println("id为："+id+" 修改后的reportStatus为："+reportStatus);
        int flag1 = reportService.updateReportStatus(id,reportStatus);
        System.out.println("flag1:"+flag1);

        if(status.equals("7"))
        //修改study_info表中的状态 需要从前端传修改后的状态 错，不能改studyInfo
        {
            //ReportService rs1 = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
            String studyID=request.getParameter("studyID");
            //String status = request.getParameter("status");
            System.out.println("studyID为："+studyID+" 修改后的status为："+status);
            boolean flag2 = reportService.updateStudyInfoStatus(studyID,status);
            System.out.println("flag2:"+flag2);
        }


        //获取审核医生id
        //UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        //String auditorName= request.getParameter("auditorName"); //从前端获取审核医生姓名
        String auditorName= request.getParameter("auditorID"); //从前端获取审核医生姓名
        System.out.println("审核医生姓名: "+auditorName);
        String auditorID = userService.getIdByName(auditorName);

        //修改tbl_report表中的审核医生ID
        //ReportService re2 = (ReportService)ServiceFactory.getService(new ReportServiceImpl());
        int flag3 = reportService.updateAuditorID(id,auditorID);
        System.out.println("flag3: "+flag3);

        boolean flag = false;
        if(flag1==1 && flag3 == 1) flag = true;
        //PrintJson.printJsonFlag(response,flag);
        return Results.newSuccessResult(null);
    }

    @PostMapping("save.do")
    private Result save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行填写报告的添加操作");
        String id = request.getParameter("id");
        String studyID = request.getParameter("studyID");
        String patientID = request.getParameter("patientID");
        String reportStatus = "1";

        //UserService u1 = (UserService) ServiceFactory.getService(new UserServiceImpl());
        String userName = request.getParameter("createUserID"); //这里createUserID是名字
        String createUserID = userService.getIdByName(userName);

        String auditorID = request.getParameter("auditorID");
        String imagingFindings = request.getParameter("imagingFindings");
        String diagnosticOpinion = request.getParameter("diagnosticOpinion");
        String bodyPart = request.getParameter("bodyPart");
        String diseaseName = request.getParameter("diseaseName");
        String diseaseDescription = request.getParameter("diseaseDescription");
        String createTime = DateTimeUtil.getSysTime(); //时间是自动生成的当前时间
        String positive = request.getParameter("positive");
        String elapsedTime = request.getParameter("elapsedTime");

        //判断是否为审核医生
        Boolean flag0 = Boolean.valueOf(request.getParameter("flag0"));
        System.out.println(flag0);
        Report r = new Report();
        r.setId(id);
        r.setStudyID(studyID);
        r.setPatientID(patientID);
        r.setReportStatus(reportStatus);
        r.setCreateUserID(createUserID);
        if(flag0) {
            r.setAuditorID(createUserID);
        }
        r.setImagingFindings(imagingFindings);
        r.setDiagnosticOpinion(diagnosticOpinion);
        r.setBodyPart(bodyPart);
        r.setDiseaseName(diseaseName);
        r.setDiseaseDescription(diseaseDescription);
        r.setCreateTime(createTime);
        r.setPositive(positive);
        r.setElapsedTime(elapsedTime);

        System.out.println("要保存的报告："+r);

        //ReportService re = (ReportService)ServiceFactory.getService(new ReportServiceImpl());
        Boolean flag1 = Boolean.valueOf(request.getParameter("flag"));
        int result = 0;
        boolean flag = false;
        System.out.println("flag1:"+flag1);
        if(flag1 == false)
            result = reportService.save(r);
        else if(flag1)
            result = reportService.update(r);

        flag= result == 1;
        System.out.println("啦啦啦啦啦啦啦啦啦啦啦啦啦"+flag);

        if(flag && flag0){
            reportService.updateAuditorID(id,createUserID);
            reportService.updateStudyInfoStatus(studyID,"6"); //updateStudyinfoStatus
            reportService.updateReportStatus(studyID,"1");
        }
        return Results.newSuccessResult(null);
    }

    @GetMapping("report_print.do")
    private void detail(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        String id = request.getParameter("id");
        //ReportService rs0 = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
        String accessionNumber = reportService.getByIdOrStudyID(id).getStudyID();
        //动态代理产生的对象在使用一次后会结束生命周期
        //RegisterService rs = (RegisterService) ServiceFactory.getService(new RegisterServiceImpl());
        Patient patient = registerService.getPatientByaccessionNumber(accessionNumber);
        //RegisterService rs1 = (RegisterService) ServiceFactory.getService(new RegisterServiceImpl());
        StudyInfo studyinfo = registerService.getStudyInfoByAccessionNumber(accessionNumber);
        //ReportService rs2 = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
        Report report = reportService.getByIdOrStudyID(id);

        String createUserID = report.getCreateUserID();
        //UserService rs3 = (UserService) ServiceFactory.getService(new UserServiceImpl());
        String createUserName = userService.getNameByID(createUserID);

        String AuditorID = report.getAuditorID();
        //UserService rs4 = (UserService) ServiceFactory.getService(new UserServiceImpl());
        String AuditorName = userService.getNameByID(AuditorID);

        request.setAttribute("patient", patient);
        request.setAttribute("studyinfo", studyinfo);
        request.setAttribute("report", report);
        request.setAttribute("createUserName", createUserName);
        request.setAttribute("AuditorName", AuditorName);
        //window.location.href = "workbench/check/detail.jsp";
        request.getRequestDispatcher("/workbench/report_review/report_print.jsp").forward(request, response);
    }

    @PostMapping("printReport.do")
    private void printReport(HttpServletRequest request, HttpServletResponse response) {

        //ReportService re1 = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
        String id= request.getParameter("id");
        String reportStatus = reportService.getReportStatusById(id);
        System.out.println("打印报告 reportStatus："+reportStatus);
        boolean flag = false;
        if(reportStatus.equals("3")){
            //ReportService re2 = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
            int result = reportService.updateReportStatus(id,"4"); //修改状态为4
            System.out.println("打印是否成功？"+result);
            if(result == 1) flag = true;
        }
        else System.out.println("打印失败");
        //PrintJson.printJsonObj(response,flag);
    }

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


    //获取python的数据
    @PostMapping("get_python_message.do")
    private void get_python_message(@RequestBody String data){
        System.out.println("进入接收");
        try (JsonReader jsonReader = Json.createReader(new StringReader(data))) {
            JsonObject jsonObject = jsonReader.readObject();
            String content = jsonObject.getString("报告生成结果");
            dataHolder.set(content);
        }
    }

    //发送python获取的数据
    @GetMapping("sent_python_message.do")
    private Result sent_python_message() {
        System.out.println("进入发送");
        String data = dataHolder.get();
        dataHolder.set(null);
        return Results.newSuccessResult(data);
    }

    @GetMapping("get_data.do")
    private Result getdata(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行填写报告的从后端向前端传值操作");
        String id = UUIDUtil.getUUID();
        System.out.println("id:"+id);
        return Results.newSuccessResult(id);
    }


}
