package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Patient;
import com.bjpowernode.crm.workbench.domain.StudyInfo;
import com.bjpowernode.crm.workbench.service.RegisterService;
import com.bjpowernode.crm.workbench.service.impl.RegisterServiceImpl;
import org.dcm4che3.util.UIDUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterController extends HttpServlet {
    //获取日志记录器Logger，名字为本类类名
    private static Logger log = Logger.getLogger(RegisterController.class);
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到登记模块控制器");

        String path = request.getServletPath();

        if("/workbench/register/add.do".equals(path)){

            add(request,response);

        }else if("/workbench/register/getPatientNameList.do".equals(path)){

            getPatientNameList(request,response);

        }else if("/workbench/register/getPatientByName.do".equals(path)){

            getPatientByName(request,response);

        }else if("/workbench/register/pageList.do".equals(path)){

            pageList(request,response);

        }else if("/workbench/register/save.do".equals(path)){

            save(request,response);

        }else if("/workbench/register/delete.do".equals(path)){

            delete(request,response);

        }else if("/workbench/register/edit.do".equals(path)){

            edit(request,response);

        }else if("/workbench/register/update.do".equals(path)){

            update(request,response);

        }else if("/workbench/register/schedule.do".equals(path)){

            schedule(request,response);

        }else if("/workbench/register/appointment.do".equals(path)){

            appointment(request,response);

        }else if("/workbench/register/xxx.do".equals(path)){

            //xxx(request,response);

        }

    }

    private void appointment(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行预约时间操作");
        String accessionNumber = request.getParameter("accessionNumber");
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
        RegisterService rs = (RegisterService) ServiceFactory.getService(new RegisterServiceImpl());
        boolean flag = rs.appointment(s);
        PrintJson.printJsonFlag(response, flag);
    }

    private void schedule(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到跳转到预约页的操作");
        String accessionNumber = request.getParameter("accessionNumber");
        //动态代理产生的对象在使用一次后会结束生命周期
        RegisterService rs = (RegisterService) ServiceFactory.getService(new RegisterServiceImpl());
        Patient patient = rs.getPatientByaccessionNumber(accessionNumber);
        RegisterService rs1 = (RegisterService) ServiceFactory.getService(new RegisterServiceImpl());
        StudyInfo studyinfo = rs1.getStudyInfoByAccessionNumber(accessionNumber);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("patient",patient);
        map.put("studyinfo",studyinfo);
        PrintJson.printJsonObj(response, map);
    }

    private void update(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        System.out.println("执行登记信息更新操作");
        //patient表
        String name = request.getParameter("patientname");
        String namePinYin = request.getParameter("patientnamePinYin");
        String age = request.getParameter("age");
        String ageType = request.getParameter("ageType");
        String gender = request.getParameter("gender");
        String birthDate = request.getParameter("birthDate");
        String address = request.getParameter("address");
        String pregnancy = request.getParameter("pregnancy");
        String inpatientDepartment = request.getParameter("inpatientDepartment");
        String inpatientBedNumber = request.getParameter("inpatientBedNumber");
        String inpatientNumber = request.getParameter("inpatientNumber");
        String phoneNumber = request.getParameter("phoneNumber");
        String patientType = request.getParameter("patientType");
        String IDType = request.getParameter("IDType");
        String IDNumber = request.getParameter("IDNumber");
        String healthCareType = request.getParameter("healthCareType");
        //studyinfo表
        String accessionNumber = request.getParameter("accessionNumber");
        String status = request.getParameter("status");
        log.info("当前检查信息状态："+status);
        String department = request.getParameter("department");
        String emergency = request.getParameter("emergency");
        String clinicianID = request.getParameter("clinicianID");
        String registrarID = request.getParameter("registrarID");
        String bodyParts = request.getParameter("bodyParts");
        String modality = request.getParameter("modality");
        String studyDevice = request.getParameter("studyDevice");//此处得到的参数是设备id
//        String studyInstanceUID = UUIDUtil.getUUID();//目前还没装上dcm4che工具库，先用UUIDUtil工具生成
        String studyInstanceUID = UIDUtils.createUID();
        String requestedProcedureDescription = request.getParameter("requestedProcedureDescription");
        Patient p = new Patient();
        StudyInfo s = new StudyInfo();
        p.setName(name);
        p.setNamePinYin(namePinYin);
        p.setAge(age);
        p.setAgeType(ageType);
        p.setGender(gender);
        p.setBirthDate(birthDate);
        p.setAddress(address);
        p.setPregnancy(pregnancy);
        p.setInpatientDepartment(inpatientDepartment);
        p.setInpatientBedNumber(inpatientBedNumber);
        p.setInpatientNumber(inpatientNumber);
        p.setPhoneNumber(phoneNumber);
        p.setPatientType(patientType);
        p.setIDType(IDType);
        p.setIDNumber(IDNumber);
        p.setHealthCareType(healthCareType);
        s.setAccessionNumber(accessionNumber);
        s.setStatus(status);
        s.setDepartment(department);
        s.setEmergency(emergency);
        s.setClinicianID(clinicianID);
        s.setRegistrarID(registrarID);
        s.setBodyParts(bodyParts);
        s.setModality(modality);
        s.setStudyDevice(studyDevice);
        s.setStudyInstanceUID(studyInstanceUID);
        s.setRequestedProcedureDescription(requestedProcedureDescription);
        RegisterService rs = (RegisterService) ServiceFactory.getService(new RegisterServiceImpl());
        boolean flag = rs.update(p,s);
        if(flag){
            //修改成功
            response.sendRedirect(request.getContextPath()+"/workbench/register/index.jsp");
        }
    }

    private void getPatientByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得病人全部信息（按照病人名称进行精确查询）");
        String name = request.getParameter("name");
        RegisterService rs = (RegisterService) ServiceFactory.getService(new RegisterServiceImpl());
        Patient patient = rs.getPatientByName(name);
        PrintJson.printJsonObj(response, patient);
    }

    private void getPatientNameList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得病人名称列表（按照病人名称进行模糊查询）");
        String name = request.getParameter("name");
        RegisterService rs = (RegisterService) ServiceFactory.getService(new RegisterServiceImpl());
        List<String> nameList = rs.getPatientNameList(name);
        PrintJson.printJsonObj(response, nameList);
    }

    private void edit(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        System.out.println("进入到跳转到登记修改页的操作");
        String accessionNumber = request.getParameter("accessionNumber");
        //动态代理产生的对象在使用一次后会结束生命周期
        RegisterService rs = (RegisterService) ServiceFactory.getService(new RegisterServiceImpl());
        Patient patient = rs.getPatientByaccessionNumber(accessionNumber);
        RegisterService rs1 = (RegisterService) ServiceFactory.getService(new RegisterServiceImpl());
        StudyInfo studyinfo = rs1.getStudyInfoByAccessionNumber(accessionNumber);
        request.setAttribute("patient", patient);
        request.setAttribute("studyinfo", studyinfo);
        request.getRequestDispatcher("/workbench/register/edit.jsp").forward(request, response);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行登记信息的删除操作");
        String accessionNumbers[] = request.getParameterValues("accessionNumber");
        RegisterService rs = (RegisterService) ServiceFactory.getService(new RegisterServiceImpl());
        boolean flag = rs.delete(accessionNumbers);
        PrintJson.printJsonFlag(response, flag);
    }

    private void save(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        System.out.println("执行添加登记表单的操作");
        //patient表
        String id = UUIDUtil.getUUID();
        String name = request.getParameter("patientname");
        String namePinYin = request.getParameter("patientnamePinYin");
        String age = request.getParameter("age");
        String ageType = request.getParameter("ageType");
        String gender = request.getParameter("gender");
        String birthDate = request.getParameter("birthDate");
        String address = request.getParameter("address");
        String pregnancy = request.getParameter("pregnancy");
        String inpatientDepartment = request.getParameter("inpatientDepartment");
        String inpatientBedNumber = request.getParameter("inpatientBedNumber");
        String inpatientNumber = request.getParameter("inpatientNumber");
        String phoneNumber = request.getParameter("phoneNumber");
        String patientType = request.getParameter("patientType");
        String IDType = request.getParameter("IDType");
        String IDNumber = request.getParameter("IDNumber");
        String healthCareType = request.getParameter("healthCareType");
        //studyinfo表
        String accessionNumber = UUIDUtil.getUUID();
        String department = request.getParameter("department");
        String emergency = request.getParameter("emergency");
        String clinicianID = request.getParameter("clinicianID");
        String registrarID = request.getParameter("registrarID");
        String bodyParts = request.getParameter("bodyParts");
        String modality = request.getParameter("modality");
        String studyDevice = request.getParameter("studyDevice");//此处得到的参数是设备id
//        String studyInstanceUID = UUIDUtil.getUUID();//目前还没装上dcm4che工具库，先用UUIDUtil工具生成
        String studyInstanceUID = UIDUtils.createUID();
        String requestedProcedureDescription = request.getParameter("requestedProcedureDescription");
        Patient p = new Patient();
        StudyInfo s = new StudyInfo();
        p.setId(id);
        p.setName(name);
        p.setNamePinYin(namePinYin);
        p.setAge(age);
        p.setAgeType(ageType);
        p.setGender(gender);
        p.setBirthDate(birthDate);
        p.setAddress(address);
        p.setPregnancy(pregnancy);
        p.setInpatientDepartment(inpatientDepartment);
        p.setInpatientBedNumber(inpatientBedNumber);
        p.setInpatientNumber(inpatientNumber);
        p.setPhoneNumber(phoneNumber);
        p.setPatientType(patientType);
        p.setIDType(IDType);
        p.setIDNumber(IDNumber);
        p.setHealthCareType(healthCareType);
        s.setAccessionNumber(accessionNumber);
        s.setStatus("1");
        //s.setPatientID(id);根据是否存在病人设置，若已存在病人则使用已存在的病人的id否则使用新生成的id
        s.setDepartment(department);
        s.setEmergency(emergency);
        s.setClinicianID(clinicianID);
        s.setRegistrarID(registrarID);
        s.setBodyParts(bodyParts);
        s.setModality(modality);
        s.setStudyDevice(studyDevice);
        s.setStudyInstanceUID(studyInstanceUID);
        s.setRequestedProcedureDescription(requestedProcedureDescription);
        RegisterService rs = (RegisterService) ServiceFactory.getService(new RegisterServiceImpl());
        boolean flag = rs.save(p,s);
        if(flag){
            //创建成功
            response.sendRedirect(request.getContextPath()+"/workbench/register/index.jsp");
        }
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
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
        RegisterService rs = (RegisterService) ServiceFactory.getService(new RegisterServiceImpl());
        PaginationVO<StudyInfo> vo = rs.pageList(map);
        PrintJson.printJsonObj(response, vo);

    }

    private void add(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        System.out.println("进入到跳转到登记添加页的操作");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserListByprivileges("1");
        request.setAttribute("uList", uList);
        request.getRequestDispatcher("/workbench/register/save.jsp").forward(request, response);
    }
}
