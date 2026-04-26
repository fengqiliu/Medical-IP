package com.medical360.controller;

import com.medical360.common.Result;
import com.medical360.dto.TimelineEventDTO;
import com.medical360.service.TimelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timeline")
@RequiredArgsConstructor
public class TimelineController {

    private final TimelineService timelineService;

    @GetMapping("/{patientId}")
    public Result<List<TimelineEventDTO>> getTimeline(@PathVariable Long patientId) {
        return Result.success(timelineService.getTimeline(patientId));
    }
}