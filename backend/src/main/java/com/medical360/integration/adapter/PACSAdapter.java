package com.medical360.integration.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class PACSAdapter implements DataSourceAdapter {

    @Override
    public String getSourceType() {
        return "PACS";
    }

    @Override
    public List<Map<String, Object>> fetchLabOrders(String lastSyncTime) {
        return List.of();
    }

    @Override
    public List<Map<String, Object>> fetchLabResults(String lastSyncTime) {
        return List.of();
    }

    @Override
    public List<Map<String, Object>> fetchImagingOrders(String lastSyncTime) {
        return List.of();
    }

    @Override
    public List<Map<String, Object>> fetchImagingReports(String lastSyncTime) {
        return List.of();
    }

    @Override
    public List<Map<String, Object>> fetchPatients(String lastSyncTime) {
        return List.of();
    }

    @Override
    public List<Map<String, Object>> fetchEncounters(String lastSyncTime) {
        return List.of();
    }
}
