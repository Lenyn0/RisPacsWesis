package com.bjpowernode.crm.web.dao;

import com.bjpowernode.crm.web.domain.WorkList;
import com.bjpowernode.crm.workbench.domain.StudyInfo;

import java.util.List;
import java.util.Map;

public interface WorkListDao {
    List<WorkList> getWorkList(Map<String, Object> map);
}
