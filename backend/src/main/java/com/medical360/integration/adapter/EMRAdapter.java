package com.medical360.integration.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class EMRAdapter implements DataSourceAdapter {

    private final RestTemplate restTemplate;

    @Value("${emr-service.base-url:http://localhost:9003}")
    private String baseUrl;

    public EMRAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getSourceType() {
        return "EMR";
    }

    @Override
    public List<Map<String, Object>> fetchLabOrders(String lastSyncTime) {
        return List.of();
    }

    @Override
    public List<Map<String, Object>> fetchLabResults(String lastSyncTime) {
        return List.of();
    }

    @Override
    public List<Map<String, Object>> fetchImagingOrders(String lastSyncTime) {
        return List.of();
    }

    @Override
    public List<Map<String, Object>> fetchImagingReports(String lastSyncTime) {
        return List.of();
    }

    @Override
    public List<Map<String, Object>> fetchPatients(String lastSyncTime) {
        return fetchList("/api/patients", lastSyncTime);
    }

    @Override
    public List<Map<String, Object>> fetchEncounters(String lastSyncTime) {
        return fetchList("/api/encounters", lastSyncTime);
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> fetchList(String path, String lastSyncTime) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl).path(path);
        if (StringUtils.hasText(lastSyncTime)) {
            builder.queryParam("lastSyncTime", lastSyncTime);
        }

        URI uri = builder.build().toUri();

        List<Map<String, Object>> result = restTemplate.getForObject(uri, List.class);
        return result == null ? List.of() : result;
    }
}
