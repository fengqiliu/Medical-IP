package com.medical360.integration.sync;

import com.medical360.integration.adapter.DataSourceAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSyncScheduler {

    private final List<DataSourceAdapter> adapters;
    private String lastSyncTime = null;

    @Scheduled(cron = "0 */5 * * * ?")
    public void syncData() {
        log.info("开始数据同步...");
        for (DataSourceAdapter adapter : adapters) {
            try {
                syncLabData(adapter);
                syncImagingData(adapter);
            } catch (Exception e) {
                log.error("同步 {} 数据失败: {}", adapter.getSourceType(), e.getMessage());
            }
        }
        lastSyncTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        log.info("数据同步完成，上次同步时间: {}", lastSyncTime);
    }

    private void syncLabData(DataSourceAdapter adapter) {
        List<Map<String, Object>> orders = adapter.fetchLabOrders(lastSyncTime);
        log.info("从 {} 获取 {} 条检验单", adapter.getSourceType(), orders.size());

        for (Map<String, Object> order : orders) {
            Map<String, Object> mapped = adapter.mapLabOrderFields(order);
            // TODO: Save to database
        }
    }

    private void syncImagingData(DataSourceAdapter adapter) {
        List<Map<String, Object>> orders = adapter.fetchImagingOrders(lastSyncTime);
        log.info("从 {} 获取 {} 条影像检查单", adapter.getSourceType(), orders.size());
    }
}
