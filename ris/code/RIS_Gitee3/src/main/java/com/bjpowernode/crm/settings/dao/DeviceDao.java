package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.Device;

import java.util.List;
import java.util.Map;

public interface DeviceDao {
    Device test(String s);

    List<Device> getAll();

    int getTotalByCondition(Map<String, Object> map);

    List<Device> getDeviceListByCondition(Map<String, Object> map);

    int save(Device device);

    int delete(String[] ids);

    Device detail(String id);

    int update(Device device);
}
