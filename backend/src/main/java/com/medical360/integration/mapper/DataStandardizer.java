package com.medical360.integration.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DataStandardizer {

    private final FieldMappingService fieldMappingService;

    public NormalizedSyncPayload normalize(
        String sourceType,
        List<Map<String, Object>> labOrders,
        List<Map<String, Object>> labResults,
        List<Map<String, Object>> imagingOrders,
        List<Map<String, Object>> imagingReports,
        List<Map<String, Object>> patients,
        List<Map<String, Object>> encounters
    ) {
        return new NormalizedSyncPayload(
            normalizeRecords(sourceType, "LAB_ORDER", labOrders),
            normalizeRecords(sourceType, "LAB_RESULT", labResults),
            normalizeRecords(sourceType, "IMAGING_ORDER", imagingOrders),
            normalizeRecords(sourceType, "IMAGING_REPORT", imagingReports),
            normalizeRecords(sourceType, "PATIENT", patients),
            normalizeRecords(sourceType, "ENCOUNTER", encounters)
        );
    }

    private List<Map<String, Object>> normalizeRecords(String sourceType, String dataType, List<Map<String, Object>> records) {
        if (records == null || records.isEmpty()) {
            return List.of();
        }
        return records.stream()
            .map(record -> fieldMappingService.normalize(sourceType, dataType, record))
            .toList();
    }
}
