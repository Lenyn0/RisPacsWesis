package com.by.ris_springboot.web.listener;

import com.by.ris_springboot.settings.domain.Device;
import com.by.ris_springboot.settings.domain.DicValue;
import com.by.ris_springboot.settings.domain.DiseaseDictionary;
import com.by.ris_springboot.settings.service.DeviceService;
import com.by.ris_springboot.settings.service.DicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class SysInitListener implements ApplicationListener<ContextRefreshedEvent>, ServletContextAware {

    @Autowired
    DicService dicService;
    @Autowired
    DeviceService deviceService;

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) { // root application context, no parent
            System.out.println("服务器缓存处理数据字典开始");

            try {
                System.out.println("dicService is " + (dicService == null ? "null" : "not null"));
                System.out.println("deviceService is " + (deviceService == null ? "null" : "not null"));

                Map<String, List<DicValue>> map = dicService.getAll();
                //将map解析为上下文域对象中保存的键值对
                Set<String> set = map.keySet();
                for (String key : set) {
                    if (key.equals("reportTemplateList")){
                        ObjectMapper objectMapper = new ObjectMapper();
                        String jsonReportTemplateList = objectMapper.writeValueAsString(map.get(key));
                        servletContext.setAttribute(key,jsonReportTemplateList);
                    }else {
                        servletContext.setAttribute(key, map.get(key));
                    }

                }

                Map<String, List<DiseaseDictionary>> dmap = dicService.getDiseaseAll();
                //将dmap解析为上下文域对象中保存的键值对
                Set<String> set1 = dmap.keySet();
                for (String key : set1) {
                    servletContext.setAttribute(key, dmap.get(key));
                }
                System.out.println("服务器缓存处理数据字典结束");

                System.out.println("服务器缓存处理设备列表开始");
                List<Device> deviceList = deviceService.getAll();
                servletContext.setAttribute("deviceList", deviceList);
                System.out.println("服务器缓存处理设备列表结束");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}