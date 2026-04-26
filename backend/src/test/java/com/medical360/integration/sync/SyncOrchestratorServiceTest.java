package com.medical360.integration.sync;

import com.medical360.entity.DataSyncLog;
import com.medical360.entity.Encounter;
import com.medical360.entity.Patient;
import com.medical360.integration.adapter.DataSourceAdapter;
import com.medical360.integration.mapper.DataStandardizer;
import com.medical360.integration.mapper.NormalizedSyncPayload;
import com.medical360.mapper.DataSyncLogMapper;
import com.medical360.mapper.EncounterMapper;
import com.medical360.mapper.PatientMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SyncOrchestratorServiceTest {

    @Mock
    private DataStandardizer dataStandardizer;
    @Mock
    private DataSyncLogMapper dataSyncLogMapper;
    @Mock
    private PatientMapper patientMapper;
    @Mock
    private EncounterMapper encounterMapper;
    @Mock
    private DataSourceAdapter lisAdapter;
    @Mock
    private DataSourceAdapter emrAdapter;

    @InjectMocks
    private SyncOrchestratorService orchestratorService;

    @Test
    void shouldLogSuccessWhenSourceSyncSucceeds() {
        when(lisAdapter.getSourceType()).thenReturn("LIS");
        when(lisAdapter.fetchLabOrders(null)).thenReturn(List.of(Map.of("LAB_NO", "LAB-1")));
        when(lisAdapter.fetchLabResults(null)).thenReturn(List.of());
        when(lisAdapter.fetchImagingOrders(null)).thenReturn(List.of());
        when(lisAdapter.fetchImagingReports(null)).thenReturn(List.of());
        when(lisAdapter.fetchPatients(null)).thenReturn(List.of());
        when(lisAdapter.fetchEncounters(null)).thenReturn(List.of());
        when(dataStandardizer.normalize(any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(NormalizedSyncPayload.empty());

        orchestratorService.syncSource(lisAdapter, null);

        ArgumentCaptor<DataSyncLog> captor = ArgumentCaptor.forClass(DataSyncLog.class);
        verify(dataSyncLogMapper).insert(captor.capture());
        assertThat(captor.getValue().getSourceType()).isEqualTo("LIS");
        assertThat(captor.getValue().getStatus()).isEqualTo("SUCCESS");
    }

    @Test
    void shouldLogFailedWhenSourceSyncThrows() {
        when(lisAdapter.getSourceType()).thenReturn("LIS");
        when(lisAdapter.fetchLabOrders(null)).thenThrow(new IllegalStateException("downstream unavailable"));

        orchestratorService.syncSource(lisAdapter, null);

        ArgumentCaptor<DataSyncLog> captor = ArgumentCaptor.forClass(DataSyncLog.class);
        verify(dataSyncLogMapper).insert(captor.capture());
        assertThat(captor.getValue().getSourceType()).isEqualTo("LIS");
        assertThat(captor.getValue().getStatus()).isEqualTo("FAILED");
        assertThat(captor.getValue().getErrorMessage()).contains("downstream unavailable");
    }

    @Test
    void shouldPersistPatientsBeforeEncountersForEmrSync() {
        when(emrAdapter.getSourceType()).thenReturn("EMR");
        when(emrAdapter.fetchLabOrders(null)).thenReturn(List.of());
        when(emrAdapter.fetchLabResults(null)).thenReturn(List.of());
        when(emrAdapter.fetchImagingOrders(null)).thenReturn(List.of());
        when(emrAdapter.fetchImagingReports(null)).thenReturn(List.of());
        when(emrAdapter.fetchPatients(null)).thenReturn(List.of(Map.of("PATIENT_ID", "11")));
        when(emrAdapter.fetchEncounters(null)).thenReturn(List.of(Map.of("ENCOUNTER_ID", "21")));
        when(dataStandardizer.normalize(any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(new NormalizedSyncPayload(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(Map.of(
                    "id", 11L,
                    "unifiedPatientId", "P11",
                    "name", "李四",
                    "gender", "F",
                    "birthDate", LocalDate.of(1985, 8, 1)
                )),
                List.of(Map.of(
                    "id", 21L,
                    "patientId", 11L,
                    "encounterType", "门诊",
                    "visitDatetime", LocalDateTime.of(2026, 4, 25, 9, 30)
                ))
            ));
        when(patientMapper.selectOne(any())).thenReturn(null);
        when(encounterMapper.selectOne(any())).thenReturn(null);
        doAnswer(invocation -> {
            Patient patient = invocation.getArgument(0);
            patient.setId(101L);
            return 1;
        }).when(patientMapper).insert(any(Patient.class));

        orchestratorService.syncSource(emrAdapter, null);

        InOrder inOrder = inOrder(patientMapper, encounterMapper, dataSyncLogMapper);
        ArgumentCaptor<Patient> patientCaptor = ArgumentCaptor.forClass(Patient.class);
        ArgumentCaptor<Encounter> encounterCaptor = ArgumentCaptor.forClass(Encounter.class);
        ArgumentCaptor<DataSyncLog> logCaptor = ArgumentCaptor.forClass(DataSyncLog.class);
        inOrder.verify(patientMapper).insert(patientCaptor.capture());
        inOrder.verify(encounterMapper).insert(encounterCaptor.capture());
        inOrder.verify(dataSyncLogMapper).insert(logCaptor.capture());
        assertThat(patientCaptor.getValue().getId()).isEqualTo(101L);
        assertThat(patientCaptor.getValue().getEmrPatientId()).isEqualTo(11L);
        assertThat(patientCaptor.getValue().getUnifiedPatientId()).isEqualTo("P11");
        assertThat(encounterCaptor.getValue().getId()).isNull();
        assertThat(encounterCaptor.getValue().getEmrEncounterId()).isEqualTo(21L);
        assertThat(encounterCaptor.getValue().getPatientId()).isEqualTo(101L);
        assertThat(logCaptor.getValue().getStatus()).isEqualTo("SUCCESS");
        assertThat(logCaptor.getValue().getSyncedCount()).isEqualTo(2);
    }

    @Test
    void shouldFallbackToExternalPatientIdWhenUnifiedIdIsMissing() {
        when(emrAdapter.getSourceType()).thenReturn("EMR");
        when(emrAdapter.fetchLabOrders(null)).thenReturn(List.of());
        when(emrAdapter.fetchLabResults(null)).thenReturn(List.of());
        when(emrAdapter.fetchImagingOrders(null)).thenReturn(List.of());
        when(emrAdapter.fetchImagingReports(null)).thenReturn(List.of());
        when(emrAdapter.fetchPatients(null)).thenReturn(List.of(Map.of("PATIENT_ID", "11")));
        when(emrAdapter.fetchEncounters(null)).thenReturn(List.of());
        when(dataStandardizer.normalize(any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(new NormalizedSyncPayload(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(Map.of("id", 11L, "name", "外部患者")),
                List.of()
            ));
        when(patientMapper.selectOne(any())).thenReturn(null);
        doAnswer(invocation -> {
            Patient patient = invocation.getArgument(0);
            patient.setId(101L);
            return 1;
        }).when(patientMapper).insert(any(Patient.class));

        orchestratorService.syncSource(emrAdapter, null);

        ArgumentCaptor<Patient> patientCaptor = ArgumentCaptor.forClass(Patient.class);
        ArgumentCaptor<DataSyncLog> logCaptor = ArgumentCaptor.forClass(DataSyncLog.class);
        verify(patientMapper).insert(patientCaptor.capture());
        verify(dataSyncLogMapper).insert(logCaptor.capture());
        assertThat(patientCaptor.getValue().getUnifiedPatientId()).isEqualTo("EMR-PATIENT-11");
        assertThat(patientCaptor.getValue().getEmrPatientId()).isEqualTo(11L);
        assertThat(logCaptor.getValue().getStatus()).isEqualTo("SUCCESS");
        assertThat(logCaptor.getValue().getSyncedCount()).isEqualTo(1);
    }

    @Test
    void shouldUpdateExistingEmrRecordsOnRepeatSync() {
        when(emrAdapter.getSourceType()).thenReturn("EMR");
        when(emrAdapter.fetchLabOrders(null)).thenReturn(List.of());
        when(emrAdapter.fetchLabResults(null)).thenReturn(List.of());
        when(emrAdapter.fetchImagingOrders(null)).thenReturn(List.of());
        when(emrAdapter.fetchImagingReports(null)).thenReturn(List.of());
        when(emrAdapter.fetchPatients(null)).thenReturn(List.of(Map.of("PATIENT_ID", "11")));
        when(emrAdapter.fetchEncounters(null)).thenReturn(List.of(Map.of("ENCOUNTER_ID", "21")));
        when(dataStandardizer.normalize(any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(new NormalizedSyncPayload(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(Map.of("id", 11L, "name", "更新后患者")),
                List.of(Map.of("id", 21L, "patientId", 11L, "encounterType", "住院"))
            ));
        Patient existingPatient = new Patient();
        existingPatient.setId(100L);
        existingPatient.setEmrPatientId(11L);
        existingPatient.setUnifiedPatientId("REAL-123");
        when(patientMapper.selectOne(any())).thenReturn(existingPatient);
        Encounter existingEncounter = new Encounter();
        existingEncounter.setId(200L);
        existingEncounter.setEmrEncounterId(21L);
        existingEncounter.setPatientId(100L);
        when(encounterMapper.selectOne(any())).thenReturn(existingEncounter);

        orchestratorService.syncSource(emrAdapter, null);

        verify(patientMapper, never()).insert(any(Patient.class));
        verify(encounterMapper, never()).insert(any(Encounter.class));
        ArgumentCaptor<Patient> patientCaptor = ArgumentCaptor.forClass(Patient.class);
        ArgumentCaptor<Encounter> encounterCaptor = ArgumentCaptor.forClass(Encounter.class);
        verify(patientMapper).updateById(patientCaptor.capture());
        verify(encounterMapper).updateById(encounterCaptor.capture());
        assertThat(patientCaptor.getValue().getId()).isEqualTo(100L);
        assertThat(patientCaptor.getValue().getEmrPatientId()).isEqualTo(11L);
        assertThat(patientCaptor.getValue().getUnifiedPatientId()).isEqualTo("REAL-123");
        assertThat(encounterCaptor.getValue().getId()).isEqualTo(200L);
        assertThat(encounterCaptor.getValue().getEmrEncounterId()).isEqualTo(21L);
        assertThat(encounterCaptor.getValue().getPatientId()).isEqualTo(100L);
    }

    @Test
    void shouldContinueSyncWhenMalformedExternalPatientIdIsEncountered() {
        when(emrAdapter.getSourceType()).thenReturn("EMR");
        when(emrAdapter.fetchLabOrders(null)).thenReturn(List.of());
        when(emrAdapter.fetchLabResults(null)).thenReturn(List.of());
        when(emrAdapter.fetchImagingOrders(null)).thenReturn(List.of());
        when(emrAdapter.fetchImagingReports(null)).thenReturn(List.of());
        when(emrAdapter.fetchPatients(null)).thenReturn(List.of(Map.of("PATIENT_ID", "abc"), Map.of("PATIENT_ID", "11")));
        when(emrAdapter.fetchEncounters(null)).thenReturn(List.of(Map.of("ENCOUNTER_ID", "21", "patientId", 11L)));
        when(dataStandardizer.normalize(any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(new NormalizedSyncPayload(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(
                    Map.of("id", "abc", "name", "坏数据"),
                    Map.of("id", 11L, "unifiedPatientId", "P11", "name", "李四")
                ),
                List.of(Map.of("id", 21L, "patientId", 11L, "encounterType", "门诊"))
            ));
        when(patientMapper.selectOne(any())).thenReturn(null);
        when(encounterMapper.selectOne(any())).thenReturn(null);
        doAnswer(invocation -> {
            Patient patient = invocation.getArgument(0);
            patient.setId(101L);
            return 1;
        }).when(patientMapper).insert(any(Patient.class));

        orchestratorService.syncSource(emrAdapter, null);

        verify(patientMapper, times(1)).insert(any(Patient.class));
        verify(encounterMapper).insert(any(Encounter.class));
        ArgumentCaptor<DataSyncLog> logCaptor = ArgumentCaptor.forClass(DataSyncLog.class);
        verify(dataSyncLogMapper).insert(logCaptor.capture());
        assertThat(logCaptor.getValue().getStatus()).isEqualTo("PARTIAL_FAILED");
        assertThat(logCaptor.getValue().getErrorMessage()).contains("externalId=abc");
        assertThat(logCaptor.getValue().getSyncedCount()).isEqualTo(2);
    }


    @Test
    void shouldRelinkEncounterToExistingPatientMatchedByUnifiedId() {
        when(emrAdapter.getSourceType()).thenReturn("EMR");
        when(emrAdapter.fetchLabOrders(null)).thenReturn(List.of());
        when(emrAdapter.fetchLabResults(null)).thenReturn(List.of());
        when(emrAdapter.fetchImagingOrders(null)).thenReturn(List.of());
        when(emrAdapter.fetchImagingReports(null)).thenReturn(List.of());
        when(emrAdapter.fetchPatients(null)).thenReturn(List.of(Map.of("PATIENT_ID", "11")));
        when(emrAdapter.fetchEncounters(null)).thenReturn(List.of(Map.of("ENCOUNTER_ID", "21")));
        when(dataStandardizer.normalize(any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(new NormalizedSyncPayload(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(Map.of("id", 11L, "unifiedPatientId", "P11", "name", "李四")),
                List.of(Map.of("id", 21L, "patientId", 11L, "encounterType", "门诊"))
            ));
        Patient existingPatient = new Patient();
        existingPatient.setId(99L);
        existingPatient.setUnifiedPatientId("P11");
        when(patientMapper.selectOne(any())).thenReturn(existingPatient);
        when(encounterMapper.selectOne(any())).thenReturn(null);

        orchestratorService.syncSource(emrAdapter, null);

        ArgumentCaptor<Patient> patientCaptor = ArgumentCaptor.forClass(Patient.class);
        ArgumentCaptor<Encounter> encounterCaptor = ArgumentCaptor.forClass(Encounter.class);
        verify(patientMapper).updateById(patientCaptor.capture());
        verify(encounterMapper).insert(encounterCaptor.capture());
        assertThat(patientCaptor.getValue().getId()).isEqualTo(99L);
        assertThat(patientCaptor.getValue().getEmrPatientId()).isEqualTo(11L);
        assertThat(encounterCaptor.getValue().getPatientId()).isEqualTo(99L);
    }

    @Test
    void shouldSkipEncounterWhenPatientWasNotPersisted() {
        when(emrAdapter.getSourceType()).thenReturn("EMR");
        when(emrAdapter.fetchLabOrders(null)).thenReturn(List.of());
        when(emrAdapter.fetchLabResults(null)).thenReturn(List.of());
        when(emrAdapter.fetchImagingOrders(null)).thenReturn(List.of());
        when(emrAdapter.fetchImagingReports(null)).thenReturn(List.of());
        when(emrAdapter.fetchPatients(null)).thenReturn(List.of());
        when(emrAdapter.fetchEncounters(null)).thenReturn(List.of(Map.of("ENCOUNTER_ID", "21")));
        when(dataStandardizer.normalize(any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(new NormalizedSyncPayload(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(Map.of("id", 21L, "patientId", 404L, "encounterType", "急诊"))
            ));
        when(patientMapper.selectOne(any())).thenReturn(null);

        orchestratorService.syncSource(emrAdapter, null);

        verify(encounterMapper, never()).insert(any(Encounter.class));
        verify(encounterMapper, never()).updateById(any(Encounter.class));
        ArgumentCaptor<DataSyncLog> logCaptor = ArgumentCaptor.forClass(DataSyncLog.class);
        verify(dataSyncLogMapper).insert(logCaptor.capture());
        assertThat(logCaptor.getValue().getStatus()).isEqualTo("FAILED");
        assertThat(logCaptor.getValue().getSyncedCount()).isZero();
        assertThat(logCaptor.getValue().getErrorMessage()).contains("patient is missing");
    }

    @Test
    void shouldSkipEncounterWithoutExternalId() {
        when(emrAdapter.getSourceType()).thenReturn("EMR");
        when(emrAdapter.fetchLabOrders(null)).thenReturn(List.of());
        when(emrAdapter.fetchLabResults(null)).thenReturn(List.of());
        when(emrAdapter.fetchImagingOrders(null)).thenReturn(List.of());
        when(emrAdapter.fetchImagingReports(null)).thenReturn(List.of());
        when(emrAdapter.fetchPatients(null)).thenReturn(List.of());
        when(emrAdapter.fetchEncounters(null)).thenReturn(List.of(Map.of("patientId", 11L, "encounterType", "门诊")));
        when(dataStandardizer.normalize(any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(new NormalizedSyncPayload(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(Map.of("patientId", 11L, "encounterType", "门诊"))
            ));

        orchestratorService.syncSource(emrAdapter, null);

        verify(encounterMapper, never()).insert(any(Encounter.class));
        verify(encounterMapper, never()).updateById(any(Encounter.class));
        ArgumentCaptor<DataSyncLog> logCaptor = ArgumentCaptor.forClass(DataSyncLog.class);
        verify(dataSyncLogMapper).insert(logCaptor.capture());
        assertThat(logCaptor.getValue().getStatus()).isEqualTo("FAILED");
        assertThat(logCaptor.getValue().getSyncedCount()).isZero();
        assertThat(logCaptor.getValue().getErrorMessage()).contains("missing external encounter id");
    }

    @Test
    void shouldReportPartialFailureWithActualSyncedCountForEmrSync() {
        when(emrAdapter.getSourceType()).thenReturn("EMR");
        when(emrAdapter.fetchLabOrders(null)).thenReturn(List.of());
        when(emrAdapter.fetchLabResults(null)).thenReturn(List.of());
        when(emrAdapter.fetchImagingOrders(null)).thenReturn(List.of());
        when(emrAdapter.fetchImagingReports(null)).thenReturn(List.of());
        when(emrAdapter.fetchPatients(null)).thenReturn(List.of(Map.of("PATIENT_ID", "11"), Map.of("PATIENT_ID", "12")));
        when(emrAdapter.fetchEncounters(null)).thenReturn(List.of(Map.of("ENCOUNTER_ID", "21")));
        when(dataStandardizer.normalize(any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(new NormalizedSyncPayload(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(
                    Map.of("id", 11L, "unifiedPatientId", "P11", "name", "李四"),
                    Map.of("id", 12L, "unifiedPatientId", "P12", "name", "坏数据")
                ),
                List.of(Map.of("id", 21L, "patientId", 11L, "encounterType", "门诊"))
            ));
        when(patientMapper.selectOne(any())).thenReturn(null);
        when(encounterMapper.selectOne(any())).thenReturn(null);
        doAnswer(invocation -> {
            Patient patient = invocation.getArgument(0);
            if (Long.valueOf(12L).equals(patient.getEmrPatientId())) {
                throw new IllegalStateException("duplicate key on patient");
            }
            patient.setId(101L);
            return 1;
        }).when(patientMapper).insert(any(Patient.class));

        orchestratorService.syncSource(emrAdapter, null);

        verify(patientMapper, times(2)).insert(any(Patient.class));
        verify(encounterMapper).insert(any(Encounter.class));
        ArgumentCaptor<DataSyncLog> logCaptor = ArgumentCaptor.forClass(DataSyncLog.class);
        verify(dataSyncLogMapper).insert(logCaptor.capture());
        assertThat(logCaptor.getValue().getStatus()).isEqualTo("PARTIAL_FAILED");
        assertThat(logCaptor.getValue().getSyncedCount()).isEqualTo(2);
        assertThat(logCaptor.getValue().getErrorMessage()).contains("duplicate key on patient");
    }
}
