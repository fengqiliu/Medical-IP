package com.medical360.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.medical360.dto.TimelineEventDTO;
import com.medical360.entity.ClinicalEvent;
import com.medical360.entity.Department;
import com.medical360.entity.LabResult;
import com.medical360.mapper.ClinicalEventMapper;
import com.medical360.mapper.DepartmentMapper;
import com.medical360.mapper.LabResultMapper;
import com.medical360.service.TimelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimelineServiceImpl implements TimelineService {

    private final ClinicalEventMapper clinicalEventMapper;
    private final DepartmentMapper departmentMapper;
    private final LabResultMapper labResultMapper;

    @Override
    public List<TimelineEventDTO> getTimeline(Long patientId) {
        List<ClinicalEvent> events = clinicalEventMapper.selectList(
            new LambdaQueryWrapper<ClinicalEvent>()
                .eq(ClinicalEvent::getPatientId, patientId)
                .orderByDesc(ClinicalEvent::getEventDatetime)
        );

        return events.stream().map(e -> {
            TimelineEventDTO dto = new TimelineEventDTO();
            dto.setId(e.getId());
            dto.setEventType(e.getEventType());
            dto.setEventDatetime(e.getEventDatetime() != null ? e.getEventDatetime().toString() : null);
            dto.setEventSummary(e.getEventSummary());
            dto.setEventStatus(e.getEventStatus());
            dto.setReferenceId(e.getReferenceId());
            if (e.getDepartmentId() != null) {
                Department dept = departmentMapper.selectById(e.getDepartmentId());
                if (dept != null) dto.setDepartmentName(dept.getName());
            }
            if ("lab".equals(e.getEventType()) && e.getReferenceId() != null) {
                List<LabResult> results = labResultMapper.selectList(
                    new LambdaQueryWrapper<LabResult>()
                        .eq(LabResult::getLabOrderId, e.getReferenceId())
                        .isNotNull(LabResult::getAbnormalFlag)
                        .ne(LabResult::getAbnormalFlag, "")
                );
                dto.setAbnormal(!results.isEmpty());
            }
            return dto;
        }).collect(Collectors.toList());
    }
}