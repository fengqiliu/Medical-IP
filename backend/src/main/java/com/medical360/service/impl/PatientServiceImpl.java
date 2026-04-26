package com.medical360.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.medical360.dto.*;
import com.medical360.entity.*;
import com.medical360.mapper.*;
import com.medical360.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientMapper patientMapper;
    private final EncounterMapper encounterMapper;
    private final LabOrderMapper labOrderMapper;
    private final LabResultMapper labResultMapper;
    private final ImagingOrderMapper imagingOrderMapper;
    private final DepartmentMapper departmentMapper;

    @Override
    public Patient360DTO getPatient360(Long patientId) {
        Patient360DTO dto = new Patient360DTO();

        Patient patient = patientMapper.selectById(patientId);
        if (patient != null) {
            Patient360DTO.PatientDTO p = new Patient360DTO.PatientDTO();
            p.setId(patient.getId());
            p.setUnifiedPatientId(patient.getUnifiedPatientId());
            p.setName(patient.getName());
            p.setGender(patient.getGender());
            p.setBirthDate(patient.getBirthDate() != null ? patient.getBirthDate().toString() : null);
            dto.setPatient(p);
        }

        Encounter encounter = encounterMapper.selectOne(
            new LambdaQueryWrapper<Encounter>()
                .eq(Encounter::getPatientId, patientId)
                .orderByDesc(Encounter::getVisitDatetime)
                .last("LIMIT 1")
        );
        if (encounter != null) {
            Patient360DTO.EncounterDTO e = new Patient360DTO.EncounterDTO();
            e.setId(encounter.getId());
            e.setEncounterType(encounter.getEncounterType());
            e.setVisitDatetime(encounter.getVisitDatetime() != null ? encounter.getVisitDatetime().toString() : null);
            e.setAdmissionReason(encounter.getAdmissionReason());
            if (encounter.getDepartmentId() != null) {
                Department dept = departmentMapper.selectById(encounter.getDepartmentId());
                if (dept != null) e.setDepartmentName(dept.getName());
            }
            dto.setEncounter(e);
        }

        List<LabOrder> labOrders = labOrderMapper.selectList(
            new LambdaQueryWrapper<LabOrder>()
                .eq(LabOrder::getPatientId, patientId)
                .orderByDesc(LabOrder::getOrderDatetime)
                .last("LIMIT 5")
        );

        Patient360DTO.LabSummaryDTO labSummary = new Patient360DTO.LabSummaryDTO();
        labSummary.setRecentCount(labOrderMapper.selectCount(new LambdaQueryWrapper<LabOrder>().eq(LabOrder::getPatientId, patientId)).intValue());

        long abnormalCount = 0;
        for (LabOrder order : labOrders) {
            abnormalCount += labResultMapper.selectCount(
                new LambdaQueryWrapper<LabResult>()
                    .eq(LabResult::getLabOrderId, order.getId())
                    .isNotNull(LabResult::getAbnormalFlag)
                    .ne(LabResult::getAbnormalFlag, "")
            );
        }
        labSummary.setAbnormalCount((int) abnormalCount);
        labSummary.setRecentOrders(labOrders.stream().map(o -> {
            Patient360DTO.LabOrderDTO lo = new Patient360DTO.LabOrderDTO();
            lo.setId(o.getId());
            lo.setOrderNo(o.getOrderNo());
            lo.setOrderDatetime(o.getOrderDatetime() != null ? o.getOrderDatetime().toString() : null);
            lo.setSpecimenType(o.getSpecimenType());
            lo.setStatus(o.getStatus());
            if (o.getDepartmentId() != null) {
                Department dept = departmentMapper.selectById(o.getDepartmentId());
                if (dept != null) lo.setDepartmentName(dept.getName());
            }
            return lo;
        }).collect(Collectors.toList()));
        dto.setLabSummary(labSummary);

        Patient360DTO.ImagingSummaryDTO imagingSummary = new Patient360DTO.ImagingSummaryDTO();
        imagingSummary.setRecentCount(imagingOrderMapper.selectCount(new LambdaQueryWrapper<ImagingOrder>().eq(ImagingOrder::getPatientId, patientId)).intValue());
        List<ImagingOrder> imagingOrders = imagingOrderMapper.selectList(
            new LambdaQueryWrapper<ImagingOrder>()
                .eq(ImagingOrder::getPatientId, patientId)
                .orderByDesc(ImagingOrder::getOrderDatetime)
                .last("LIMIT 5")
        );
        imagingSummary.setRecentOrders(imagingOrders.stream().map(o -> {
            Patient360DTO.ImagingOrderDTO io = new Patient360DTO.ImagingOrderDTO();
            io.setId(o.getId());
            io.setOrderNo(o.getOrderNo());
            io.setOrderDatetime(o.getOrderDatetime() != null ? o.getOrderDatetime().toString() : null);
            io.setBodyPart(o.getBodyPart());
            io.setModality(o.getModality());
            io.setStatus(o.getStatus());
            if (o.getDepartmentId() != null) {
                Department dept = departmentMapper.selectById(o.getDepartmentId());
                if (dept != null) io.setDepartmentName(dept.getName());
            }
            return io;
        }).collect(Collectors.toList()));
        dto.setImagingSummary(imagingSummary);

        return dto;
    }
}