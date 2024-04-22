package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.dao.DeviceDao;
import com.bjpowernode.crm.settings.domain.Device;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DeviceService;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVO;

import java.util.List;
import java.util.Map;

public class DeviceServiceImpl implements DeviceService {
    private DeviceDao deviceDao = SqlSessionUtil.getSqlSession().getMapper(DeviceDao.class);
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
