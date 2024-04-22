package com.bjpowernode.crm.workbench.service;

import java.util.Map;

public interface ChartService {

    Map<String, Object> getCharts(int i);

    Map<String, Object> getWorkload(String startDate, String endDate, int i);
}
