package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.workbench.service.ChartService;
import com.bjpowernode.crm.workbench.service.impl.ChartServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class ChartController extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到统计图表控制器");
        String path = request.getServletPath();

        if ("/workbench/chart/equipment_workload/getCharts.do".equals(path)) {
            //5 对应的是设备
            getWorkload(request, response,5);
        }
        /*else if ("/workbench/chart/annual_positive_rate/getCharts.do".equals(path)) {
            getCharts(request, response,2);
        } */
        else if ("/workbench/chart/annual_positive_rate.do".equals(path)) {
            //技师
            getWorkload(request, response,6);
        }
        else if ("/workbench/chart/technician_workload/getCharts.do".equals(path)) {
            //技师
            getWorkload(request, response,2);
        } else if ("/workbench/chart/diagnostician_workload/getChartsByYear.do".equals(path)) {
            getCharts(request, response,4);
        }else if ("/workbench/chart/diagnostician1.do".equals(path)){
            getWorkload(request, response,1);
        }else if ("/workbench/chart/auditor.do".equals(path)){
            //审核医生是3
            getWorkload(request, response,3);
        }else if ("/workbench/chart/clinicianID_workload/getCharts.do".equals(path)){
            //登记员：4
            getWorkload(request, response,4);
        }
    }

    public void getWorkload(HttpServletRequest request, HttpServletResponse response, int i){
        String flag = request.getParameter("flag");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        System.out.println("flag: "+flag);
        //设置日期的格式
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //指定日期
        if(flag.equals("1")){
            startDate+=" 00:00:00";
            endDate+=" 23:59:59";
            System.out.println("开始时间"+startDate+"结束时间"+endDate);
        }
        else{
            Date date = new Date();
            endDate = formatter.format(date);
            //获取当前日期
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());

            if(flag.equals("2")) c.add(Calendar.DATE, -7); //前一周
            else if(flag.equals("3")) c.add(Calendar.MONTH, -1); //前一月
            else if(flag.equals("4")) c.add(Calendar.MONTH, -3); //前一季度
            else if(flag.equals("5")) c.add(Calendar.YEAR, -1); //前一年
            else if(flag.equals("6")) c.add(Calendar.MONTH, -6); //前半年

            Date d = c.getTime();
            startDate = formatter.format(d);
            System.out.println("截止时间："+endDate);
        }
        ChartService rs = (ChartService) ServiceFactory.getService(new ChartServiceImpl());
        Map<String,Object> wl = rs.getWorkload(startDate,endDate,i);
        System.out.println("工作量："+wl);
        PrintJson.printJsonObj(response,wl);
    }


    private void getCharts(HttpServletRequest request, HttpServletResponse response, int i) {
        ChartService rs = (ChartService) ServiceFactory.getService(new ChartServiceImpl());

        Map<String,Object> map = rs.getCharts(i);

        PrintJson.printJsonObj(response,map);
    }

    /*private void getPositiveCharts(HttpServletRequest request, HttpServletResponse response) {
        ChartService rs = (ChartService) ServiceFactory.getService(new ChartServiceImpl());

        Map<String,Object> map = rs.getPositiveCharts();

        PrintJson.printJsonObj(response,map);
    }*/

    /*private void getDeviceCharts(HttpServletRequest request, HttpServletResponse response) {

        ChartService rs = (ChartService) ServiceFactory.getService(new ChartServiceImpl());

        Map<String,Object> map = rs.getCharts(i);

        PrintJson.printJsonObj(response,map);

    }*/

}

