package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.Device;
import com.bjpowernode.crm.vo.PaginationVO;

import java.util.List;
import java.util.Map;

public interface DeviceService {
    List<Device> getAll();

    PaginationVO<Device> pageList(Map<String, Object> map);

    boolean save(Device device);

    boolean delete(String[] ids);

    Device detail(String id);

    boolean update(Device device);
}
