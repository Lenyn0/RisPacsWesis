package com.by.ris_springboot.workbench.service.impl;



import com.by.ris_springboot.vo.PaginationVO;
import com.by.ris_springboot.workbench.dao.PatientDao;
import com.by.ris_springboot.workbench.dao.StudyInfoDao;
import com.by.ris_springboot.workbench.domain.Patient;
import com.by.ris_springboot.workbench.domain.StudyInfo;
import com.by.ris_springboot.workbench.service.SigninService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SigninServiceImpl implements SigninService {
    @Autowired
    private StudyInfoDao studyinfoDao;
    @Autowired
    private PatientDao patientDao;
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
