package com.medical360.integration.adapter;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LISAdapter implements DataSourceAdapter {

    @Override
    public String getSourceType() {
        return "LIS";
    }

    @Override
    public List<Map<String, Object>> fetchLabOrders(String lastSyncTime) {
        // TODO: Implement actual LIS API call
        return Collections.emptyList();
    }

    @Override
    public List<Map<String, Object>> fetchLabResults(String lastSyncTime) {
        return Collections.emptyList();
    }

    @Override
    public List<Map<String, Object>> fetchImagingOrders(String lastSyncTime) {
        return Collections.emptyList();
    }

    @Override
    public List<Map<String, Object>> fetchImagingReports(String lastSyncTime) {
        return Collections.emptyList();
    }

    @Override
    public Map<String, Object> mapLabOrderFields(Map<String, Object> source) {
        Map<String, Object> mapped = new HashMap<>();
        mapped.put("order_no", source.get("LAB_NO"));
        mapped.put("patient_id", source.get("PATIENT_ID"));
        mapped.put("order_datetime", source.get("ORDER_TIME"));
        mapped.put("specimen_type", source.get("SPECIMEN_TYPE"));
        mapped.put("status", source.get("STATUS"));
        return mapped;
    }

    @Override
    public Map<String, Object> mapLabResultFields(Map<String, Object> source) {
        Map<String, Object> mapped = new HashMap<>();
        mapped.put("item_code", source.get("TEST_CODE"));
        mapped.put("item_name", source.get("TEST_NAME"));
        mapped.put("result_value", source.get("RESULT"));
        mapped.put("unit", source.get("UNIT"));
        mapped.put("abnormal_flag", source.get("ABNORMAL"));
        return mapped;
    }

    @Override
    public Map<String, Object> mapImagingOrderFields(Map<String, Object> source) {
        Map<String, Object> mapped = new HashMap<>();
        mapped.put("order_no", source.get("EXAM_NO"));
        mapped.put("patient_id", source.get("PATIENT_ID"));
        mapped.put("body_part", source.get("BODY_PART"));
        mapped.put("modality", source.get("MODALITY"));
        mapped.put("order_datetime", source.get("ORDER_TIME"));
        return mapped;
    }

    @Override
    public Map<String, Object> mapImagingReportFields(Map<String, Object> source) {
        Map<String, Object> mapped = new HashMap<>();
        mapped.put("report_content", source.get("FINDINGS"));
        mapped.put("impression", source.get("CONCLUSION"));
        mapped.put("report_doctor", source.get("REPORT_DOCTOR"));
        mapped.put("report_datetime", source.get("REPORT_TIME"));
        return mapped;
    }
}
