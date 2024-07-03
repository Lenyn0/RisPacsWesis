package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.domain.Device;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.DiseaseDictionary;
import com.bjpowernode.crm.settings.service.DeviceService;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.FaceEngineService;
import com.bjpowernode.crm.settings.service.impl.DeviceServiceImpl;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.settings.service.impl.FaceEngineServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.web.dao.WorkListDao;
import com.bjpowernode.crm.web.domain.WorkList;
import com.bjpowernode.crm.workbench.dao.StudyInfoDao;
import com.bjpowernode.crm.workbench.domain.StudyInfo;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.logging.Logger;

import static com.bjpowernode.crm.web.dcmqrscp.DcmQRSCP.worlistService;
import static com.bjpowernode.crm.web.hl7rcv.HL7Rcv.hl7Service;

public class SysInitListener implements ServletContextListener {
    /*
        该方法是用来监听上下文域对象的方法，当服务器启动，上下文域对象创建
        对象创建完毕后，马上执行该方法
        event：该参数能够取得监听的对象
                监听的是什么对象，就可以通过该参数能取得什么对象
                例如我们现在监听的是上下文域对象，通过该参数就可以取得上下文域对象
     */
//    private WorkListDao workListDao = SqlSessionUtil.getSqlSession().getMapper(WorkListDao.class);
    public void contextInitialized(ServletContextEvent event) {
        //System.out.println("上下文域对象创建了");
        System.out.println("服务器缓存处理数据字典开始");
        ServletContext application = event.getServletContext();
        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
        /*
            应该管业务层要
            7个list
            可以打包成为一个map
            业务层应该是这样来保存数据的：
                map.put("appellationList",dvList1);
                map.put("clueStateList",dvList2);
                map.put("stageList",dvList3);
                ....
                ...
         */
        Map<String, List<DicValue>> map = ds.getAll();
        //将map解析为上下文域对象中保存的键值对
        Set<String> set = map.keySet();
        for (String key : set) {
            application.setAttribute(key, map.get(key));
        }

        DicService ds1 = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DiseaseDictionary>> dmap = ds1.getDiseaseAll();
        //将dmap解析为上下文域对象中保存的键值对
        Set<String> set1 = dmap.keySet();
        for (String key : set1) {
            /*for(DiseaseDictionary dd:dmap.get(key)){
                System.out.println(key+"  "+dd.getName());
            }*/
            application.setAttribute(key, dmap.get(key));
        }
        System.out.println("服务器缓存处理数据字典结束");


        System.out.println("服务器缓存处理设备列表开始");
        DeviceService ds2 = (DeviceService) ServiceFactory.getService(new DeviceServiceImpl());
        List<Device> deviceList = ds2.getAll();
        //dMap.put("deviceList",deviceList);
        application.setAttribute("deviceList", deviceList);
        System.out.println("服务器缓存处理设备列表结束");


        /*
        开启dcmqrscp——>worklist服务
         */

//        String[] args2 = {"-b", "DCMQRSCP:11112", "--dicomdir", "C:\\Users\\admin\\Desktop\\dcm4che-5.21.0-bin\\dcm4che-5.21.0\\bin\\disk99\\DICOMDIR2"};

        String[] args2 = {"-b", "DCMQRSCP:"};
        ResourceBundle res = ResourceBundle.getBundle("ris");
        String worklistPort = res.getString("worklistPort");
        args2[1]+=worklistPort;
        System.out.println("-------------worklist服务参数："+args2[1]);
        boolean flag = false;
        try {
            flag = isPortUsing("127.0.0.1", Integer.valueOf(worklistPort));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if(flag){//worklist端口被使用
            System.out.println("-------------worklist端口被使用,开启worklist服务失败--------------");
        }else{//worklist端口未被使用
            System.out.println("准备开启worklist服务");
            worlistService(args2);
            System.out.println("开启worklist服务成功");
        }

/*
        开启hl7rcv——>接收hl7消息服务
         */
        System.out.println("准备开启hl7Service服务");
        hl7Service();
        System.out.println("开启hl7Service服务成功");
//        Map<String, Object> myMap=new HashMap<>();
//        myMap.put("patientID","06e3cbdf10a44eca8511dddfc6896789");
//        List<WorkList> workList = workListDao.getWorkList(myMap);
//        System.out.println(workList);



        //------------------------------------------------------------------------
        //数据字典处理完毕后，处理Stage2Possibility.properties文件
        /*
            处理Stage2Possibility.properties文件步骤：
                解析该文件，将该属性文件中的键值对关系处理成为java中键值对关系（map）
                Map<String(阶段stage),String(可能性possibility)> pMap = ....
                pMap.put("01资质审查",10);
                pMap.put("02需求分析",25);
                pMap.put("07...",...);
                pMap保存值之后，放在服务器缓存中
                application.setAttribute("pMap",pMap);
         */
        //解析properties文件
        Map<String, String> pMap = new HashMap<String, String>();
        //ResourceBundle是解析properties文件的类
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e = rb.getKeys();
        while (e.hasMoreElements()) {
            //阶段
            String key = e.nextElement();
            //可能性
            String value = rb.getString(key);
            pMap.put(key, value);
        }
        //将pMap保存到服务器缓存中
        application.setAttribute("pMap", pMap);

        //初始化FaceEngine
        /*FaceEngineService fs = (FaceEngineService) ServiceFactory.getService(new FaceEngineServiceImpl());
        fs.init();*/
    }
    /***
     * 测试主机Host的port端口是否被使用
     * @param host
     * @param port
     * @throws UnknownHostException
     */
    public static boolean isPortUsing(String host,int port) throws UnknownHostException {
        boolean flag = false;
        InetAddress Address = InetAddress.getByName(host);
        try {
            Socket socket = new Socket(Address,port);  //建立一个Socket连接
            //如果建立连接成功就往下执行，如果建立连接失败就异常
            System.out.println("建立socket成功，11113端口被使用");
            socket.close();
            flag = true;
        } catch (IOException e) {
            System.out.println("建立socket失败，11113端口未被使用");
        }
        return flag;
    }
}
