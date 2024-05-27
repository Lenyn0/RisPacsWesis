package com.by.ris_springboot.settings.service;



import com.by.ris_springboot.settings.domain.Device;
import com.by.ris_springboot.vo.PaginationVO;

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
