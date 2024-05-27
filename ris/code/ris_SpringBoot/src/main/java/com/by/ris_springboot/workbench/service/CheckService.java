package com.by.ris_springboot.workbench.service;


import com.by.ris_springboot.vo.PaginationVO;
import com.by.ris_springboot.workbench.domain.StudyInfo;

import java.util.Map;

public interface CheckService {
    PaginationVO<StudyInfo> pageList_Check(Map<String, Object> map);
}
