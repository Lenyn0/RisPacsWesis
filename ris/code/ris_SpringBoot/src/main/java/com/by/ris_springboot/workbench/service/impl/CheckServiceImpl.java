package com.by.ris_springboot.workbench.service.impl;


import com.by.ris_springboot.vo.PaginationVO;
import com.by.ris_springboot.workbench.dao.StudyInfoDao;
import com.by.ris_springboot.workbench.domain.StudyInfo;
import com.by.ris_springboot.workbench.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CheckServiceImpl implements CheckService {

    @Autowired
    private StudyInfoDao studyInfoDao;

    public PaginationVO<StudyInfo> pageList_Check(Map<String, Object> map) {
        //取得total,可能会有条件
        int total = studyInfoDao.getTotalByCondition3(map);
        System.out.println("total:"+total);
        //取得dataList，可能会有条件
        List<StudyInfo> dataList = studyInfoDao.getCheckListByCondition(map);
        //dataList.set("等待填写",);
        System.out.println("dataList结果:");
        for(int i=0;i< dataList.size();i++)
            System.out.println(dataList.get(i));

        //创建一个vo对象，将total和dataList封装到vo中
        PaginationVO<StudyInfo> vo = new PaginationVO<StudyInfo>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        return vo;
    }
}
