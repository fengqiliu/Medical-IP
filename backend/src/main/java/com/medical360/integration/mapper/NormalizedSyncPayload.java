package com.medical360.integration.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public record NormalizedSyncPayload(
    List<Map<String, Object>> labOrders,
    List<Map<String, Object>> labResults,
    List<Map<String, Object>> imagingOrders,
    List<Map<String, Object>> imagingReports,
    List<Map<String, Object>> patients,
    List<Map<String, Object>> encounters
) {
    public static NormalizedSyncPayload empty() {
        return new NormalizedSyncPayload(
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList()
        );
    }
}
