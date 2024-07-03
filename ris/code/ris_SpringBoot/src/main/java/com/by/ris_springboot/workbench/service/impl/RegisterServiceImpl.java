package com.by.ris_springboot.workbench.service.impl;


import com.by.ris_springboot.utils.SqlSessionUtil;
import com.by.ris_springboot.vo.PaginationVO;
import com.by.ris_springboot.workbench.dao.PatientDao;
import com.by.ris_springboot.workbench.dao.StudyInfoDao;
import com.by.ris_springboot.workbench.domain.Patient;
import com.by.ris_springboot.workbench.domain.StudyInfo;
import com.by.ris_springboot.workbench.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private StudyInfoDao studyinfoDao;
    @Autowired
    private PatientDao patientDao;

    public PaginationVO<StudyInfo> pageList(Map<String, Object> map){

        //取得total
        int total = studyinfoDao.getTotalByCondition(map);

        //取得dataList
        List<StudyInfo> dataList = studyinfoDao.getStudyInfoDaoListByCondition(map);

        //创建一个vo对象，将total和dataList封装到vo中
        PaginationVO<StudyInfo> vo = new PaginationVO<StudyInfo>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        //将vo返回
        return vo;
    }

    public boolean save(Patient p, StudyInfo s) {
        boolean flag = true;
        Patient patient = patientDao.getPatientByName(p.getName());
        if(patient==null){
            //没有改病人则新建一个
            s.setPatientID(p.getId());
            int count1 = patientDao.save(p);
            if(count1!=1){
                flag=false;
            }
            int count2 = studyinfoDao.save(s);
            if(count2!=1){
                flag=false;
            }
        }else{
            //有则修改原有的病人信息
            p.setId(patient.getId());//修改则需保留原来的id
            s.setPatientID(patient.getId());//修改则需保留原来的id
            int count1 = patientDao.update(p);
            if(count1!=1){
                flag=false;
            }
            int count2 = studyinfoDao.save(s);
            if(count2!=1){
                flag=false;
            }
        }
        return flag;
/*        boolean flagPatient = true;
        boolean flagStudyinfo = true;
        if(patientDao.save(p)!=1){
            flagPatient=false;
        }
        if( studyinfoDao.save(s)!=1){
            flagStudyinfo=false;
        }
        if (flagPatient&&flagStudyinfo){
            return true;
        }
        else{
            return false;
        }*/
    }

    public boolean delete(String[] accessionNumbers) {
        boolean flag = true;
        //删除登记信息
        int count = studyinfoDao.delete(accessionNumbers);
        if(count!=accessionNumbers.length){
            flag = false;
        }
        return flag;
    }

    public List<String> getPatientNameList(String name) {
        List<String> nameList = patientDao.getPatientNameList(name);
        return nameList;
    }

    public Patient getPatientByName(String name) {
        Patient p = patientDao.getPatientByName(name);
        return p;
    }

    public Patient getPatientByaccessionNumber(String accessionNumber) {
        Patient p = patientDao.getPatientByaccessionNumber(accessionNumber);
        return p;
    }

    public StudyInfo getStudyInfoByAccessionNumber(String accessionNumber) {
        StudyInfo studyinfo = studyinfoDao.getStudyInfoByAccessionNumber(accessionNumber);
        return studyinfo;
    }

    public boolean update(Patient p, StudyInfo s) {
        boolean flag = true;
        Patient patient = patientDao.getPatientByName(p.getName());
        p.setId(patient.getId());
        int count1 = patientDao.update(p);
        if(count1!=1){
            flag=false;
        }
        if("4".equals(s.getStatus())){
            //如果是需修改的在修改完成后将直接变成待检查状态
            s.setStatus("3");
        }else if("0".equals(s.getStatus())){
            //如果是HIS接收到的检查是0状态/待登记状态，修改为待预约状态
            s.setStatus("1");
        }
        int count2 = studyinfoDao.update(s);
        if(count2!=1){
            flag=false;
        }
        return flag;
    }

    public boolean appointment(StudyInfo s) {
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

}
