package com.by.ris_springboot.settings.service.impl;



import com.by.ris_springboot.settings.dao.DeviceDao;
import com.by.ris_springboot.settings.domain.Device;
import com.by.ris_springboot.settings.service.DeviceService;
import com.by.ris_springboot.vo.PaginationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceDao deviceDao;
    public List<Device> getAll() {
        List<Device> deviceList = deviceDao.getAll();
        return deviceList;
    }

    public PaginationVO<Device> pageList(Map<String, Object> map) {
        int total = deviceDao.getTotalByCondition(map);
        List<Device> deviceList = deviceDao.getDeviceListByCondition(map);
        PaginationVO<Device> vo = new PaginationVO<Device>();
        vo.setTotal(total);
        vo.setDataList(deviceList);
        return vo;
    }

    public boolean save(Device device) {
        boolean flag = true;
        int count = deviceDao.save(device);
        if(count!=1){
            flag=false;
        }
        return flag;
    }

    public boolean delete(String[] ids) {
        boolean flag = true;
        int count = deviceDao.delete(ids);
        if(count!=ids.length){
            flag=false;
        }
        return flag;
    }

    public Device detail(String id) {
        Device device = deviceDao.detail(id);
        return device;
    }

    public boolean update(Device device) {
        boolean flag = true;
        int count = deviceDao.update(device);
        if(count!=1){
            flag=false;
        }
        return flag;
    }
}
