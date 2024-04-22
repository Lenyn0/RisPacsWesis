//package com.bjpowernode.web.dcmqrscp;
//
//import com.bjpowernode.crm.utils.SqlSessionUtil;
//import com.bjpowernode.crm.web.dao.WorkListDao;
//import com.bjpowernode.crm.web.domain.WorkList;
//import org.junit.Test;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static com.bjpowernode.crm.web.dcmqrscp.DcmQRSCP.worlistService;
//
//public class TestDcmQRSCP {
//    private WorkListDao workListDao = SqlSessionUtil.getSqlSession().getMapper(WorkListDao.class);
//    @Test
//    public void test1(){
////        String[] args2={"-b","DCMQRSCP:11112","--dicomdir","C:\\Users\\admin\\Desktop\\dcm4che-5.21.0-bin\\dcm4che-5.21.0\\bin\\disk99\\DICOMDIR2"};
//        String[] args2 = {"-b", "DCMQRSCP:11112"};
//        worlistService(args2);
////        System.out.println("准备开启worklist服务");
////        Map<String, Object> myMap=new HashMap<>();
////        myMap.put("patientID","3111dde3282046a29e150bd1adc8497b");
////        List<WorkList> workList = workListDao.getWorkList(myMap);
////        System.out.println(workList);
////
//        System.out.println("开启worklist服务成功");
//        int i=0;
//        while (i<1000){
//            i++;
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
