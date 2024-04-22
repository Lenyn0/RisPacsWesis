package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Patient;
import com.bjpowernode.crm.workbench.domain.StudyInfo;
import com.bjpowernode.crm.workbench.service.CheckService;
import com.bjpowernode.crm.workbench.service.impl.CheckServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.RegisterServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.ReportServiceImpl;
import com.bjpowernode.crm.workbench.service.RegisterService;
import com.bjpowernode.crm.workbench.service.ReportService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CheckController extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到报告控制器");
        String path = request.getServletPath();

        if ("/workbench/Check/pageList_Check.do".equals(path)) {
            pageList_Check(request, response);
        } else if ("/workbench/check/detail.do".equals(path)) {
            detail(request, response);
        } else if ("/workbench/Check/Submit.do".equals(path)) {
            Submit(request, response, "5");
        } else if ("/workbench/Check/Reject.do".equals(path)) {
            Submit(request, response, "4");
        } else if ("/workbench/Check/Finish.do".equals(path)) {
            Submit(request, response, "6");
        }
    }

    private void Submit(HttpServletRequest request, HttpServletResponse response, String s) {

        System.out.println("进入啦啦啦啦啦啦啦啦啦啦");
        //修改study_info表中的状态 需要从前端传修改后的状态
        String accessionNumber = request.getParameter("accessionNumber");
        String technicianID = request.getParameter("technicianID");
        System.out.println(technicianID);
        Boolean flag1 = Boolean.valueOf(request.getParameter("flag"));
        if (flag1) {
            //<%--id="projection"使用耗材useConsumables,scheduledProcedure-StepDescription--%>
            String projection = request.getParameter("projection");
            String useConsumables = request.getParameter("useConsumables");
            String scheduledProcedureStepDescription = request.getParameter("scheduledProcedure-StepDescription");
            ReportService rs0 = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
            StudyInfo st = new StudyInfo();
            st.setAccessionNumber(accessionNumber);
            st.setProjection(projection);
            st.setUseConsumables(useConsumables);
            st.setScheduledProcedureStepDescription(scheduledProcedureStepDescription);
            Boolean flag3 = rs0.updateStudyInfoByArtificer(st);
        }
        System.out.println("accessionNumber:" + accessionNumber + " status:" + s);
        ReportService rs1 = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
        boolean flag2 = rs1.updateStudyInfoStatus(accessionNumber, s);
        ReportService rs2 = (ReportService) ServiceFactory.getService(new ReportServiceImpl());
        boolean flag3 = rs2.updatetechnicianID(accessionNumber, technicianID);
        boolean flag4 = false;
        if (flag2 && flag3) flag4 = true;
        PrintJson.printJsonFlag(response, flag4);

    }

    private void pageList_Check(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到待检查信息列表的操作（结合条件查询+分页查询）");
        //accessionNumber
        //department" : $
        //patientName" :
        //emergency" : $.
        String accessionNumber = request.getParameter("accessionNumber");
        String department = request.getParameter("department");
        String patientName = request.getParameter("patientName");
        String emergency = request.getParameter("emergency");

        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount = (pageNo - 1) * pageSize;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("accessionNumber", accessionNumber);
        map.put("department", department);
        map.put("patientName", patientName);
        map.put("emergency", emergency);

        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize);
        CheckService checkService = (CheckService) ServiceFactory.getService(new CheckServiceImpl());
        PaginationVO<StudyInfo> paginationVO = checkService.pageList_Check(map);
        System.out.println("输出成功");
        System.out.println(paginationVO.toString());
        PrintJson.printJsonObj(response, paginationVO);
    }

    /**
     * xhy的函数
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到跳转到登记修改页的操作");
        String accessionNumber = request.getParameter("accessionNumber");
        //动态代理产生的对象在使用一次后会结束生命周期
        RegisterService rs = (RegisterService) ServiceFactory.getService(new RegisterServiceImpl());
        Patient patient = rs.getPatientByaccessionNumber(accessionNumber);
        RegisterService rs1 = (RegisterService) ServiceFactory.getService(new RegisterServiceImpl());
        StudyInfo studyinfo = rs1.getStudyInfoByAccessionNumber(accessionNumber);
        System.out.println(patient);
        System.out.println(studyinfo);
        request.setAttribute("patient", patient);
        request.setAttribute("studyinfo", studyinfo);
        //window.location.href = "workbench/check/detail.jsp";
        request.getRequestDispatcher("/workbench/check/detail.jsp").forward(request, response);
    }

}

