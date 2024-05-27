package com.by.ris_springboot.web.dao;


import com.by.ris_springboot.web.domain.WorkList;

import java.util.List;
import java.util.Map;

public interface WorkListDao {
    List<WorkList> getWorkList(Map<String, Object> map);
}
