package com.medical360.integration.sync;

import com.medical360.entity.DataSyncLog;
import com.medical360.integration.adapter.DataSourceAdapter;
import com.medical360.integration.mapper.DataStandardizer;
import com.medical360.integration.mapper.NormalizedSyncPayload;
import com.medical360.mapper.DataSyncLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncOrchestratorService {

    private final DataStandardizer dataStandardizer;
    private final DataSyncLogMapper dataSyncLogMapper;

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

            int syncedCount =
                payload.labOrders().size() +
                    payload.labResults().size() +
                    payload.imagingOrders().size() +
                    payload.imagingReports().size() +
                    payload.patients().size() +
                    payload.encounters().size();

            log.info("source={} sync succeeded, normalized records={}", sourceType, syncedCount);
            saveLog(sourceType, "SUCCESS", null, lastSyncTime, syncedCount);
        } catch (Exception e) {
            log.error("source={} sync failed", sourceType, e);
            saveLog(sourceType, "FAILED", e.getMessage(), lastSyncTime, 0);
        }
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
