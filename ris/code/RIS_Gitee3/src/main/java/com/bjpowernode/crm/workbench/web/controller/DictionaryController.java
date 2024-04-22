package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.DiseaseDictionary;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictionaryController extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到统计图表控制器");
        String path = request.getServletPath();

        if ("/workbench/Disease/pageList.do".equals(path)) {
            pageList_Disease(request, response,1);
        }else if ("/workbench/DiseaseDictionary/save.do".equals(path)) {
            saveDisDic(request, response);
        } else if ("/workbench/DiseaseDictionary/delete.do".equals(path)) {
            deleteDisDicByID(request,response);
        } else if ("/workbench/Dictionary/getAll.do".equals(path)) {
            //4 代表诊断医生；5代表报告医生；6代表登记员 按年
            getAll(request, response);
        }else if ("/workbench/Disease/update.do".equals(path)) {
            updateDisDicByID(request, response);
        }else if ("/workbench/ValueDictionary/save.do".equals(path)){
            saveDicValue(request, response);
        }else if("/workbench/Value/pageList.do".equals(path)){
            pageList_value(request, response);
        }else if("/workbench/ValueDictionary/delete.do".equals(path)){
            deleteDicValueByID(request, response);
        }else if("/workbench/DictionaryValue/getAll.do".equals(path)){
            getAll_value(request, response);
        }else if("/workbench/Value/update.do".equals(path)){
            updateDicValueByID(request, response);
        }
        /**/
    }

    /*System.out.println("执行市场活动的删除操作");

    String ids[] = request.getParameterValues("id");

    ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

    boolean flag = as.delete(ids);

        PrintJson.printJsonFlag(response, flag);*/
    public void deleteDicValueByID(HttpServletRequest request, HttpServletResponse response){
        System.out.println("执行删除字典值操作");
        //从前端获取值
        String ids[] = request.getParameterValues("id");
        boolean flag1 = false;
        boolean flag2 = false;
        for(int i =0;i<ids.length;i++)
        {
            String id = ids[i];
            //获取value(bodyPart)
            DicService ds1 = (DicService) ServiceFactory.getService(new DicServiceImpl());
            DicValue dv1 = ds1.getDicValueByID(id);
            String bodyPart = dv1.getValue();
            System.out.println("要删除的bodyPart为:"+bodyPart);
            System.out.println("dv1"+dv1);

            //从dicValue中删除
            DicService ds2 = (DicService) ServiceFactory.getService(new DicServiceImpl());
            flag1 = (ds2.deleteDicValueByID(id)==1);

            //从diseaseDictionary中删除 根据bodyPart删除
            DicService ds3= (DicService) ServiceFactory.getService(new DicServiceImpl());
            flag2 = (ds3.deleteDisDicByBodyPart(bodyPart)==1);

            //更新缓存
            System.out.println("更新缓存......");
            ServletContext application = request.getServletContext();
            //更新dicValue缓存
            String typeCode = dv1.getTypeCode()+"List";
            List<DicValue> dvlist= (List<DicValue>)application.getAttribute(typeCode); //获取当前缓存
            dvlist.remove(dv1); //直接删除dv1即可
            application.setAttribute(typeCode,dvlist);
            System.out.println("更新"+typeCode+"字典:"+dvlist.toString());
            //更新diseaseDictionary缓存
            String bplist = bodyPart+"List";
            application.removeAttribute(bplist);
            System.out.println("更新diseaseDictionary缓存完成");
        }

        PrintJson.printJsonObj(response,flag1);
    }

    public void updateDicValueByID(HttpServletRequest request, HttpServletResponse response){
        System.out.println("执行修改字典值操作");

        //从前端获取数据
        String id = request.getParameter("id");
        String value = request.getParameter("value");
        System.out.println("要修改的id:"+id+" 修改后的value:"+value);

        //获取原value值
        DicService ds1 = (DicService) ServiceFactory.getService(new DicServiceImpl());
        DicValue dv1 = ds1.getDicValueByID(id);
        String oldValue = dv1.getValue();

        //修改dic_value表的value
        DicService ds2 = (DicService) ServiceFactory.getService(new DicServiceImpl());
        boolean flag1 = (ds2.updateDicValueByID(id, value) == 1);

        //修改disease_dictionary的bodyPart
        //要先获得bodyPart值=oldValue的疾病的id
        DicService ds3 = (DicService) ServiceFactory.getService(new DicServiceImpl());
        List<DiseaseDictionary> dds = ds3.getDisDicByBodyPart(oldValue);
        for(int i=0;i<dds.size();i++){
            System.out.println("修改id为"+dds.get(i)+"的bodyPart为:"+value);
            DicService ds4 = (DicService) ServiceFactory.getService(new DicServiceImpl());
            ds4.updateDisDicValueByID(id,value);
        }
        //更新缓存
        System.out.println("更新缓存......");
        ServletContext application = request.getServletContext();
        //更新dic_value缓存
        String typeCode = dv1.getTypeCode()+"List";
        List<DicValue> dvlist= (List<DicValue>)application.getAttribute(typeCode); //获取当前缓存
        for(int i=0;i< dvlist.size();i++){
            DicValue dv2 = dvlist.get(i);
            if(dv2.getId().equals(id)){ //通过id查找
                dv2.setValue(value);
                dv2.setText(value);
                dvlist.set(i,dv2); //根据索引改变元素值
            }
        }
        application.setAttribute(typeCode,dvlist);
        System.out.println("更新"+typeCode+"字典:"+dvlist.toString());
        //更新diseaseDictionary缓存
        String bodyPart = oldValue+"List";
        for(int i=0;i<dds.size();i++){
            DiseaseDictionary dd = dds.get(i);
            dd.setBodyPart(value);
        }
        String newBodyPart = value+"List";
        application.removeAttribute(bodyPart); //删除旧缓存
        application.setAttribute(newBodyPart,dds);
        System.out.println("更新"+bodyPart+"字典，改名为"+newBodyPart+":"+dds.toString());

        PrintJson.printJsonObj(response,flag1);
    }

    private void getAll_value(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        System.out.println("id又没与值"+id);
        DicService dicService1 = (DicService) ServiceFactory.getService(new DicServiceImpl());
        DicValue dd = dicService1.getValueByID(id);
        PrintJson.printJsonObj(response,dd);
    }

    public void deleteDisDicByID(HttpServletRequest request, HttpServletResponse response){
        System.out.println("执行删除疾病字典操作");
        String ids[] = request.getParameterValues("id");
        boolean flag1 = false;
        for(int i =0;i<ids.length;i++) {
            String id = ids[i];

            DicService dicService1 = (DicService) ServiceFactory.getService(new DicServiceImpl());
            DiseaseDictionary dd = dicService1.getDisDicByID(id);

            DicService dicService = (DicService) ServiceFactory.getService(new DicServiceImpl());
            if (dicService.deleteDisDicByID(id) == 1) {
                flag1 = true;
                System.out.println("删除疾病字典成功");
            }
            //更新缓存
            ServletContext application = request.getServletContext();
            String type = dd.getBodyPart() + "List";
            List<DiseaseDictionary> dl = (List<DiseaseDictionary>) application.getAttribute(type);
            for (int j = 0; j < dl.size(); j++) {
                if (dl.get(j).getId().equals(id)) {
                    dl.remove(j);
                    break;
                }
            }
            request.setAttribute(type, dl);
            System.out.println("缓存更新：" + dl);
        }
        PrintJson.printJsonObj(response, flag1);
    }

    private void pageList_value(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到疾病字典列表的操作（结合条件查询+分页查询）");
        //"DiseaseID"
        //"bodyParts"
        //"DiseaseName
        String typeCode = request.getParameter("typeCode");
        String value = request.getParameter("value");
        System.out.println("code:"+typeCode);
        System.out.println("value:"+value);
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("typeCode", typeCode);
        map.put("value", value);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        DicService dicService = (DicService) ServiceFactory.getService(new DicServiceImpl());

        //vo位于src/main/java/com/bjpowernode/crm/vo/PaginationVO.java
        //里面有详细用法简述
        PaginationVO<DicValue> paginationVO = dicService.pageList_Value(map);
        //System.out.println("输出成功");
        //vo--> {"total":100,"dataList":[{待写报告1},{2},{3}]}
        PrintJson.printJsonObj(response, paginationVO);
    }

    public void saveDicValue(HttpServletRequest request, HttpServletResponse response){
        System.out.println("执行增加字典值操作");

        //从前端获取值
        String id = UUIDUtil.getUUID();
        String value = request.getParameter("value");
        String typeCode = request.getParameter("typeCode");

        DicService dicService1 = (DicService) ServiceFactory.getService(new DicServiceImpl());
        int no = dicService1.getMaxOrderNo(typeCode) + 1;
        String orderNo = String.valueOf(no);

        //实例
        DicValue dicValue = new DicValue();
        dicValue.setId(id);
        dicValue.setValue(value);
        dicValue.setText(value);
        dicValue.setOrderNo(orderNo);
        dicValue.setTypeCode(typeCode);
        System.out.println("要增加的字典值："+dicValue);

        DicService dicService2 = (DicService) ServiceFactory.getService(new DicServiceImpl());
        boolean flag = false;
        if(dicService2.saveDicValue(dicValue)==1) {
            System.out.println("新增字典值成功");
            flag = true;
        }

        //更新缓存
        ServletContext application = request.getServletContext();
        String type = typeCode+"List";
        List<DicValue> dicValueList = (List<DicValue>)application.getAttribute(type);
        dicValueList.add(dicValue);
        request.setAttribute(type,dicValueList);
        System.out.println("缓存更新："+dicValueList);

        PrintJson.printJsonObj(response,flag);
    }

    private void getAll(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        DicService dicService1 = (DicService) ServiceFactory.getService(new DicServiceImpl());
        DiseaseDictionary dd = dicService1.getDisDicByID(id);
        PrintJson.printJsonObj(response,dd);
    }

    public void updateDisDicByID(HttpServletRequest request, HttpServletResponse response){
        System.out.println("执行修改疾病字典操作");
        String id = request.getParameter("id");
        String bodyPart = request.getParameter("bodyPart");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String modifyUserID = request.getParameter("modifyUserID");
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String modifyTime = formatter.format(date);
//        System.out.println("时间"+modifyTime);

        DiseaseDictionary diseaseDictionary = new DiseaseDictionary();
        diseaseDictionary.setModifyUserID(modifyUserID);
        diseaseDictionary.setBodyPart(bodyPart);
        diseaseDictionary.setName(name);
        diseaseDictionary.setDescription(description);
        diseaseDictionary.setModifyTime(modifyTime);
        diseaseDictionary.setId(id);

        /*System.out.println("疾病字典修改之后为"+diseaseDictionary);*/

        DicService dicService1 = (DicService) ServiceFactory.getService(new DicServiceImpl());
        DiseaseDictionary dd = dicService1.getDisDicByID(id);

        DicService dicService = (DicService) ServiceFactory.getService(new DicServiceImpl());
        boolean flag = false;
        if(dicService.updateDisDicByID(diseaseDictionary)==1) {
            System.out.println("更新疾病字典成功");
            flag = true;
        }

        //更新缓存
        ServletContext application = request.getServletContext();
        String type = dd.getBodyPart()+"List";
        List<DiseaseDictionary> dl = (List<DiseaseDictionary>)application.getAttribute(type);
        for(int i=0;i<dl.size();i++){
            if(dl.get(i).getId().equals(id)){
                dl.remove(dl.get(i));
                break;
            }
        }
        dl.add(diseaseDictionary);
        request.setAttribute(type,dl);
        System.out.println("缓存更新："+dl);

        PrintJson.printJsonObj(response,flag);
    }

    public void saveDisDic(HttpServletRequest request, HttpServletResponse response){
        System.out.println("执行增加疾病字典操作");
        String id = UUIDUtil.getUUID();
        String bodyPart = request.getParameter("bodyPart");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String createUserID = request.getParameter("createUserID");
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String createTime = formatter.format(date);

        DiseaseDictionary diseaseDictionary = new DiseaseDictionary();
        diseaseDictionary.setId(id);
        diseaseDictionary.setBodyPart(bodyPart);
        diseaseDictionary.setName(name);
        diseaseDictionary.setDescription(description);
        diseaseDictionary.setCreateUserID(createUserID);
        diseaseDictionary.setCreateTime(createTime);

        DicService dicService = (DicService) ServiceFactory.getService(new DicServiceImpl());
        if(dicService.saveDisDic(diseaseDictionary)==1)
            System.out.println("新增疾病字典成功");

        //更新缓存
        ServletContext application = request.getServletContext();
        String type = bodyPart+"List";
        List<DiseaseDictionary> dl = (List<DiseaseDictionary>)application.getAttribute(type);
        dl.add(diseaseDictionary);
        request.setAttribute(type,dl);
        System.out.println("缓存更新："+dl);

        PrintJson.printJsonObj(response,true);
    }

    private void pageList_Disease(HttpServletRequest request, HttpServletResponse response, int i) {

        System.out.println("进入到疾病字典列表的操作（结合条件查询+分页查询）");
        //"DiseaseID"
        //"bodyParts"
        //"DiseaseName
        String id = request.getParameter("DiseaseID");
        String name = request.getParameter("DiseaseName");
        String bodyPart = request.getParameter("bodyParts");
        System.out.println("两个东子"+bodyPart+"  "+name);

        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id", id);
        map.put("name", name);
        map.put("bodyPart",bodyPart);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        DicService dicService = (DicService) ServiceFactory.getService(new DicServiceImpl());

        //vo位于src/main/java/com/bjpowernode/crm/vo/PaginationVO.java
        //里面有详细用法简述
        PaginationVO<DiseaseDictionary> paginationVO = dicService.pageList_Disease(map);
        //System.out.println("输出成功");
        //vo--> {"total":100,"dataList":[{待写报告1},{2},{3}]}
        PrintJson.printJsonObj(response, paginationVO);

    }


}