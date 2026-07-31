package com.medical360.service;

import com.medical360.dto.AccessLogDTO;

import java.util.List;

public interface AccessLogService {
    void logAccess(Long userId, Long patientId, String action, String requestDetail, String ipAddress);

    List<AccessLogDTO> listAccessLogs(
        Long userId,
        Long patientId,
        String action,
        String startDate,
        String endDate,
        int page,
        int size
    );

    long countAccessLogs(Long userId, Long patientId, String action, String startDate, String endDate);
}
