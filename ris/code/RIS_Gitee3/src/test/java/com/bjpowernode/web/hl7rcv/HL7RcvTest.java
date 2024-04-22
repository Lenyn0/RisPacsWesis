package com.bjpowernode.web.hl7rcv;
//
//import lombok.SneakyThrows;
//import org.junit.Test;
//
//import java.net.URLEncoder;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//
//import static com.bjpowernode.crm.web.hl7rcv.HL7Rcv.hl7Service;
//
public class HL7RcvTest {
//    @Test
//    public void test1() {
//        hl7Service();
//        System.out.println("开启hl7rcv服务成功");
//        int i = 0;
//        while (i < 1000) {
//            i++;
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @SneakyThrows
//    @Test
//    public void test2() {
//        String s="abc";
//        String s1= URLEncoder.encode(s, "utf-8");
//
//        String t = "这是一个字符串aaa111";
//        String utf8 = new String(t.getBytes( "UTF-8"));
//        System.out.println(utf8);
//        String unicode = new String(t.getBytes(),"UTF-8");
//        System.out.println(unicode);
//        String gbk = new String(unicode.getBytes("GBK"));
//        System.out.println(gbk);
//    }
//
//    /**
//     * 把字符串转化成需要的时间格式
//     * 并两个日期时间的差值
//     */
//    @Test
//    public void test3() {
//        //当前日期
////        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        Date date1 = new Date(System.currentTimeMillis());//获得当前时间，Date类型
////        System.out.println(formatter.format(date1));
//
//        //指定日期
//        String dateString = "20210719";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        Date date2 = null;
//        try {
//            date2 = sdf.parse(dateString);//返回date格式
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        int year = 0;
//        int month = 0 ;
//        int day = 0 ;
//        if (date2 != null) {
//            year = (int) ((date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24)) / 365; // 计算年
//            month = (int) ((date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24)) / 30; // 计算月
//            day = (int) ((date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24)); // 计算天
//        }
//        System.out.println("相差" + year + "年");
//        System.out.println("相差" + month + "月");
//        System.out.println("相差" + day + "天");
//
////        Calendar cal = Calendar.getInstance();
////        long time1 = 0;
////        long time2 = 0;
////
////        try{
////            cal.setTime(sdf.parse(smdate));
////            time1 = cal.getTimeInMillis();
////            cal.setTime(sdf.parse(bdate));
////            time2 = cal.getTimeInMillis();
////        }catch(Exception e){
////            e.printStackTrace();
////        }
////        long between_days=(time2-time1)/(1000*3600*24);
////        int days = Integer.parseInt(String.valueOf(between_days));
////        System.out.println();;
//    }
}
