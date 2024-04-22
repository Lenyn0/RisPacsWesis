package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.PatientDao;
import com.bjpowernode.crm.workbench.dao.StudyInfoDao;
import com.bjpowernode.crm.workbench.domain.Patient;
import com.bjpowernode.crm.workbench.domain.StudyInfo;
import com.bjpowernode.crm.workbench.service.SigninService;

import java.util.List;
import java.util.Map;

public class SigninServiceImpl implements SigninService {
    private StudyInfoDao studyinfoDao = SqlSessionUtil.getSqlSession().getMapper(StudyInfoDao.class);
    private PatientDao patientDao = SqlSessionUtil.getSqlSession().getMapper(PatientDao.class);
    public PaginationVO<StudyInfo> pageList(Map<String, Object> map) {
        //取得total
        int total = studyinfoDao.getTotalByCondition(map);
        //取得dataList
        List<StudyInfo> dataList = studyinfoDao.getStudyInfoListByCondition(map);
        //创建一个vo对象，将total和dataList封装到vo中
        PaginationVO<StudyInfo> vo = new PaginationVO<StudyInfo>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        //将vo返回
        return vo;
    }

    public Patient getPatientByaccessionNumber(String accessionNumber) {
        Patient patient = patientDao.getPatientByaccessionNumber(accessionNumber);
        return patient;
    }

    public StudyInfo getStudyInfoByAccessionNumber(String accessionNumber) {
        StudyInfo studyinfo = studyinfoDao.getStudyInfoByAccessionNumber(accessionNumber);
        return studyinfo;
    }

    public boolean updatescheduledProcedureStepStart(StudyInfo s) {
        boolean flag = true;
        if("".equals(s.getScheduledProcedureStepStartDate())&&"".equals(s.getScheduledProcedureStepStartTime())){
            //没有填写预约时间
            s.setStatus("1");
        }else{
            //填写了预约时间
            s.setStatus("2");
        }
        int count = studyinfoDao.updatescheduledProcedureStepStart(s);
        if(count!=1){
            flag=false;
        }
        return flag;
    }

    public boolean cancel(StudyInfo s) {
        boolean flag = true;
        int count = studyinfoDao.cancel(s);
        if(count!=1){
            flag=false;
        }
        return flag;
    }

    public boolean signin(StudyInfo s) {
        boolean flag = true;
        int count = studyinfoDao.signin(s);
        if(count!=1){
            flag=false;
        }
        return flag;
    }
}
