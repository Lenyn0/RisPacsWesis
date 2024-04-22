package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.domain.Device;
import com.bjpowernode.crm.settings.service.DeviceService;
import com.bjpowernode.crm.settings.service.impl.DeviceServiceImpl;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DeviceController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到设备控制器");
        String path = request.getServletPath();
        if("/settings/device/pageList.do".equals(path)){
            pageList(request,response);
        }else if("/settings/device/save.do".equals(path)){
            save(request,response);
        }else if("/settings/device/detail.do".equals(path)){
            detail(request,response);
        }else if("/settings/device/update.do".equals(path)){
            update(request,response);
        }else if("/settings/device/delete.do".equals(path)){
            delete(request,response);
        }else if("/settings/device/xxx.do".equals(path)){
            //xxx(request,response);
        }
    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        System.out.println("进入到修改设备信息的操作");
        String id = request.getParameter("id");
        String name = request.getParameter("devicename");
        String AET = request.getParameter("AET");
        String port = request.getParameter("port");
        String ip = request.getParameter("ip");
        String room = request.getParameter("room");
        Device device = new Device();
        device.setId(id);
        device.setName(name);
        device.setAET(AET);
        device.setPort(port);
        device.setIp(ip);
        device.setRoom(room);
        DeviceService ds = (DeviceService) ServiceFactory.getService(new DeviceServiceImpl());
        boolean flag = ds.update(device);
        if(flag){
            //修改成功
            DeviceService ds1 = (DeviceService) ServiceFactory.getService(new DeviceServiceImpl());
            Device device1 = ds1.detail(id);
            request.setAttribute("device", device1);
            request.getRequestDispatcher("/settings/device/detail.jsp").forward(request, response);
        }
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        System.out.println("进入到跳转到设备详细信息页的操作");
        String id = request.getParameter("id");
        DeviceService ds = (DeviceService) ServiceFactory.getService(new DeviceServiceImpl());
        Device device = ds.detail(id);
        request.setAttribute("device", device);
        request.getRequestDispatcher("/settings/device/detail.jsp").forward(request, response);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行用户信息的删除操作");
        String ids[] = request.getParameterValues("id");
        DeviceService ds = (DeviceService) ServiceFactory.getService(new DeviceServiceImpl());
        boolean flag = ds.delete(ids);
        PrintJson.printJsonFlag(response, flag);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        System.out.println("进入到添加设备的操作");
        String id = UUIDUtil.getUUID();
        String name = request.getParameter("devicename");
        String AET = request.getParameter("AET");
        String port = request.getParameter("port");
        String ip = request.getParameter("ip");
        String room = request.getParameter("room");
        Device device = new Device();
        device.setId(id);
        device.setName(name);
        device.setAET(AET);
        device.setPort(port);
        device.setIp(ip);
        device.setRoom(room);
        DeviceService ds = (DeviceService) ServiceFactory.getService(new DeviceServiceImpl());
        boolean flag = ds.save(device);
        if(flag){
            //创建成功
            response.sendRedirect(request.getContextPath()+"/settings/device/index.jsp");
        }
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询设备信息列表的操作【结合条件查询+分页查询】");
        String name = request.getParameter("devicename");
        String AET = request.getParameter("AET");
        String room = request.getParameter("room");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name",name);
        map.put("AET",AET);
        map.put("room",room);
        map.put("pageSize",pageSize);
        map.put("skipCount",skipCount);
        DeviceService ds = (DeviceService) ServiceFactory.getService(new DeviceServiceImpl());
        PaginationVO<Device> vo = ds.pageList(map);
        PrintJson.printJsonObj(response, vo);
    }
}
