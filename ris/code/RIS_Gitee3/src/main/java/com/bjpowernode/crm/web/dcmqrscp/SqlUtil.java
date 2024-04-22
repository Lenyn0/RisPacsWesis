package com.bjpowernode.crm.web.dcmqrscp;

import com.bjpowernode.crm.web.domain.WorkList;

import java.sql.*;
import java.util.HashMap;
import java.util.List;

public class SqlUtil {
    static List<WorkList> workListCache;
//    static WorkList workList = new WorkList();
    static Connection conn = null;
    static Statement stmt = null;
    static ResultSet rs = null;

    static {

        try {
            // 1 注册驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 2 获取连接
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ris", "root", "13445923");
            // 3 获取数据库操作对象
            stmt = conn.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     *
     * @param table 查询的表
     * @param field 查询的字段
     * @param idField id字段名
     * @param idValue id字段值
     * @return 查询的字段的值
     */
    static public String select(String table,String field,String idField,String idValue){
        try {
            // 4 执行sql
            String sql = "select * from "+table+" where "+idField+" = '"+idValue+"'";
            rs = stmt.executeQuery(sql);
            // 5 处理结果集
            if (rs.next()) {
//                System.out.println(rs.getObject(field));
                return (String) rs.getObject(field);
            }else{
                return "";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }
//    public static void main(String[] args) {
//
//        try {
//
//            // 4 执行sql
//            String sql = "select * from tbl_study_info where patientID = '3111dde3282046a29e150bd1adc8497b'";
//            rs = stmt.executeQuery(sql);
//            // 5 处理结果集
//            if (rs.next()) {
//                System.out.println(rs.getObject("accessionNumber"));
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            // 6 释放资源
//            if (rs != null) {
//                try {
//                    rs.close();
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                }
//            }
//            if (stmt != null) {
//                try {
//                    stmt.close();
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                }
//            }
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                }
//            }
//
//        }
//    }
}
