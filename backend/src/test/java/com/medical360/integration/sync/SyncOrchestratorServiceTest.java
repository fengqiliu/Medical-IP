package com.medical360.integration.sync;

import com.medical360.integration.adapter.DataSourceAdapter;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SyncOrchestratorServiceTest {

    @Test
    void shouldSyncSpecifiedSourceAndReturnSuccessSummary() throws Exception {
        DataSourceAdapter lisAdapter = new StubLisAdapter();
        Object orchestrator = newSyncOrchestrator(List.of(lisAdapter));

        Map<String, Object> result = invokeSyncSource(orchestrator, "LIS", "2026-04-25T00:00:00");

        assertEquals("LIS", result.get("sourceType"));
        assertEquals("SUCCESS", result.get("status"));
        assertEquals(1, result.get("labOrderCount"));
    }

    @Test
    void shouldContinueOtherSourcesWhenOneSourceFails() throws Exception {
        DataSourceAdapter broken = new BrokenAdapter();
        DataSourceAdapter lisAdapter = new StubLisAdapter();
        Object orchestrator = newSyncOrchestrator(List.of(broken, lisAdapter));

        List<Map<String, Object>> results = invokeSyncAllSources(orchestrator, "2026-04-25T00:00:00");

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(item -> "BROKEN".equals(item.get("sourceType")) && "FAILED".equals(item.get("status"))));
        assertTrue(results.stream().anyMatch(item -> "LIS".equals(item.get("sourceType")) && "SUCCESS".equals(item.get("status"))));
    }

    @Test
    void shouldReturnFailureWhenSourceTypeNotFound() throws Exception {
        DataSourceAdapter lisAdapter = new StubLisAdapter();
        Object orchestrator = newSyncOrchestrator(List.of(lisAdapter));

        Map<String, Object> result = invokeSyncSource(orchestrator, "EMR", "2026-04-25T00:00:00");

        assertEquals("EMR", result.get("sourceType"));
        assertEquals("FAILED", result.get("status"));
        assertFalse(String.valueOf(result.get("errorMessage")).isBlank());
    }

    private static Object newSyncOrchestrator(List<DataSourceAdapter> adapters) throws Exception {
        Class<?> clazz = assertDoesNotThrow(
                () -> Class.forName("com.medical360.integration.sync.SyncOrchestratorService"),
                "SyncOrchestratorService class should exist in red phase runtime checks"
        );
        Constructor<?> constructor = clazz.getDeclaredConstructor(List.class);
        constructor.setAccessible(true);
        return constructor.newInstance(adapters);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> invokeSyncSource(Object target, String sourceType, String lastSyncTime) throws Exception {
        Method method = target.getClass().getMethod("syncSource", String.class, String.class);
        return (Map<String, Object>) method.invoke(target, sourceType, lastSyncTime);
    }

    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> invokeSyncAllSources(Object target, String lastSyncTime) throws Exception {
        Method method = target.getClass().getMethod("syncAllSources", String.class);
        return (List<Map<String, Object>>) method.invoke(target, lastSyncTime);
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
