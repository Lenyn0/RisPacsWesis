package com.by.ris_springboot.settings.controller;

import com.by.ris_springboot.settings.domain.Device;
import com.by.ris_springboot.settings.service.DeviceService;
import com.by.ris_springboot.utils.UUIDUtil;
import com.by.ris_springboot.vo.PaginationVO;
import com.by.ris_springboot.vo.Result;
import com.by.ris_springboot.vo.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("settings/device/")
public class DeviceController{

    @Autowired
    DeviceService deviceService;


    @PostMapping("update.do")
    private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        System.out.println("进入到修改设备信息的操作");
        String id = request.getParameter("id");
        String name = request.getParameter("devicename");
        String aet = request.getParameter("aet");
        String port = request.getParameter("port");
        String ip = request.getParameter("ip");
        String room = request.getParameter("room");
        Device device = new Device();
        device.setId(id);
        device.setName(name);
        device.setAet(aet);
        device.setPort(port);
        device.setIp(ip);
        device.setRoom(room);
        //DeviceService ds = (DeviceService) ServiceFactory.getService(new DeviceServiceImpl());
        boolean flag = deviceService.update(device);
        if(flag){
            //修改成功
            //DeviceService ds1 = (DeviceService) ServiceFactory.getService(new DeviceServiceImpl());
            Device device1 = deviceService.detail(id);
            request.setAttribute("device", device1);
            request.getRequestDispatcher("/settings/device/detail.jsp").forward(request, response);
        }
    }


    @GetMapping("detail.do")
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        System.out.println("进入到跳转到设备详细信息页的操作");
        String id = request.getParameter("id");
        //DeviceService ds = (DeviceService) ServiceFactory.getService(new DeviceServiceImpl());
        Device device = deviceService.detail(id);
        request.setAttribute("device", device);
        request.getRequestDispatcher("/settings/device/detail.jsp").forward(request, response);
    }

    @PostMapping("delete.do")
    private Result delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行用户信息的删除操作");
        String ids[] = request.getParameterValues("id");
        //DeviceService ds = (DeviceService) ServiceFactory.getService(new DeviceServiceImpl());
        boolean flag = deviceService.delete(ids);
        return Results.newSuccessResult(flag);
        //PrintJson.printJsonFlag(response, flag);
    }

    @PostMapping("save.do")
    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        System.out.println("进入到添加设备的操作");
        String id = UUIDUtil.getUUID();
        String name = request.getParameter("devicename");
        String aet = request.getParameter("aet");
        String port = request.getParameter("port");
        String ip = request.getParameter("ip");
        String room = request.getParameter("room");
        Device device = new Device();
        device.setId(id);
        device.setName(name);
        device.setAet(aet);
        device.setPort(port);
        device.setIp(ip);
        device.setRoom(room);
        //DeviceService ds = (DeviceService) ServiceFactory.getService(new DeviceServiceImpl());
        boolean flag = deviceService.save(device);
        if(flag){
            //创建成功
            response.sendRedirect(request.getContextPath()+"/settings/device/index.jsp");
        }
    }

    @GetMapping("pageList.do")
    private Result pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询设备信息列表的操作【结合条件查询+分页查询】");
        String name = request.getParameter("devicename");
        String aet = request.getParameter("aet");
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
        map.put("aet",aet);
        map.put("room",room);
        map.put("pageSize",pageSize);
        map.put("skipCount",skipCount);
        //DeviceService ds = (DeviceService) ServiceFactory.getService(new DeviceServiceImpl());
        PaginationVO<Device> vo = deviceService.pageList(map);
        return Results.newSuccessResult(vo);
        //PrintJson.printJsonObj(response, vo);
    }
}
