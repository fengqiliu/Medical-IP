package com.medical360.service;

public interface AccessLogService {
    void logAccess(Long userId, Long patientId, String action, String requestDetail, String ipAddress);
}
