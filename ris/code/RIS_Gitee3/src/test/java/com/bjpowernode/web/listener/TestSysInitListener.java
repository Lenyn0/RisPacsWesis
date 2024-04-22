//package com.bjpowernode.web.listener;
//
//import com.bjpowernode.crm.utils.ServiceFactory;
//import com.bjpowernode.crm.utils.SqlSessionUtil;
//import com.bjpowernode.crm.utils.UUIDUtil;
//import com.bjpowernode.crm.web.dao.WorkListDao;
//import com.bjpowernode.crm.web.domain.WorkList;
//import com.bjpowernode.crm.workbench.domain.Activity;
//import com.bjpowernode.crm.workbench.service.ActivityService;
//import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
//import org.junit.Assert;
//import org.junit.Test;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class TestSysInitListener {
//    private WorkListDao workListDao = SqlSessionUtil.getSqlSession().getMapper(WorkListDao.class);
//    @Test
//    public void test1(){
//
//        System.out.println("准备开启worklist服务");
//        Map<String, Object> myMap=new HashMap<>();
//        myMap.put("patientID","3111dde3282046a29e150bd1adc8497b");
//        List<WorkList> workList = workListDao.getWorkList(myMap);
//        System.out.println("查询到个数："+workList.size());
//        System.out.println(workList);
//
//        System.out.println("开启worklist服务成功");
//
//    }
//
//}
