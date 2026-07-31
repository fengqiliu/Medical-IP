package com.medical360.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.medical360.dto.AccessLogDTO;
import com.medical360.entity.AccessLog;
import com.medical360.entity.Patient;
import com.medical360.entity.User;
import com.medical360.mapper.AccessLogMapper;
import com.medical360.mapper.PatientMapper;
import com.medical360.mapper.UserMapper;
import com.medical360.service.AccessLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccessLogServiceImpl implements AccessLogService {

    private final AccessLogMapper accessLogMapper;
    private final UserMapper userMapper;
    private final PatientMapper patientMapper;

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

    @Override
    public List<AccessLogDTO> listAccessLogs(
        Long userId,
        Long patientId,
        String action,
        String startDate,
        String endDate,
        int page,
        int size
    ) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.min(Math.max(size, 1), 200);
        long offset = (long) (safePage - 1) * safeSize;

        LambdaQueryWrapper<AccessLog> wrapper = buildFilter(userId, patientId, action, startDate, endDate)
            .orderByDesc(AccessLog::getAccessDatetime)
            .last("LIMIT " + safeSize + " OFFSET " + offset);
        List<AccessLog> logs = accessLogMapper.selectList(wrapper);

        Set<Long> userIds = logs.stream()
            .map(AccessLog::getUserId)
            .filter(id -> id != null)
            .collect(Collectors.toSet());
        Map<Long, User> usersById = userIds.isEmpty()
            ? Map.of()
            : userMapper.selectBatchIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        Set<Long> patientIds = logs.stream()
            .map(AccessLog::getPatientId)
            .filter(id -> id != null)
            .collect(Collectors.toSet());
        Map<Long, Patient> patientsById = patientIds.isEmpty()
            ? Map.of()
            : patientMapper.selectBatchIds(patientIds).stream()
                .collect(Collectors.toMap(Patient::getId, Function.identity()));

        return logs.stream()
            .map(log -> toDTO(log, usersById, patientsById))
            .toList();
    }

    @Override
    public long countAccessLogs(Long userId, Long patientId, String action, String startDate, String endDate) {
        return accessLogMapper.selectCount(buildFilter(userId, patientId, action, startDate, endDate));
    }

    private LambdaQueryWrapper<AccessLog> buildFilter(
        Long userId,
        Long patientId,
        String action,
        String startDate,
        String endDate
    ) {
        LambdaQueryWrapper<AccessLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(userId != null, AccessLog::getUserId, userId)
            .eq(patientId != null, AccessLog::getPatientId, patientId)
            .like(hasText(action), AccessLog::getAction, action);
        if (hasText(startDate)) {
            wrapper.ge(AccessLog::getAccessDatetime, parseDateTime(startDate, false));
        }
        if (hasText(endDate)) {
            wrapper.le(AccessLog::getAccessDatetime, parseDateTime(endDate, true));
        }
        return wrapper;
    }

    private LocalDateTime parseDateTime(String value, boolean endOfDay) {
        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException ignored) {
            try {
                LocalDate date = LocalDate.parse(value);
                return endOfDay ? date.atTime(LocalTime.MAX) : date.atStartOfDay();
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date value: " + value, e);
            }
        }
    }

    private AccessLogDTO toDTO(AccessLog log, Map<Long, User> usersById, Map<Long, Patient> patientsById) {
        AccessLogDTO dto = new AccessLogDTO();
        dto.setId(log.getId());
        dto.setUserId(log.getUserId());
        User user = log.getUserId() != null ? usersById.get(log.getUserId()) : null;
        dto.setUsername(user != null ? user.getUsername() : null);
        dto.setPatientId(log.getPatientId());
        Patient patient = log.getPatientId() != null ? patientsById.get(log.getPatientId()) : null;
        dto.setPatientName(patient != null ? patient.getName() : null);
        dto.setAction(log.getAction());
        dto.setRequestDetail(log.getRequestDetail());
        dto.setIpAddress(log.getIpAddress());
        dto.setAccessDatetime(log.getAccessDatetime());
        return dto;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
