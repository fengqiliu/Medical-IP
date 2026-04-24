package com.medical360.dto;

import lombok.Data;

@Data
public class TimelineEventDTO {
    private Long id;
    private String eventType;
    private String eventDatetime;
    private String eventSummary;
    private String eventStatus;
    private Long referenceId;
    private String departmentName;
    private Boolean abnormal;
}