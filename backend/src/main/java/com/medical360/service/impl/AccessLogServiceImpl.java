package com.medical360.service.impl;

import com.medical360.entity.AccessLog;
import com.medical360.mapper.AccessLogMapper;
import com.medical360.service.AccessLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessLogServiceImpl implements AccessLogService {

    private final AccessLogMapper accessLogMapper;

    @Override
    @Async
    public void logAccess(Long userId, Long patientId, String action, String requestDetail, String ipAddress) {
        AccessLog log = new AccessLog();
        log.setUserId(userId);
        log.setPatientId(patientId);
        log.setAction(action);
        log.setRequestDetail(requestDetail);
        log.setIpAddress(ipAddress);
        accessLogMapper.insert(log);
    }
}
