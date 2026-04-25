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
import java.util.ArrayList;
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

            if ("EMR".equals(sourceType)) {
                EmrSyncResult result = syncEmrPayload(payload);
                String status = result.errors().isEmpty()
                    ? "SUCCESS"
                    : result.syncedCount() > 0 ? "PARTIAL_FAILED" : "FAILED";
                String errorMessage = summarizeErrors(result.errors());

                if (result.errors().isEmpty()) {
                    log.info("source={} sync succeeded, synced records={}", sourceType, result.syncedCount());
                } else {
                    log.warn(
                        "source={} sync completed with status={}, synced records={}, errors={}",
                        sourceType,
                        status,
                        result.syncedCount(),
                        result.errors().size()
                    );
                }
                saveLog(sourceType, status, errorMessage, lastSyncTime, result.syncedCount());
                return;
            }

            int syncedCount = countNormalizedRecords(payload);
            log.info("source={} sync succeeded, normalized records={}", sourceType, syncedCount);
            saveLog(sourceType, "SUCCESS", null, lastSyncTime, syncedCount);
        } catch (Exception e) {
            log.error("source={} sync failed", sourceType, e);
            saveLog(sourceType, "FAILED", e.getMessage(), lastSyncTime, 0);
        }
    }

    private EmrSyncResult syncEmrPayload(NormalizedSyncPayload payload) {
        List<String> errors = new ArrayList<>();
        PatientSyncResult patientSyncResult = syncPatients(payload.patients(), errors);
        int syncedCount = patientSyncResult.syncedCount()
            + syncEncounters(payload.encounters(), patientSyncResult.patientIdMappings(), errors);
        return new EmrSyncResult(syncedCount, errors);
    }

    private int countNormalizedRecords(NormalizedSyncPayload payload) {
        return payload.labOrders().size() +
            payload.labResults().size() +
            payload.imagingOrders().size() +
            payload.imagingReports().size() +
            payload.patients().size() +
            payload.encounters().size();
    }

    private PatientSyncResult syncPatients(List<Map<String, Object>> patients, List<String> errors) {
        Map<Long, Long> patientIdMappings = new HashMap<>();
        int syncedCount = 0;
        for (Map<String, Object> record : patients) {
            try {
                Patient patient = toPatient(record);
                Long sourcePatientId = patient.getEmrPatientId();
                Patient existing = findExistingPatient(patient);
                if (existing == null) {
                    patient.setId(null);
                    patientMapper.insert(patient);
                    if (sourcePatientId != null && patient.getId() != null) {
                        patientIdMappings.put(sourcePatientId, patient.getId());
                    }
                } else {
                    patient.setId(existing.getId());
                    patientMapper.updateById(patient);
                    if (sourcePatientId != null) {
                        patientIdMappings.put(sourcePatientId, existing.getId());
                    }
                }
                syncedCount++;
            } catch (Exception e) {
                Long sourcePatientId = getLong(record, "id");
                log.warn("Skipping EMR patient sync, patientId={}, reason={}", sourcePatientId, e.getMessage());
                errors.add("patient externalId=" + sourcePatientId + ": " + e.getMessage());
            }
        }
        return new PatientSyncResult(patientIdMappings, syncedCount);
    }

    private Patient findExistingPatient(Patient patient) {
        if (patient.getEmrPatientId() != null) {
            Patient existing = patientMapper.selectOne(
                new LambdaQueryWrapper<Patient>()
                    .eq(Patient::getEmrPatientId, patient.getEmrPatientId())
            );
            if (existing != null) {
                return existing;
            }
        }
        if (hasText(patient.getUnifiedPatientId())) {
            Patient existing = patientMapper.selectOne(
                new LambdaQueryWrapper<Patient>()
                    .eq(Patient::getUnifiedPatientId, patient.getUnifiedPatientId())
            );
            if (existing != null) {
                return existing;
            }
        }
        return null;
    }

    private int syncEncounters(List<Map<String, Object>> encounters, Map<Long, Long> patientIdMappings, List<String> errors) {
        int syncedCount = 0;
        for (Map<String, Object> record : encounters) {
            try {
                Encounter encounter = toEncounter(record);
                Long sourceEncounterId = encounter.getEmrEncounterId();
                Long sourcePatientId = encounter.getPatientId();
                encounter.setPatientId(resolvePatientId(sourcePatientId, patientIdMappings));
                if (encounter.getPatientId() == null) {
                    String error = "encounter externalId=" + sourceEncounterId + ": patient is missing for external patientId=" + sourcePatientId;
                    log.warn("Skipping EMR encounter sync because patient is missing, encounterId={}, patientId={}", sourceEncounterId, sourcePatientId);
                    errors.add(error);
                    continue;
                }

                Encounter existing = findExistingEncounter(encounter);
                if (existing == null) {
                    encounter.setId(null);
                    encounterMapper.insert(encounter);
                } else {
                    encounter.setId(existing.getId());
                    encounterMapper.updateById(encounter);
                }
                syncedCount++;
            } catch (Exception e) {
                Long sourceEncounterId = getLong(record, "id");
                log.warn("Skipping EMR encounter sync, encounterId={}, reason={}", sourceEncounterId, e.getMessage());
                errors.add("encounter externalId=" + sourceEncounterId + ": " + e.getMessage());
            }
        }
        return syncedCount;
    }

    private Encounter findExistingEncounter(Encounter encounter) {
        if (encounter.getEmrEncounterId() == null) {
            return null;
        }
        return encounterMapper.selectOne(
            new LambdaQueryWrapper<Encounter>()
                .eq(Encounter::getEmrEncounterId, encounter.getEmrEncounterId())
        );
    }

    private Long resolvePatientId(Long emrPatientId, Map<Long, Long> patientIdMappings) {
        if (emrPatientId == null) {
            return null;
        }
        Long mappedPatientId = patientIdMappings.get(emrPatientId);
        if (mappedPatientId != null) {
            return mappedPatientId;
        }
        Patient existing = patientMapper.selectOne(
            new LambdaQueryWrapper<Patient>()
                .eq(Patient::getEmrPatientId, emrPatientId)
        );
        return existing == null ? null : existing.getId();
    }

    private Patient toPatient(Map<String, Object> record) {
        Patient patient = new Patient();
        patient.setEmrPatientId(getLong(record, "id"));
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
        encounter.setEmrEncounterId(getLong(record, "id"));
        encounter.setPatientId(getLong(record, "patientId"));
        encounter.setEncounterType(getString(record, "encounterType"));
        encounter.setDepartmentId(getLong(record, "departmentId"));
        encounter.setVisitDatetime((LocalDateTime) record.get("visitDatetime"));
        encounter.setAdmissionReason(getString(record, "admissionReason"));
        return encounter;
    }

    private String summarizeErrors(List<String> errors) {
        if (errors.isEmpty()) {
            return null;
        }
        return String.join("; ", errors.stream().limit(5).toList());
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

    private record PatientSyncResult(Map<Long, Long> patientIdMappings, int syncedCount) {
    }

    private record EmrSyncResult(int syncedCount, List<String> errors) {
    }
}
