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
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
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
        when(encounterMapper.selectById(21L)).thenReturn(null);

        orchestratorService.syncSource(emrAdapter, null);

        InOrder inOrder = inOrder(patientMapper, encounterMapper, dataSyncLogMapper);
        ArgumentCaptor<Patient> patientCaptor = ArgumentCaptor.forClass(Patient.class);
        ArgumentCaptor<Encounter> encounterCaptor = ArgumentCaptor.forClass(Encounter.class);
        ArgumentCaptor<DataSyncLog> logCaptor = ArgumentCaptor.forClass(DataSyncLog.class);
        inOrder.verify(patientMapper).insert(patientCaptor.capture());
        inOrder.verify(encounterMapper).insert(encounterCaptor.capture());
        inOrder.verify(dataSyncLogMapper).insert(logCaptor.capture());
        assertThat(patientCaptor.getValue().getId()).isEqualTo(11L);
        assertThat(patientCaptor.getValue().getUnifiedPatientId()).isEqualTo("P11");
        assertThat(encounterCaptor.getValue().getId()).isEqualTo(21L);
        assertThat(encounterCaptor.getValue().getPatientId()).isEqualTo(11L);
        assertThat(logCaptor.getValue().getStatus()).isEqualTo("SUCCESS");
        assertThat(logCaptor.getValue().getSyncedCount()).isEqualTo(2);
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
                List.of(Map.of("id", 11L, "unifiedPatientId", "P11", "name", "更新后患者")),
                List.of(Map.of("id", 21L, "patientId", 11L, "encounterType", "住院"))
            ));
        Patient existingPatient = new Patient();
        existingPatient.setId(11L);
        existingPatient.setUnifiedPatientId("P11");
        when(patientMapper.selectOne(any())).thenReturn(existingPatient);
        Encounter existingEncounter = new Encounter();
        existingEncounter.setId(21L);
        existingEncounter.setPatientId(11L);
        when(encounterMapper.selectById(21L)).thenReturn(existingEncounter);

        orchestratorService.syncSource(emrAdapter, null);

        verify(patientMapper, never()).insert(any(Patient.class));
        verify(encounterMapper, never()).insert(any(Encounter.class));
        verify(patientMapper).updateById(any(Patient.class));
        verify(encounterMapper).updateById(any(Encounter.class));
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
        when(encounterMapper.selectById(21L)).thenReturn(null);

        orchestratorService.syncSource(emrAdapter, null);

        ArgumentCaptor<Encounter> encounterCaptor = ArgumentCaptor.forClass(Encounter.class);
        verify(encounterMapper).insert(encounterCaptor.capture());
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
        when(patientMapper.selectById(404L)).thenReturn(null);

        orchestratorService.syncSource(emrAdapter, null);

        verify(encounterMapper, never()).insert(any(Encounter.class));
        verify(encounterMapper, never()).updateById(any(Encounter.class));
        ArgumentCaptor<DataSyncLog> logCaptor = ArgumentCaptor.forClass(DataSyncLog.class);
        verify(dataSyncLogMapper).insert(logCaptor.capture());
        assertThat(logCaptor.getValue().getStatus()).isEqualTo("SUCCESS");
        assertThat(logCaptor.getValue().getSyncedCount()).isZero();
    }
}
