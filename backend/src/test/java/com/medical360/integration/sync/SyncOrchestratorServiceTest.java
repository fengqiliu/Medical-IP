package com.medical360.integration.sync;

import com.medical360.integration.adapter.DataSourceAdapter;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SyncOrchestratorServiceTest {

    @Test
    void shouldSyncSpecifiedSourceAndReturnSuccessSummary() {
        DataSourceAdapter lisAdapter = new StubLisAdapter();
        SyncOrchestratorService orchestrator = new SyncOrchestratorService(List.of(lisAdapter));

        Map<String, Object> result = orchestrator.syncSource("LIS", "2026-04-25T00:00:00");

        assertEquals("LIS", result.get("sourceType"));
        assertEquals("SUCCESS", result.get("status"));
        assertEquals(1, result.get("labOrderCount"));
    }

    @Test
    void shouldContinueOtherSourcesWhenOneSourceFails() {
        DataSourceAdapter broken = new BrokenAdapter();
        DataSourceAdapter lisAdapter = new StubLisAdapter();
        SyncOrchestratorService orchestrator = new SyncOrchestratorService(List.of(broken, lisAdapter));

        List<Map<String, Object>> results = orchestrator.syncAllSources("2026-04-25T00:00:00");

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(item -> "BROKEN".equals(item.get("sourceType")) && "FAILED".equals(item.get("status"))));
        assertTrue(results.stream().anyMatch(item -> "LIS".equals(item.get("sourceType")) && "SUCCESS".equals(item.get("status"))));
    }

    @Test
    void shouldReturnFailureWhenSourceTypeNotFound() {
        DataSourceAdapter lisAdapter = new StubLisAdapter();
        SyncOrchestratorService orchestrator = new SyncOrchestratorService(List.of(lisAdapter));

        Map<String, Object> result = orchestrator.syncSource("EMR", "2026-04-25T00:00:00");

        assertEquals("EMR", result.get("sourceType"));
        assertEquals("FAILED", result.get("status"));
        assertFalse(String.valueOf(result.get("errorMessage")).isBlank());
    }

    private static class StubLisAdapter implements DataSourceAdapter {
        @Override
        public String getSourceType() {
            return "LIS";
        }

        @Override
        public List<Map<String, Object>> fetchLabOrders(String lastSyncTime) {
            return List.of(Map.of("LAB_NO", "L-1001", "PATIENT_ID", "P-1"));
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
            return source;
        }

        @Override
        public Map<String, Object> mapLabResultFields(Map<String, Object> source) {
            return source;
        }

        @Override
        public Map<String, Object> mapImagingOrderFields(Map<String, Object> source) {
            return source;
        }

        @Override
        public Map<String, Object> mapImagingReportFields(Map<String, Object> source) {
            return source;
        }
    }

    private static final class BrokenAdapter extends StubLisAdapter {
        @Override
        public String getSourceType() {
            return "BROKEN";
        }

        @Override
        public List<Map<String, Object>> fetchLabOrders(String lastSyncTime) {
            throw new IllegalStateException("adapter unavailable");
        }
    }
}
