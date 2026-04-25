package com.medical360.integration.mapper;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class FieldMappingService {

    private static final Map<String, String> LIS_LAB_ORDER_MAPPINGS = new HashMap<>();
    private static final Map<String, String> LIS_LAB_RESULT_MAPPINGS = new HashMap<>();
    private static final Map<String, String> RIS_IMAGING_ORDER_MAPPINGS = new HashMap<>();
    private static final Map<String, String> RIS_IMAGING_REPORT_MAPPINGS = new HashMap<>();
    private static final Map<String, String> PACS_IMAGING_REPORT_MAPPINGS = new HashMap<>();
    private static final Map<String, String> EMR_PATIENT_MAPPINGS = new HashMap<>();
    private static final Map<String, String> EMR_ENCOUNTER_MAPPINGS = new HashMap<>();
    private static final Map<String, String> VALUE_CONVERTERS = new HashMap<>();

    static {
        LIS_LAB_ORDER_MAPPINGS.put("LAB_NO", "orderNo");
        LIS_LAB_ORDER_MAPPINGS.put("PATIENT_ID", "patientId");
        LIS_LAB_ORDER_MAPPINGS.put("ORDER_TIME", "orderDatetime");
        LIS_LAB_ORDER_MAPPINGS.put("SPECIMEN_TYPE", "specimenType");
        LIS_LAB_ORDER_MAPPINGS.put("STATUS", "status");

        LIS_LAB_RESULT_MAPPINGS.put("LAB_ORDER_ID", "labOrderId");
        LIS_LAB_RESULT_MAPPINGS.put("TEST_CODE", "itemCode");
        LIS_LAB_RESULT_MAPPINGS.put("TEST_NAME", "itemName");
        LIS_LAB_RESULT_MAPPINGS.put("RESULT", "resultValue");
        LIS_LAB_RESULT_MAPPINGS.put("UNIT", "unit");
        LIS_LAB_RESULT_MAPPINGS.put("ABNORMAL", "abnormalFlag");
        LIS_LAB_RESULT_MAPPINGS.put("REF_LOW", "refRangeLow");
        LIS_LAB_RESULT_MAPPINGS.put("REF_HIGH", "refRangeHigh");
        LIS_LAB_RESULT_MAPPINGS.put("RESULT_TIME", "resultDatetime");

        RIS_IMAGING_ORDER_MAPPINGS.put("EXAM_NO", "orderNo");
        RIS_IMAGING_ORDER_MAPPINGS.put("PATIENT_ID", "patientId");
        RIS_IMAGING_ORDER_MAPPINGS.put("ORDER_TIME", "orderDatetime");
        RIS_IMAGING_ORDER_MAPPINGS.put("BODY_PART", "bodyPart");
        RIS_IMAGING_ORDER_MAPPINGS.put("MODALITY", "modality");
        RIS_IMAGING_ORDER_MAPPINGS.put("STATUS", "status");

        RIS_IMAGING_REPORT_MAPPINGS.put("EXAM_NO", "orderNo");
        RIS_IMAGING_REPORT_MAPPINGS.put("FINDINGS", "reportContent");
        RIS_IMAGING_REPORT_MAPPINGS.put("CONCLUSION", "impression");
        RIS_IMAGING_REPORT_MAPPINGS.put("REPORT_DOCTOR", "reportDoctor");
        RIS_IMAGING_REPORT_MAPPINGS.put("REPORT_TIME", "reportDatetime");

        PACS_IMAGING_REPORT_MAPPINGS.put("EXAM_NO", "orderNo");
        PACS_IMAGING_REPORT_MAPPINGS.put("REPORT_URL", "pacsUrl");
        PACS_IMAGING_REPORT_MAPPINGS.put("REPORT_TIME", "reportDatetime");

        EMR_PATIENT_MAPPINGS.put("PATIENT_ID", "id");
        EMR_PATIENT_MAPPINGS.put("UNIFIED_PATIENT_ID", "unifiedPatientId");
        EMR_PATIENT_MAPPINGS.put("NAME", "name");
        EMR_PATIENT_MAPPINGS.put("GENDER", "gender");
        EMR_PATIENT_MAPPINGS.put("BIRTH_DATE", "birthDate");
        EMR_PATIENT_MAPPINGS.put("ID_CARD_NO", "idCardNo");

        EMR_ENCOUNTER_MAPPINGS.put("ENCOUNTER_ID", "id");
        EMR_ENCOUNTER_MAPPINGS.put("PATIENT_ID", "patientId");
        EMR_ENCOUNTER_MAPPINGS.put("ENCOUNTER_TYPE", "encounterType");
        EMR_ENCOUNTER_MAPPINGS.put("DEPARTMENT_ID", "departmentId");
        EMR_ENCOUNTER_MAPPINGS.put("VISIT_TIME", "visitDatetime");

        VALUE_CONVERTERS.put("patientId", "Long");
        VALUE_CONVERTERS.put("labOrderId", "Long");
        VALUE_CONVERTERS.put("departmentId", "Long");
        VALUE_CONVERTERS.put("birthDate", "LocalDate");
        VALUE_CONVERTERS.put("orderDatetime", "LocalDateTime");
        VALUE_CONVERTERS.put("resultDatetime", "LocalDateTime");
        VALUE_CONVERTERS.put("reportDatetime", "LocalDateTime");
        VALUE_CONVERTERS.put("visitDatetime", "LocalDateTime");
        VALUE_CONVERTERS.put("refRangeLow", "BigDecimal");
        VALUE_CONVERTERS.put("refRangeHigh", "BigDecimal");
    }

    public Map<String, Object> normalize(String sourceType, String dataType, Map<String, Object> source) {
        if (source == null || source.isEmpty()) {
            return Map.of();
        }

        Map<String, String> mappings = getMappingByType(sourceType, dataType);
        if (mappings.isEmpty()) {
            return new HashMap<>(source);
        }

        Map<String, Object> normalized = new HashMap<>();
        mappings.forEach((sourceField, targetField) -> {
            Object converted = mapValue(targetField, source.get(sourceField));
            if (converted != null) {
                normalized.put(targetField, converted);
            }
        });
        return normalized;
    }

    private Map<String, String> getMappingByType(String sourceType, String dataType) {
        return switch (sourceType + ":" + dataType) {
            case "LIS:LAB_ORDER" -> LIS_LAB_ORDER_MAPPINGS;
            case "LIS:LAB_RESULT" -> LIS_LAB_RESULT_MAPPINGS;
            case "RIS:IMAGING_ORDER" -> RIS_IMAGING_ORDER_MAPPINGS;
            case "RIS:IMAGING_REPORT" -> RIS_IMAGING_REPORT_MAPPINGS;
            case "PACS:IMAGING_REPORT" -> PACS_IMAGING_REPORT_MAPPINGS;
            case "EMR:PATIENT" -> EMR_PATIENT_MAPPINGS;
            case "EMR:ENCOUNTER" -> EMR_ENCOUNTER_MAPPINGS;
            default -> Map.of();
        };
    }

    private Object mapValue(String targetField, Object value) {
        if (value == null) {
            return null;
        }
        String type = VALUE_CONVERTERS.get(targetField);
        if (type == null) {
            return value;
        }
        String strValue = value.toString().trim();
        if (strValue.isEmpty()) {
            return null;
        }
        try {
            return switch (type) {
                case "Long" -> Long.parseLong(strValue);
                case "LocalDate" -> parseDate(strValue);
                case "LocalDateTime" -> parseDateTime(strValue);
                case "BigDecimal" -> new BigDecimal(strValue);
                default -> value;
            };
        } catch (Exception e) {
            return value;
        }
    }

    private LocalDateTime parseDateTime(String value) {
        String[] patterns = {
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy/MM/dd HH:mm:ss"
        };
        for (String pattern : patterns) {
            try {
                return LocalDateTime.parse(value, DateTimeFormatter.ofPattern(pattern));
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    private LocalDate parseDate(String value) {
        String[] patterns = {
            "yyyy-MM-dd",
            "yyyy/MM/dd",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss"
        };
        for (String pattern : patterns) {
            try {
                return LocalDate.parse(value, DateTimeFormatter.ofPattern(pattern));
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}
