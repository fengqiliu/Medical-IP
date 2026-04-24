package com.medical360.service;

import com.medical360.dto.TimelineEventDTO;
import java.util.List;

public interface TimelineService {
    List<TimelineEventDTO> getTimeline(Long patientId);
}