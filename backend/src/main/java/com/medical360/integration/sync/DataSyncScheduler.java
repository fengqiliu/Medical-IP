package com.medical360.integration.sync;

import com.medical360.integration.adapter.DataSourceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSyncScheduler {

    private final List<DataSourceAdapter> adapters;
    private final SyncOrchestratorService syncOrchestratorService;
    private String lastSyncTime;

    @Scheduled(cron = "0 */5 * * * ?")
    public void syncData() {
        for (DataSourceAdapter adapter : adapters) {
            syncOrchestratorService.syncSource(adapter, lastSyncTime);
        }
        lastSyncTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
