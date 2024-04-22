package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.workbench.dao.ReportDao;
import com.bjpowernode.crm.workbench.dao.StudyInfoDao;
import com.bjpowernode.crm.workbench.service.ChartService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartServiceImpl implements ChartService {
    private StudyInfoDao studyInfoDao = SqlSessionUtil.getSqlSession().getMapper(StudyInfoDao.class);
    private ReportDao reportDao = SqlSessionUtil.getSqlSession().getMapper(ReportDao.class);

    public Map<String, Object> getCharts(int i) {
        //取得dataList
        List<Map<String,Object>> map =  new ArrayList<Map<String, Object>>();
        if(i==1){
            //设备
            /*List<Map<String,Object>>*/ map = studyInfoDao.getCharts();
        }else if(i==2){
            //年度
            /*List<Map<String,Object>>*/ map = studyInfoDao.getPositiveCharts();
        }else if(i==3){
            //技师
            /*List<Map<String,Object>>*/ map = studyInfoDao.getTechnicianCharts();
        }else if(i==4){
            //诊断医生 对应的是study_info表中的诊断医生那一个列diagnostician
            map = studyInfoDao.getDiagnosticianChartsByYear();
        }
        //Map<String,Object> map = studyInfoDao.getPositiveCharts();
        List<String> name = new ArrayList<String>(),count=new ArrayList<String>();
        for(Map m:map){
            name.add(m.get("name").toString());
            count.add(m.get("value").toString());
        }
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("name",name);
        result.put("value",count);
        return result;
    }

    public Map<String, Object> getWorkload(String startDate, String endDate, int i){
        //取得dataList
        List<Map<String,Object>> map =  new ArrayList<Map<String, Object>>();
        if(i==1){
            map = reportDao.getWorkload(startDate,endDate);
        }
        if(i==2){
            //技师对应的是2
            startDate = startDate.substring(0,10);
            endDate = endDate.substring(0,10);
            map = studyInfoDao.getWorkload_technician(startDate,endDate);
        }
        if(i==3){
            map = reportDao.getWorkload_auditor(startDate,endDate);
        }
        if(i==4){
            startDate = startDate.substring(0,10);
            endDate = endDate.substring(0,10);
            map = studyInfoDao.getWorkload_clinicianID(startDate,endDate);
        }
        if(i==5){
            startDate = startDate.substring(0,10);
            endDate = endDate.substring(0,10);
            map = studyInfoDao.getWorkload_Device(startDate,endDate);
        }
        if(i==6){
            //对应的是年度阳性统计率
            startDate = startDate.substring(0,10);
            endDate = endDate.substring(0,10);
            map = studyInfoDao.getWorkload_positive(startDate,endDate);
        }
        //Map<String,Object> map = studyInfoDao.getPositiveCharts();
        List<String> name = new ArrayList<String>(),count=new ArrayList<String>();
        for(Map m:map){
            name.add(m.get("name").toString());
            count.add(m.get("value").toString());
        }
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("name",name);
        result.put("value",count);
        return result;
        /*return reportDao.getWorkload(startDate,endDate);*/
    }

    public Map<String, Object> getTechnicianCharts() {
        //取得dataList
        List<Map<String,Object>> map = studyInfoDao.getTechnicianCharts();
        //Map<String,Object> map = studyInfoDao.getPositiveCharts();
        List<String> name = new ArrayList<String>(),count=new ArrayList<String>();
        for(Map m:map){
            name.add(m.get("name").toString());
            count.add(m.get("value").toString());
        }
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("name",name);
        result.put("value",count);
        return result;
    }

    public Map<String, Object> getPositiveCharts() {

        //取得dataList
        List<Map<String,Object>> map = studyInfoDao.getPositiveCharts();
        List<String> name = new ArrayList<String>(),count=new ArrayList<String>();
        for(Map m:map){
            name.add(m.get("name").toString());
            count.add(m.get("value").toString());
        }
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("name",name);
        result.put("value",count);
        return result;
    }
}
