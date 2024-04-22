package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Patient;
import com.bjpowernode.crm.workbench.domain.StudyInfo;
import com.bjpowernode.crm.workbench.service.SigninService;
import com.bjpowernode.crm.workbench.service.impl.SigninServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SigninController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到签到模块控制器");

        String path = request.getServletPath();

        if("/workbench/sign_in/pageList.do".equals(path)){

            pageList(request,response);

        }else if("/workbench/sign_in/edit.do".equals(path)){

            edit(request,response);

        }else if("/workbench/sign_in/updatescheduledProcedureStepStart.do".equals(path)){

            updatescheduledProcedureStepStart(request,response);

        }else if("/workbench/sign_in/cancel.do".equals(path)){

            cancel(request,response);

        }else if("/workbench/sign_in/signin.do".equals(path)){

            signin(request,response);

        }else if("/workbench/sign_in/xxx.do".equals(path)){

            //xxx(request,response);

        }

    }

    private void signin(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行签到的操作");
        String accessionNumber = request.getParameter("accessionNumber");
        StudyInfo s = new StudyInfo();
        s.setAccessionNumber(accessionNumber);
        s.setStatus("3");
        SigninService ss = (SigninService) ServiceFactory.getService(new SigninServiceImpl());
        boolean flag = ss.signin(s);
        PrintJson.printJsonFlag(response, flag);
    }

    private void cancel(HttpServletRequest request, HttpServletResponse response){
        System.out.println("执行预约时间更新操作");
        String accessionNumber = request.getParameter("accessionNumber");
        String cancellationReason = request.getParameter("cancellationReason");
        String cancellationTime = DateTimeUtil.getSysTime();
        String cancellationUser = request.getParameter("id");
        StudyInfo s = new StudyInfo();
        s.setAccessionNumber(accessionNumber);
        s.setStatus("8");
        s.setScheduledProcedureStepStartDate("");
        s.setScheduledProcedureStepStartTime("");
        s.setCancellationReason(cancellationReason);
        s.setCancellationTime(cancellationTime);
        s.setCancellationUser(cancellationUser);
        SigninService ss = (SigninService) ServiceFactory.getService(new SigninServiceImpl());
        boolean flag = ss.cancel(s);
        PrintJson.printJsonFlag(response, flag);
    }

    private void updatescheduledProcedureStepStart(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行预约时间更新操作");
        String accessionNumber = request.getParameter("accessionNumber");
        //System.out.println(accessionNumber);
        String scheduledProcedureStepStart = request.getParameter("scheduledProcedureStepStart");//yy-mm-dd hh:ii格式的时间
        String scheduledProcedureStepStartDate = "";
        String scheduledProcedureStepStartTime = "";
        if(!("".equals(scheduledProcedureStepStart))){
            String str[] = scheduledProcedureStepStart.split(" ");
            scheduledProcedureStepStartDate = str[0];
            scheduledProcedureStepStartTime = str[1];
        }
        StudyInfo s = new StudyInfo();
        s.setAccessionNumber(accessionNumber);
        s.setScheduledProcedureStepStartDate(scheduledProcedureStepStartDate);
        s.setScheduledProcedureStepStartTime(scheduledProcedureStepStartTime);
        SigninService ss = (SigninService) ServiceFactory.getService(new SigninServiceImpl());
        boolean flag = ss.updatescheduledProcedureStepStart(s);
        PrintJson.printJsonFlag(response, flag);
    }

    private void edit(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到跳转到预约修改页的操作");
        String accessionNumber = request.getParameter("accessionNumber");
        //动态代理产生的对象在使用一次后会结束生命周期
        SigninService ss = (SigninService) ServiceFactory.getService(new SigninServiceImpl());
        Patient patient = ss.getPatientByaccessionNumber(accessionNumber);
        SigninService ss1 = (SigninService) ServiceFactory.getService(new SigninServiceImpl());
        StudyInfo studyinfo = ss1.getStudyInfoByAccessionNumber(accessionNumber);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("patient",patient);
        map.put("studyinfo",studyinfo);
        PrintJson.printJsonObj(response, map);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询预约信息列表的操作【结合条件查询+分页查询】");
        String patientname = request.getParameter("patientname");
        String department = request.getParameter("department");
        String scheduledProcedureStepStartDate = request.getParameter("scheduledProcedureStepStartDate");
        String scheduledProcedureStepStartTime = request.getParameter("scheduledProcedureStepStartTime");
        String clinicianID = request.getParameter("clinicianID");
//        String registrarID = request.getParameter("registrarID");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("patientname", patientname);
        map.put("department", department);
        map.put("scheduledProcedureStepStartDate",scheduledProcedureStepStartDate);
        map.put("scheduledProcedureStepStartTime",scheduledProcedureStepStartTime);
        map.put("clinicianID",clinicianID);
//        map.put("registrarID",registrarID);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        //针对于签到模块的pageList方法需要查询的检查信息表中状态为2的
        map.put("status","2");
        SigninService ss = (SigninService) ServiceFactory.getService(new SigninServiceImpl());
        PaginationVO<StudyInfo> vo = ss.pageList(map);
        PrintJson.printJsonObj(response, vo);
    }

}
