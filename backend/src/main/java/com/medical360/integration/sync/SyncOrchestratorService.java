package com.medical360.integration.sync;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.medical360.entity.DataSyncLog;
import com.medical360.entity.Encounter;
import com.medical360.entity.Patient;
import com.medical360.integration.adapter.DataSourceAdapter;
import com.medical360.integration.mapper.DataStandardizer;
import com.medical360.integration.mapper.NormalizedSyncPayload;
import com.medical360.mapper.DataSyncLogMapper;
import com.medical360.mapper.EncounterMapper;
import com.medical360.mapper.PatientMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncOrchestratorService {

    private final DataStandardizer dataStandardizer;
    private final DataSyncLogMapper dataSyncLogMapper;
    private final PatientMapper patientMapper;
    private final EncounterMapper encounterMapper;

    public void syncSource(DataSourceAdapter adapter, String lastSyncTime) {
        String sourceType = adapter.getSourceType();
        try {
            NormalizedSyncPayload payload = dataStandardizer.normalize(
                sourceType,
                adapter.fetchLabOrders(lastSyncTime),
                adapter.fetchLabResults(lastSyncTime),
                adapter.fetchImagingOrders(lastSyncTime),
                adapter.fetchImagingReports(lastSyncTime),
                adapter.fetchPatients(lastSyncTime),
                adapter.fetchEncounters(lastSyncTime)
            );

            int syncedCount = "EMR".equals(sourceType)
                ? syncEmrPayload(payload)
                : countNormalizedRecords(payload);

            log.info("source={} sync succeeded, normalized records={}", sourceType, syncedCount);
            saveLog(sourceType, "SUCCESS", null, lastSyncTime, syncedCount);
        } catch (Exception e) {
            log.error("source={} sync failed", sourceType, e);
            saveLog(sourceType, "FAILED", e.getMessage(), lastSyncTime, 0);
        }
    }

    private int syncEmrPayload(NormalizedSyncPayload payload) {
        int syncedCount =
            payload.labOrders().size() +
                payload.labResults().size() +
                payload.imagingOrders().size() +
                payload.imagingReports().size();

        Map<Long, Long> patientIdMappings = syncPatients(payload.patients());
        syncedCount += payload.patients().size();
        syncedCount += syncEncounters(payload.encounters(), patientIdMappings);
        return syncedCount;
    }

    private int countNormalizedRecords(NormalizedSyncPayload payload) {
        return payload.labOrders().size() +
            payload.labResults().size() +
            payload.imagingOrders().size() +
            payload.imagingReports().size() +
            payload.patients().size() +
            payload.encounters().size();
    }

    private Map<Long, Long> syncPatients(List<Map<String, Object>> patients) {
        Map<Long, Long> patientIdMappings = new HashMap<>();
        for (Map<String, Object> record : patients) {
            Patient patient = toPatient(record);
            Long sourcePatientId = patient.getId();
            Patient existing = findExistingPatient(patient);
            if (existing == null) {
                patientMapper.insert(patient);
                if (sourcePatientId != null && patient.getId() != null) {
                    patientIdMappings.put(sourcePatientId, patient.getId());
                }
                continue;
            }

            patient.setId(existing.getId());
            patientMapper.updateById(patient);
            if (sourcePatientId != null) {
                patientIdMappings.put(sourcePatientId, existing.getId());
            }
        }
        return patientIdMappings;
    }

    private Patient findExistingPatient(Patient patient) {
        if (hasText(patient.getUnifiedPatientId())) {
            Patient existing = patientMapper.selectOne(
                new LambdaQueryWrapper<Patient>()
                    .eq(Patient::getUnifiedPatientId, patient.getUnifiedPatientId())
            );
            if (existing != null) {
                return existing;
            }
        }
        return patient.getId() == null ? null : patientMapper.selectById(patient.getId());
    }

    private int syncEncounters(List<Map<String, Object>> encounters, Map<Long, Long> patientIdMappings) {
        int syncedCount = 0;
        for (Map<String, Object> record : encounters) {
            Encounter encounter = toEncounter(record);
            Long sourcePatientId = encounter.getPatientId();
            encounter.setPatientId(resolvePatientId(sourcePatientId, patientIdMappings));
            boolean patientResolvedInSync = sourcePatientId != null && patientIdMappings.containsKey(sourcePatientId);
            if (encounter.getPatientId() == null || (!patientResolvedInSync && patientMapper.selectById(encounter.getPatientId()) == null)) {
                log.warn("Skipping EMR encounter sync because patient is missing, encounterId={}, patientId={}", encounter.getId(), encounter.getPatientId());
                continue;
            }

            Encounter existing = encounter.getId() == null ? null : encounterMapper.selectById(encounter.getId());
            if (existing == null) {
                encounterMapper.insert(encounter);
            } else {
                encounter.setId(existing.getId());
                encounterMapper.updateById(encounter);
            }
            syncedCount++;
        }
        return syncedCount;
    }

    private Long resolvePatientId(Long patientId, Map<Long, Long> patientIdMappings) {
        if (patientId == null) {
            return null;
        }
        return patientIdMappings.getOrDefault(patientId, patientId);
    }

    private Patient toPatient(Map<String, Object> record) {
        Patient patient = new Patient();
        patient.setId(getLong(record, "id"));
        patient.setUnifiedPatientId(getString(record, "unifiedPatientId"));
        patient.setName(getString(record, "name"));
        patient.setGender(getString(record, "gender"));
        patient.setBirthDate((LocalDate) record.get("birthDate"));
        patient.setIdCardNo(getString(record, "idCardNo"));
        patient.setPhone(getString(record, "phone"));
        patient.setAddress(getString(record, "address"));
        return patient;
    }

    private Encounter toEncounter(Map<String, Object> record) {
        Encounter encounter = new Encounter();
        encounter.setId(getLong(record, "id"));
        encounter.setPatientId(getLong(record, "patientId"));
        encounter.setEncounterType(getString(record, "encounterType"));
        encounter.setDepartmentId(getLong(record, "departmentId"));
        encounter.setVisitDatetime((LocalDateTime) record.get("visitDatetime"));
        encounter.setAdmissionReason(getString(record, "admissionReason"));
        return encounter;
    }

    private Long getLong(Map<String, Object> record, String key) {
        Object value = record.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        return Long.parseLong(value.toString());
    }

    private String getString(Map<String, Object> record, String key) {
        Object value = record.get(key);
        return value == null ? null : value.toString();
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private void saveLog(String sourceType, String status, String error, String syncTime, int syncedCount) {
        DataSyncLog logRecord = new DataSyncLog();
        logRecord.setSourceType(sourceType);
        logRecord.setDataType("ALL");
        logRecord.setLastSyncTime(syncTime);
        logRecord.setStatus(status);
        logRecord.setErrorMessage(error);
        logRecord.setSyncedCount(syncedCount);
        dataSyncLogMapper.insert(logRecord);
    }
}
