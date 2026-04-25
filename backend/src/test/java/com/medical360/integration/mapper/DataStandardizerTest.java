package com.medical360.integration.mapper;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataStandardizerTest {

    @Test
    void shouldStandardizeLabOrderFieldsFromLisSource() {
        DataStandardizer standardizer = new DataStandardizer();

        Map<String, Object> lisOrder = Map.of(
                "LAB_NO", "L-1001",
                "PATIENT_ID", "P-42",
                "ORDER_TIME", "2026-04-25T10:00:00",
                "SPECIMEN_TYPE", "Blood",
                "STATUS", "ORDERED"
        );

        Map<String, Object> standardized = standardizer.standardizeRecord("LIS", "LAB_ORDER", lisOrder);

        assertEquals("L-1001", standardized.get("order_no"));
        assertEquals("P-42", standardized.get("patient_id"));
        assertEquals("2026-04-25T10:00:00", standardized.get("order_datetime"));
        assertEquals("Blood", standardized.get("specimen_type"));
        assertEquals("ORDERED", standardized.get("status"));
    }

    @Test
    void shouldKeepUnknownFieldsAndTagSourceMetadata() {
        DataStandardizer standardizer = new DataStandardizer();

        Map<String, Object> sourceRecord = Map.of(
                "EXAM_NO", "IMG-9",
                "CUSTOM_FIELD", "kept"
        );

        Map<String, Object> standardized = standardizer.standardizeRecord("RIS", "IMAGING_ORDER", sourceRecord);

        assertEquals("IMG-9", standardized.get("order_no"));
        assertEquals("kept", standardized.get("CUSTOM_FIELD"));
        assertEquals("RIS", standardized.get("source_type"));
        assertEquals("IMAGING_ORDER", standardized.get("data_type"));
    }

    @Test
    void shouldStandardizeBatchWithStableSizeAndOrder() {
        DataStandardizer standardizer = new DataStandardizer();

        List<Map<String, Object>> sourceBatch = List.of(
                Map.of("LAB_NO", "L-1", "PATIENT_ID", "P-1"),
                Map.of("LAB_NO", "L-2", "PATIENT_ID", "P-2")
        );

        List<Map<String, Object>> standardized = standardizer.standardizeBatch("LIS", "LAB_ORDER", sourceBatch);

        assertEquals(2, standardized.size());
        assertEquals("L-1", standardized.get(0).get("order_no"));
        assertEquals("L-2", standardized.get(1).get("order_no"));
        assertTrue(standardized.stream().allMatch(item -> "LAB_ORDER".equals(item.get("data_type"))));
    }
}
