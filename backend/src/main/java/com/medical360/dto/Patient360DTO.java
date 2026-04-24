package com.medical360.dto;

import lombok.Data;
import java.util.List;

@Data
public class Patient360DTO {
    private PatientDTO patient;
    private EncounterDTO encounter;
    private LabSummaryDTO labSummary;
    private ImagingSummaryDTO imagingSummary;
    private String aiSummary;
    private List<AlertDTO> alerts;

    @Data
    public static class PatientDTO {
        private Long id;
        private String unifiedPatientId;
        private String name;
        private String gender;
        private String birthDate;
        private String idCardNo;
        private String phone;
    }

    @Data
    public static class EncounterDTO {
        private Long id;
        private String encounterType;
        private String departmentName;
        private String visitDatetime;
        private String admissionReason;
    }

    @Data
    public static class LabSummaryDTO {
        private Integer recentCount;
        private Integer abnormalCount;
        private List<LabOrderDTO> recentOrders;
    }

    @Data
    public static class LabOrderDTO {
        private Long id;
        private String orderNo;
        private String orderDatetime;
        private String specimenType;
        private String status;
        private String departmentName;
    }

    @Data
    public static class ImagingSummaryDTO {
        private Integer recentCount;
        private List<ImagingOrderDTO> recentOrders;
    }

    @Data
    public static class ImagingOrderDTO {
        private Long id;
        private String orderNo;
        private String orderDatetime;
        private String bodyPart;
        private String modality;
        private String status;
        private String departmentName;
    }

    @Data
    public static class AlertDTO {
        private String type;
        private String message;
        private String datetime;
    }
}