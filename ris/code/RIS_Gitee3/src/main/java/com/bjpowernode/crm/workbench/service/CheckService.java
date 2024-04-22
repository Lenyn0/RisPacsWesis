package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.StudyInfo;

import java.util.Map;

public interface CheckService {
    PaginationVO<StudyInfo> pageList_Check(Map<String, Object> map);
}
