package com.medical360.integration.adapter;

import java.util.List;
import java.util.Map;

public interface DataSourceAdapter {
    String getSourceType();

    List<Map<String, Object>> fetchLabOrders(String lastSyncTime);

    List<Map<String, Object>> fetchLabResults(String lastSyncTime);

    List<Map<String, Object>> fetchImagingOrders(String lastSyncTime);

    List<Map<String, Object>> fetchImagingReports(String lastSyncTime);

    Map<String, Object> mapLabOrderFields(Map<String, Object> source);

    Map<String, Object> mapLabResultFields(Map<String, Object> source);

    Map<String, Object> mapImagingOrderFields(Map<String, Object> source);

    Map<String, Object> mapImagingReportFields(Map<String, Object> source);
}
