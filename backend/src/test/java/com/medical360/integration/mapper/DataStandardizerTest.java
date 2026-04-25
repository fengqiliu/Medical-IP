package com.medical360.integration.mapper;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DataStandardizerTest {

    private final DataStandardizer standardizer = new DataStandardizer(new FieldMappingService());

    @Test
    void shouldNormalizeRisOrderRecord() {
        Map<String, Object> raw = Map.of(
            "EXAM_NO", "RIS-001",
            "PATIENT_ID", "101",
            "BODY_PART", "胸部",
            "MODALITY", "CT",
            "ORDER_TIME", "2026-04-25T08:30:00"
        );

        NormalizedSyncPayload payload = standardizer.normalize(
            "RIS",
            List.of(),
            List.of(),
            List.of(raw),
            List.of(),
            List.of(),
            List.of()
        );

        assertThat(payload.imagingOrders()).hasSize(1);
        assertThat(payload.imagingOrders().get(0).get("orderNo")).isEqualTo("RIS-001");
        assertThat(payload.imagingOrders().get(0).get("patientId")).isEqualTo(101L);
    }

    @Test
    void shouldNormalizeEmrPatientRecord() {
        Map<String, Object> raw = Map.of(
            "PATIENT_ID", "11",
            "UNIFIED_PATIENT_ID", "P20240011",
            "NAME", "王测试",
            "GENDER", "男"
        );

        NormalizedSyncPayload payload = standardizer.normalize(
            "EMR",
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(raw),
            List.of()
        );

        assertThat(payload.patients()).hasSize(1);
        assertThat(payload.patients().get(0).get("name")).isEqualTo("王测试");
        assertThat(payload.patients().get(0).get("unifiedPatientId")).isEqualTo("P20240011");
    }
}
