package com.medical360.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AccessLogDTO {
    private Long id;
    private Long userId;
    private String username;
    private Long patientId;
    private String patientName;
    private String action;
    private String requestDetail;
    private String ipAddress;
    private LocalDateTime accessDatetime;
}
