package com.by.ris_springboot.workbench.service.impl;

import com.by.ris_springboot.settings.dao.DiseaseDictionaryDao;
import com.by.ris_springboot.settings.domain.DiseaseDictionary;
import com.by.ris_springboot.vo.PaginationVO;
import com.by.ris_springboot.workbench.dao.PatientDao;
import com.by.ris_springboot.workbench.dao.ReportDao;
import com.by.ris_springboot.workbench.domain.Patient;
import com.by.ris_springboot.workbench.domain.Report;
import com.by.ris_springboot.workbench.domain.StudyInfo;
import com.by.ris_springboot.workbench.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.by.ris_springboot.workbench.dao.StudyInfoDao;

import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private  StudyInfoDao studyInfoDao;
    @Autowired
    private ReportDao reportDao;
    @Autowired
    private DiseaseDictionaryDao diseaseDictionaryDao;
    @Autowired
    private PatientDao patientDao;

    public int save(Report r) {
        int result = reportDao.save(r);
        System.out.println("save:"+result);
        return result;
    }
    public int update(Report r) {
        int result = reportDao.update(r);
        System.out.println("update result:"+result);
        return result;
    }

    public PaginationVO<Report> pageList_Report(Map<String, Object> map) {
        //取得total,可能会有条件
        int total = reportDao.getTotalByCondition(map);
        System.out.println("total:"+total);

        //取得dataList，可能会有条件
        List<Report> dataList = reportDao.getReportListByCondition(map);
        //dataList.set("等待填写",);
        System.out.println("dataList结果:");
        for(int i=0;i< dataList.size();i++)
            System.out.println(dataList.get(i));
        //创建一个vo对象，将total和dataList封装到vo中
        PaginationVO<Report> vo = new PaginationVO<Report>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        System.out.println("vo值：");
        System.out.println(vo.toString());
        //将vo返回
        return vo;
    }

    public int updateReportStatus(String id, String reportStatus) {
        int result = reportDao.updateReportStatus(id, reportStatus);
        System.out.println("updateReportStatus result:"+result);
        return result;
    }

    public int updateAuditorID(String id, String auditorID) {
        int result = reportDao.updateAuditorID(id, auditorID);
        return result;
    }

    public PaginationVO<StudyInfo> pageList_StudyInfo(Map<String, Object> map) {

        //取得total,可能会有条件
        //int total = studyInfoDao.getTotalByCondition6OR4(map);
        //System.out.println("total:"+total);
        int total = 0;
        //取得dataList，可能会有条件
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        int skipCount = Integer.parseInt(map.get("skipCount").toString());
        System.out.println("pagesize:"+pageSize+" skipcount:"+skipCount);
        /*map.remove("pageSize");
        map.remove("skipCount");*/

        /*
        返回两类数据：
        1 检查完成但是没有写报告的
        2 报告状态处于「等待修改」状态的
         */

        //查询所有的「待写报告」状态的检查信息StudyInfo，包括「未写报告」和「需要修改报告」
        List<StudyInfo> dataList = studyInfoDao.getReportListByCondition(map);
        //查询报告表中处于「需修改」状态对应的「检查号」
        List<String> studyID = reportDao.getReportListByCondition1();
        //查询报告表中处于「待审核」「审核通过」「锁定」状态的报告
        List<String> studyID_remove = reportDao.getReportListByCondition2();


        /*
        下面这一大段代码是为了修改状态？
        把需修改状态的检查，修改检查状态
         */
        for(int i=0;i<dataList.size();i++){
            StudyInfo s = dataList.get(i);
            //查询到所有的报告
            for(int j=0;j<studyID.size();j++){
                if(s.getAccessionNumber().equals(studyID.get(j))){
                    s.setStatus("8");//为什么设置检查状态是8呢？
                    studyID.remove(j);
                    j -= 1;
                    dataList.set(i,s);
                    /*total -= 1;*/
                    break;
                }
            }
        }

        /*
        把「待审核」「审核通过」「锁定」状态的报告对应的检查去除
         */
        for(int i=0;i<dataList.size();i++){
            StudyInfo s = dataList.get(i);
            for(int j=0;j<studyID_remove.size();j++){
                if(s.getAccessionNumber().equals(studyID_remove.get(j))){
                    //s.setStatus("9");
                    studyID_remove.remove(j);
                    j -= 1;
                    dataList.remove(i);
                    i -= 1;
                    //total -= 1;
                    break;
                }
            }
        }
        total = dataList.size();
        System.out.println("total:"+total);
        if(total<=skipCount+pageSize-1){
            dataList = dataList.subList(skipCount,total);
        }
        if(total>skipCount+pageSize-1){
            dataList = dataList.subList(skipCount,skipCount+pageSize);
        }
        //dataList.set("等待填写",);
        System.out.println("dataList结果:");
        for(int i=0;i< dataList.size();i++)
            System.out.println(dataList.get(i));
        //创建一个vo对象，将total和dataList封装到vo中
        PaginationVO<StudyInfo> vo = new PaginationVO<StudyInfo>();
        vo.setTotal(total);
        System.out.println("total: "+total);
        vo.setDataList(dataList);
        System.out.println("vo值：");
        System.out.println(vo.toString());
        //将vo返回
        return vo;
    }

    @Override
    public Report getByIdOrStudyID(String id) {
        return reportDao.getByIdOrStudyID(id);
    }

    public List<DiseaseDictionary> getListBybodyPart(String bodyparts) {

        List<DiseaseDictionary> dlist = diseaseDictionaryDao.getListBybodyPart(bodyparts);
        return dlist;
    }

    public Patient getPatientByID(String id) {
        Patient p = patientDao.getPatientByID(id);
        System.out.println(p);
        return p;
    }

    public StudyInfo getStudyInfoByAccessionNumber(String accessionNumber){
        StudyInfo s = studyInfoDao.getStudyInfoByAccessionNumber(accessionNumber);
        System.out.println(s);
        return s;
    }

    public Boolean updateStudyInfoByArtificer(StudyInfo st) {
        Boolean flag = studyInfoDao.updateStudyInfoByArtificer(st);
        return flag;
    }

    public boolean updateStudyInfoStatus(String accessionNumber, String status) {
        boolean result = studyInfoDao.updateStudyInfoStatus(accessionNumber,status);
        System.out.println("result:"+result);
        return result;
    }

    public boolean updatetechnicianID(String accessionNumber, String technicianID) {
        boolean result = studyInfoDao.updatetechnicianID(accessionNumber,technicianID);
        System.out.println("result:"+result);
        return result;
    }

    public String getReportStatusById(String id){
        String reportStatus = reportDao.getReportStatusById(id);
        System.out.println("reportStatus:"+reportStatus);
        return reportStatus;
    }
}
