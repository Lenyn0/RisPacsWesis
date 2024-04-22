package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.StudyInfoDao;
import com.bjpowernode.crm.workbench.domain.StudyInfo;
import com.bjpowernode.crm.workbench.service.CheckService;

import java.util.List;
import java.util.Map;

public class CheckServiceImpl implements CheckService {

    private StudyInfoDao studyInfoDao = SqlSessionUtil.getSqlSession().getMapper(StudyInfoDao.class);

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
