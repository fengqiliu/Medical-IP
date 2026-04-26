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
    public List<Map<String, Object>> fetchPatients(String lastSyncTime) {
        return Collections.emptyList();
    }

    @Override
    public List<Map<String, Object>> fetchEncounters(String lastSyncTime) {
        return Collections.emptyList();
    }
}
