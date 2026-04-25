package com.medical360.integration.adapter;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EMRAdapterTest {

    @Test
    void shouldFetchPatientsFromConfiguredEndpoint() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        EMRAdapter adapter = new EMRAdapter(restTemplate);
        ReflectionTestUtils.setField(adapter, "baseUrl", "http://localhost:9003");

        URI expectedUri = UriComponentsBuilder.fromHttpUrl("http://localhost:9003")
            .path("/api/patients")
            .queryParam("lastSyncTime", "2026-04-25T10:00:00")
            .build()
            .toUri();
        when(restTemplate.getForObject(eq(expectedUri), eq(List.class)))
            .thenReturn(List.of(Map.of("PATIENT_ID", "101", "NAME", "张三")));

        List<Map<String, Object>> result = adapter.fetchPatients("2026-04-25T10:00:00");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).get("PATIENT_ID")).isEqualTo("101");
        verify(restTemplate).getForObject(eq(expectedUri), eq(List.class));
    }

    @Test
    void shouldFetchEncountersWithoutLastSyncWhenBlank() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        EMRAdapter adapter = new EMRAdapter(restTemplate);
        ReflectionTestUtils.setField(adapter, "baseUrl", "http://localhost:9003");

        URI expectedUri = UriComponentsBuilder.fromHttpUrl("http://localhost:9003")
            .path("/api/encounters")
            .build()
            .toUri();
        when(restTemplate.getForObject(eq(expectedUri), eq(List.class)))
            .thenReturn(List.of(Map.of("ENCOUNTER_ID", "201")));

        List<Map<String, Object>> result = adapter.fetchEncounters(null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).get("ENCOUNTER_ID")).isEqualTo("201");
        verify(restTemplate).getForObject(eq(expectedUri), eq(List.class));
    }
}
