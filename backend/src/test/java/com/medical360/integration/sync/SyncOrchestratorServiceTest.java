package com.medical360.integration.sync;

import com.medical360.entity.DataSyncLog;
import com.medical360.integration.adapter.DataSourceAdapter;
import com.medical360.integration.mapper.DataStandardizer;
import com.medical360.integration.mapper.NormalizedSyncPayload;
import com.medical360.mapper.DataSyncLogMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SyncOrchestratorServiceTest {

    @Mock
    private DataStandardizer dataStandardizer;
    @Mock
    private DataSyncLogMapper dataSyncLogMapper;
    @Mock
    private DataSourceAdapter lisAdapter;

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
}
