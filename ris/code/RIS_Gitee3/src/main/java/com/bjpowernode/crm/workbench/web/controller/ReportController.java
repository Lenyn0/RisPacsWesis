package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.DiseaseDictionary;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Patient;
import com.bjpowernode.crm.workbench.domain.Report;
import com.bjpowernode.crm.workbench.domain.StudyInfo;
import com.bjpowernode.crm.workbench.service.RegisterService;
import com.bjpowernode.crm.workbench.service.impl.RegisterServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.ReportServiceImpl;
import com.bjpowernode.crm.workbench.service.ReportService;

import javax.json.JsonException;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.concurrent.atomic.AtomicReference;
import javax.json.JsonReader;
import javax.json.Json;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;


//一个模块一个servlet
public class ReportController extends HttpServlet {

    private static final AtomicReference<String> dataHolder = new AtomicReference<>("");
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到报告控制器");
        String path = request.getServletPath();

        if ("/workbench/Report/save.do".equals(path)) {
            System.out.println("正在保存");
            save(request, response);
            System.out.println("保存成功");
        }
        else if ("/workbench/Report/get_data.do".equals(path)) {
            getdata(request, response);
        }
        else if ("/workbench/Report/pageList.do".equals(path)) {
            reportPageList(request, response);
        }
        else if ("/workbench/Report/get_datalist.do".equals(path)) {
            getUserList(request, response, "3");
        }
        else if ("/workbench/Report/get_datalist2.do".equals(path)) {
            getUserList(request, response, "4");
        }
        else if ("/workbench/Report/pageList_review.do".equals(path)) {
            reviewPageList(request, response);
        }
        else if ("/workbench/Report/get_report.do".equals(path)) {
            getReportByIdOrStudyID(request, response);
        }
        else if ("/workbench/Report/pass.do".equals(path)) { //修改report表和studyInfo表中的状态
            updateReportStatus(request, response, "3", "7");
        }
        else if ("/workbench/Report/reject.do".equals(path)) { //修改report表和studyInfo表中的状态，不对，study_info表中的状态不需要修改
            updateReportStatus(request, response, "2", "6");
        }
        else if ("/workbench/Report/get_age_gender.do".equals(path)) {
            getPatientByStudyIDOrPatientID(request, response);
        }
        else if ("/workbench/Report/get_data_from_studyInfo.do".equals(path)) {
            getStudyInfoByAccessionNumber(request, response);
        }
        else if("/workbench/Report/get_bodypart_from_Session.do".equals(path)){
            getBodypartFromSession(request,response);
        }
        else if ("/workbench/Report/get_report0.do".equals(path)) {
            getReportByStudyId(request, response);
        }
        else if ("/workbench/Report/printReport.do".equals(path)) {
            printReport(request, response);
        }
        else if ("/workbench/Report/get_diseaseDescription.do".equals(path)) {
            getDiseaseDescription(request, response);
        }
        else if ("/workbench/report_review/report_print.do".equals(path)) {
            detail(request, response);
        }
        else if ("/workbench/Report/get_python_message.do".equals(path)) {
            get_python_message(request, response);
        }
        else if ("/workbench/Report/sent_python_message.do".equals(path)) {
            sent_python_message(request, response);
        }
    }
    public static String restoreUnicodeEscape(String input) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (i < input.length()) {
            if (input.charAt(i) == '\\' && i + 1 < input.length() && input.charAt(i + 1) == 'u') {
                // 找到 Unicode 转义序列
                String unicode = input.substring(i + 2, i + 6); // 提取 Unicode 序列
                int codePoint = Integer.parseInt(unicode, 16); // 将 Unicode 序列转换为整数
                builder.append((char) codePoint); // 将整数转换为字符并添加到结果中
                i += 6; // 跳过 Unicode 转义序列
            } else {
                // 非转义字符直接添加到结果中
                builder.append(input.charAt(i));
                i++;
            }
        }
        return builder.toString();
    }

    // 在方法中使用 Gson 解析 JSON 数据
    protected void get_python_message(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入接收");
        request.setCharacterEncoding("UTF-8");
        // 读取请求数据
        BufferedReader reader = request.getReader();
        StringBuilder data = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            data.append(line);
        }

        String originalData = restoreUnicodeEscape(data.toString());
        System.out.println("接收到的数据：" + originalData); // 打印接收到的数据


        try {
            // 创建 Gson 对象
            Gson gson = new Gson();

            // 解析 JSON 数据为一个包含键值对的 Map 对象
            Map<String, String> jsonMap = gson.fromJson(originalData, new TypeToken<Map<String, String>>() {}.getType());

            // 获取报告生成结果的值
            String content = jsonMap.get("报告生成结果");

            // 打印解析后的数据
            System.out.println("解析后的数据：" + content);
            // 存储数据
            dataHolder.set(content);

        } catch (JsonSyntaxException e) {
            // JSON 解析失败时的处理
            System.out.println("解析失败：" + e.getMessage());
        }

        // 设置响应内容类型为 JSON
        response.setContentType("application/json");

        // 返回成功状态的 JSON 响应
        response.getWriter().write("{\"status\":\"success\"}");
    }


    protected void sent_python_message(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入发送");
        String data = dataHolder.get();
        if (data != null && !data.isEmpty()) {
            // 如果数据不为空，则返回存储的数据
            response.setContentType("application/json");
            response.getWriter().write(data);
            System.out.println(data);
            // 发送完后将内容清空
            dataHolder.set(null);
        } else {
            // 如果数据为空，则不发送任何内容
            System.out.println("数据为空，无需发送");
        }
    }

    private void detail(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        String id = request.getParameter("id");
        ReportService rs0 = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
        String accessionNumber = rs0.getByIdOrStudyID(id).getStudyID();
        //动态代理产生的对象在使用一次后会结束生命周期
        RegisterService rs = (RegisterService) ServiceFactory.getService(new RegisterServiceImpl());
        Patient patient = rs.getPatientByaccessionNumber(accessionNumber);
        RegisterService rs1 = (RegisterService) ServiceFactory.getService(new RegisterServiceImpl());
        StudyInfo studyinfo = rs1.getStudyInfoByAccessionNumber(accessionNumber);
        ReportService rs2 = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
        Report report = rs2.getByIdOrStudyID(id);

        String createUserID = report.getCreateUserID();
        UserService rs3 = (UserService) ServiceFactory.getService(new UserServiceImpl());
        String createUserName = rs3.getNameByID(createUserID);

        String AuditorID = report.getAuditorID();
        UserService rs4 = (UserService) ServiceFactory.getService(new UserServiceImpl());
        String AuditorName = rs4.getNameByID(AuditorID);

        request.setAttribute("patient", patient);
        request.setAttribute("studyinfo", studyinfo);
        request.setAttribute("report", report);
        request.setAttribute("createUserName", createUserName);
        request.setAttribute("AuditorName", AuditorName);
        //window.location.href = "workbench/check/detail.jsp";
        request.getRequestDispatcher("/workbench/report_review/report_print.jsp").forward(request, response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行填写报告的添加操作");
        String id = request.getParameter("id");
        String studyID = request.getParameter("studyID");
        String patientID = request.getParameter("patientID");
        String reportStatus = "1";

        UserService u1 = (UserService) ServiceFactory.getService(new UserServiceImpl());
        String userName = request.getParameter("createUserID"); //这里createUserID是名字
        String createUserID = u1.getIdByName(userName);

        String auditorID = request.getParameter("auditorID");
        String imagingFindings = request.getParameter("imagingFindings");
        String diagnosticOpinion = request.getParameter("diagnosticOpinion");
        String bodyPart = request.getParameter("bodyPart");
        String diseaseName = request.getParameter("diseaseName");
        String diseaseDescription = request.getParameter("diseaseDescription");
        String createTime = DateTimeUtil.getSysTime(); //时间是自动生成的当前时间
        String positive = request.getParameter("positive");

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

        System.out.println("要保存的报告："+r);

        ReportService re = (ReportService)ServiceFactory.getService(new ReportServiceImpl());
        Boolean flag1 = Boolean.valueOf(request.getParameter("flag"));
        int result = 0;
        boolean flag = false;
        System.out.println("flag1:"+flag1);
        if(flag1 == false)
            result = re.save(r);
        else if(flag1)
            result = re.update(r);

        flag= result == 1;
        System.out.println("啦啦啦啦啦啦啦啦啦啦啦啦啦"+flag);
        /*if(flag==true && flag0==false) {
            //用检查号去遍历study_info表，将这条数据的状态更改为等待检查
            ReportService st = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
            st.updateStudyInfoStatus(studyID,"3"); //accessionNumber = studyID
        }
        else */
        if(flag && flag0){

            ReportService re1 = (ReportService)ServiceFactory.getService(new ReportServiceImpl());
            re1.updateAuditorID(id,createUserID);

            ReportService re2 = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
            re2.updateStudyInfoStatus(studyID,"6"); //updateStudyinfoStatus

            ReportService re3 = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
            re3.updateReportStatus(studyID,"1");
        }
        PrintJson.printJsonFlag(response,flag);
    }

    private void getdata(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行填写报告的从后端向前端传值操作");
        String id = UUIDUtil.getUUID();
        System.out.println("id:"+id);
        try {
            response.getWriter().print(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reportPageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到待写报告信息列表的操作（结合条件查询+分页查询）");
        //"studyID" :
        //"patientID"
        //"patientName
        //"status" :
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
        ReportService reportService = (ReportService) ServiceFactory.getService(new ReportServiceImpl());

        //vo位于src/main/java/com/bjpowernode/crm/vo/PaginationVO.java
        //里面有详细用法简述
        PaginationVO<StudyInfo> paginationVO = reportService.pageList_StudyInfo(map);
        System.out.println("输出成功");

        //vo--> {"total":100,"dataList":[{待写报告1},{2},{3}]}
        PrintJson.printJsonObj(response, paginationVO);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response, String privileges) {
        System.out.println("取得用户信息列表");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getByPrivileges(privileges);
        PrintJson.printJsonObj(response,uList);
    }

    private void reviewPageList(HttpServletRequest request, HttpServletResponse response) {
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
        ReportService reportService = (ReportService) ServiceFactory.getService(new ReportServiceImpl());

        //vo位于src/main/java/com/bjpowernode/crm/vo/PaginationVO.java
        //里面有详细用法简述
        PaginationVO<Report> paginationVO = reportService.pageList_Report(map);
        System.out.println("输出成功");

        //vo--> {"total":100,"dataList":[{待写报告1},{2},{3}]}
        PrintJson.printJsonObj(response, paginationVO);
    }

    private void getReportByIdOrStudyID(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据id查找这条报告的详细信息");
        String studyID= request.getParameter("studyID"); //studyID=id或studyID
        ReportService re = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
        Report report = re.getByIdOrStudyID(studyID);
        System.out.println(report);

        System.out.println("将病人号转化成为病人名，并获得病人年龄和性别");
        String patientID= request.getParameter("patientID");
        System.out.println("病人号:"+patientID);
        ReportService rs1 = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
        Patient patient =  rs1.getPatientByID(patientID);
        System.out.println("病人信息："+patient);

        System.out.println("将疾病名称ID转化为名称");
        String DiseaseName = report.getDiseaseName(); //获取疾病名称


        //获得三个参数，投照方式....
        System.out.println("进入查询投照方式，使用耗材，计划的程序步骤的相关描述。");
        System.out.println("studyID:"+studyID);
        ReportService rs2 = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
        StudyInfo studyInfo = rs2.getStudyInfoByAccessionNumber(studyID);
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

        PrintJson.printJsonObj(response,map);
    }

    private void updateReportStatus(HttpServletRequest request, HttpServletResponse response, String reportStatus, String status) {

        //修改report表中的状态 需要从前端传修改后的状态
        ReportService re = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
        String id= request.getParameter("id");
        //String reportStatus = request.getParameter("reportStatus");
        System.out.println("id为："+id+" 修改后的reportStatus为："+reportStatus);
        int flag1 = re.updateReportStatus(id,reportStatus);
        System.out.println("flag1:"+flag1);

        if(status.equals("7"))
        //修改study_info表中的状态 需要从前端传修改后的状态 错，不能改studyInfo
        {
            ReportService rs1 = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
            String studyID=request.getParameter("studyID");
            //String status = request.getParameter("status");
            System.out.println("studyID为："+studyID+" 修改后的status为："+status);
            boolean flag2 = rs1.updateStudyInfoStatus(studyID,status);
            System.out.println("flag2:"+flag2);
        }


        //获取审核医生id
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        //String auditorName= request.getParameter("auditorName"); //从前端获取审核医生姓名
        String auditorName= request.getParameter("auditorID"); //从前端获取审核医生姓名
        System.out.println("审核医生姓名: "+auditorName);
        String auditorID = us.getIdByName(auditorName);

        //修改tbl_report表中的审核医生ID
        ReportService re2 = (ReportService)ServiceFactory.getService(new ReportServiceImpl());
        int flag3 = re2.updateAuditorID(id,auditorID);
        System.out.println("flag3: "+flag3);

        boolean flag = false;
        if(flag1==1 && flag3 == 1) flag = true;
        PrintJson.printJsonFlag(response,flag);
    }

    private void getPatientByStudyIDOrPatientID(HttpServletRequest request, HttpServletResponse response) {

        String patientID= request.getParameter("patientID");
        ReportService rs2 = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
        Patient patient = rs2.getPatientByID(patientID);
        System.out.println("patient:"+patient);

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("age", patient.getAge());//病人ID
        map.put("gender", patient.getGender());//病人名称
        PrintJson.printJsonObj(response,map);
    }

    private void getStudyInfoByAccessionNumber(HttpServletRequest request, HttpServletResponse response) {
        String id= request.getParameter("studyID");
        System.out.println(id);
        ReportService rs1 = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
        StudyInfo studyInfo = rs1.getStudyInfoByAccessionNumber(id);
        System.out.println("hello???: "+studyInfo.toString());
        PrintJson.printJsonObj(response,studyInfo);
    }

    private void getBodypartFromSession(HttpServletRequest request, HttpServletResponse response) {
        String bodypart = request.getParameter("body");
        ReportService rs = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
        List<DiseaseDictionary> listBybodyPart = rs.getListBybodyPart(bodypart);
        System.out.println("listBybodyPart:"+listBybodyPart.toString());
        PrintJson.printJsonObj(response,listBybodyPart);
    }

    private void getReportByStudyId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据id查找这条报告的详细信息");
        String studyID= request.getParameter("studyID");
        ReportService re = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
        Report report = re.getByIdOrStudyID(studyID);
        /*System.out.println("report:"+report);
        System.out.println("lalal"+report.toString());*/
        //把上面的数据封装到一个map中
        /*Map<String,Object> map = new HashMap<String,Object>();
        map.put("id", report.getId());//报告号
        System.out.println("id到底是啥："+report.getId());
        //diseaseDescription,imagingFindings,diagnosticOpinion疾病描述，影像学所见，诊断意见
        map.put("diseaseDescription",report.getDiseaseDescription());//
        map.put("imagingFindings",report.getImagingFindings());//
        map.put("diagnosticOpinion",report.getDiagnosticOpinion());//*/
        PrintJson.printJsonObj(response,report);
    }

    private void printReport(HttpServletRequest request, HttpServletResponse response) {

        ReportService re1 = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
        String id= request.getParameter("id");
        String reportStatus = re1.getReportStatusById(id);
        System.out.println("打印报告 reportStatus："+reportStatus);
        boolean flag = false;
        if(reportStatus.equals("3")){
            ReportService re2 = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
            int result = re2.updateReportStatus(id,"4"); //修改状态为4
            System.out.println("打印是否成功？"+result);
            if(result == 1) flag = true;
        }
        else System.out.println("打印失败");
        PrintJson.printJsonObj(response,flag);
    }

    private void getDiseaseDescription(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        ReportService us = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
        String description = us.getDiseaseDescription(name);
        System.out.println("description:"+description);
        PrintJson.printJsonObj(response,description);
    }
}


