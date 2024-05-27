package com.by.ris_springboot.workbench.controller;



import com.by.ris_springboot.utils.DateTimeUtil;
import com.by.ris_springboot.vo.PaginationVO;
import com.by.ris_springboot.vo.Result;
import com.by.ris_springboot.vo.Results;
import com.by.ris_springboot.workbench.domain.Patient;
import com.by.ris_springboot.workbench.domain.StudyInfo;
import com.by.ris_springboot.workbench.service.SigninService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("workbench/sign_in/")
public class SigninController {

    @Autowired
    SigninService signinService;

    //签到-签到
    @PostMapping("signin.do")
    private Result signin(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行签到的操作");
        String accessionNumber = request.getParameter("accessionNumber");
        StudyInfo s = new StudyInfo();
        s.setAccessionNumber(accessionNumber);
        s.setStatus("3");
        boolean flag = signinService.signin(s);
        return Results.newSuccessResult(null);
        //PrintJson.printJsonFlag(response, flag);
    }


    //签到-取消预约
    @PostMapping("cancel.do")
    private Result cancel(HttpServletRequest request, HttpServletResponse response){
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
        //SigninService ss = (SigninService) ServiceFactory.getService(new SigninServiceImpl());
        boolean flag = signinService.cancel(s);
        return Results.newSuccessResult(null);
        //PrintJson.printJsonFlag(response, flag);
    }

    //签到-修改预约时间-更新预约时间
    @PostMapping ("updatescheduledProcedureStepStart.do")
    private Result updatescheduledProcedureStepStart(HttpServletRequest request, HttpServletResponse response) {
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
        //SigninService ss = (SigninService) ServiceFactory.getService(new SigninServiceImpl());
        boolean flag = signinService.updatescheduledProcedureStepStart(s);
        return Results.newSuccessResult(null);
        //PrintJson.printJsonFlag(response, flag);
    }


    //签到-修改预约时间-获取信息
    @GetMapping("edit.do")
    private Result edit(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到跳转到预约修改页的操作");
        String accessionNumber = request.getParameter("accessionNumber");
        //动态代理产生的对象在使用一次后会结束生命周期
        //SigninService ss = (SigninService) ServiceFactory.getService(new SigninServiceImpl());
        Patient patient = signinService.getPatientByaccessionNumber(accessionNumber);
        //SigninService ss1 = (SigninService) ServiceFactory.getService(new SigninServiceImpl());
        StudyInfo studyinfo = signinService.getStudyInfoByAccessionNumber(accessionNumber);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("patient",patient);
        map.put("studyinfo",studyinfo);
        return Results.newSuccessResult(map);
        //PrintJson.printJsonObj(response, map);
    }


    //签到-获取预约列表
    @GetMapping("pageList.do")
    private Result pageList(HttpServletRequest request, HttpServletResponse response) {
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
        PaginationVO<StudyInfo> vo = signinService.pageList(map);
        return Results.newSuccessResult(vo);
    }

}
