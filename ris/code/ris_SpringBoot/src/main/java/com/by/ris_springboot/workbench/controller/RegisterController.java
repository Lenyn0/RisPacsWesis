package com.by.ris_springboot.workbench.controller;


import com.by.ris_springboot.utils.UUIDUtil;
import com.by.ris_springboot.vo.Result;
import com.by.ris_springboot.vo.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.by.ris_springboot.workbench.service.RegisterService;
import com.by.ris_springboot.workbench.domain.Patient;
import com.by.ris_springboot.workbench.domain.StudyInfo;

import org.dcm4che3.util.UIDUtils;

@RestController
@RequestMapping("workbench/register/")
public class RegisterController {

    @Autowired
    private RegisterService registerService;


    @GetMapping("pageList.do")
    private Result pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询登记信息列表的操作【结合条件查询+分页查询】");
        String patientname = request.getParameter("patientname");
        String department = request.getParameter("department");
        String clinicianID = request.getParameter("clinicianID");
        String registrarID = request.getParameter("registrarID");
        String emergency = request.getParameter("emergency");
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
        map.put("clinicianID",clinicianID);
        map.put("registrarID",registrarID);
        map.put("emergency",emergency);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        //针对于登记模块的pageList方法需要查询的检查信息表中状态为1的
        //map.put("status","1");
//        RegisterService rs = (RegisterService) ServiceFactory.getService(new RegisterServiceImpl());
//        PaginationVO<StudyInfo> vo = rs.pageList(map);
//        PrintJson.printJsonObj(response, vo);
        return Results.newSuccessResult(registerService.pageList(map));
    }


    @PostMapping("delete.do")
    private Result delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行登记信息的删除操作");
        String accessionNumbers[] = request.getParameterValues("accessionNumber");
        //RegisterService rs = (RegisterService) ServiceFactory.getService(new RegisterServiceImpl());
        //boolean flag = rs.delete(accessionNumbers);
        //PrintJson.printJsonFlag(response, flag);
        return Results.newSuccessResult(registerService.delete(accessionNumbers));
    }

    //获取登记表单详情
    @GetMapping("edit.do")
    private Result edit(String accessionNumber){
        System.out.println("进入到跳转到登记修改页的操作");
        Patient patient = registerService.getPatientByaccessionNumber(accessionNumber);
        StudyInfo studyinfo = registerService.getStudyInfoByAccessionNumber(accessionNumber);
        Map<String, Object> data = new HashMap<>();
        data.put("patient", patient);
        data.put("studyinfo", studyinfo);
        return Results.newSuccessResult(data);
    }

    //更新登记表单
    @PostMapping("update.do")
    private void update(@ModelAttribute Patient patient, @ModelAttribute StudyInfo studyInfo,HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        System.out.println("执行登记信息更新操作");
        String studyInstanceUID = UIDUtils.createUID();
        studyInfo.setStudyInstanceUID(studyInstanceUID);
        boolean flag = registerService.update(patient,studyInfo);
        if(flag){
            //修改成功
            response.sendRedirect(request.getContextPath()+"/workbench/register/index.jsp");
        }
    }

    @GetMapping("getPatientNameList.do")
    private Result getPatientNameList(String name) {
        System.out.println("取得病人名称列表（按照病人名称进行模糊查询）");
        List<String> nameList = registerService.getPatientNameList(name);
        return Results.newSuccessResult(nameList);
    }

    @PostMapping("getPatientByName.do")
    private Result getPatientByName(String name) {
        System.out.println("取得病人全部信息（按照病人名称进行精确查询）");
        Patient patient = registerService.getPatientByName(name);
        return Results.newSuccessResult(patient);
    }

    @PostMapping("save.do")
    private void save(@ModelAttribute Patient patient, @ModelAttribute StudyInfo studyInfo,HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
        System.out.println("执行添加登记表单的操作");
        //patient表
        String id = UUIDUtil.getUUID();
        patient.setId(id);

        //studyinfo表
        String accessionNumber = UUIDUtil.getUUID();
        String studyInstanceUID = UIDUtils.createUID();
        studyInfo.setStudyInstanceUID(studyInstanceUID);
        studyInfo.setAccessionNumber(accessionNumber);

        System.out.println(patient);
        System.out.println(studyInfo);
        boolean flag = registerService.save(patient,studyInfo);
        if(flag){
            //创建成功
            response.sendRedirect(request.getContextPath()+"/workbench/register/index.jsp");
        }
    }

    @GetMapping("schedule.do")
    private Result schedule(String accessionNumber) {
        System.out.println("进入到跳转到预约页的操作");
        //String accessionNumber = request.getParameter("accessionNumber");
        //动态代理产生的对象在使用一次后会结束生命周期
        Patient patient = registerService.getPatientByaccessionNumber(accessionNumber);
        StudyInfo studyinfo =registerService.getStudyInfoByAccessionNumber(accessionNumber);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("patient",patient);
        map.put("studyinfo",studyinfo);
        return Results.newSuccessResult(map);
    }

    //预约
    @PostMapping("appointment.do")
    private Result appointment( String accessionNumber, String scheduledProcedureStepStart) {
        System.out.println("执行预约时间操作");
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
        //RegisterService rs = (RegisterService) ServiceFactory.getService(new RegisterServiceImpl());
        boolean flag = registerService.appointment(s);
        return Results.newSuccessResult(null);
        //PrintJson.printJsonFlag(response, flag);
    }
}
