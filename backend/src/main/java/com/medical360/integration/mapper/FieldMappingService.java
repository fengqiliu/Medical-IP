package com.medical360.integration.mapper;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FieldMappingService {

    private static final Map<String, String> LIS_MAPPINGS = new HashMap<>();
    private static final Map<String, String> RIS_MAPPINGS = new HashMap<>();

    static {
        // LIS field mappings: source field -> target field
        LIS_MAPPINGS.put("LAB_NO", "order_no");
        LIS_MAPPINGS.put("PATIENT_ID", "patient_id");
        LIS_MAPPINGS.put("ORDER_TIME", "order_datetime");
        LIS_MAPPINGS.put("SPECIMEN_TYPE", "specimen_type");
        LIS_MAPPINGS.put("STATUS", "status");

        // RIS field mappings
        RIS_MAPPINGS.put("EXAM_NO", "order_no");
        RIS_MAPPINGS.put("PATIENT_ID", "patient_id");
        RIS_MAPPINGS.put("BODY_PART", "body_part");
        RIS_MAPPINGS.put("MODALITY", "modality");
        RIS_MAPPINGS.put("ORDER_TIME", "order_datetime");
    }

    public Map<String, String> getLisMapping() {
        return LIS_MAPPINGS;
    }

    public Map<String, String> getRisMapping() {
        return RIS_MAPPINGS;
    }

    public Object mapValue(String sourceType, String sourceField, Object value) {
        return value;
    }
}
