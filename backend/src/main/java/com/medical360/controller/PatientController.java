package com.medical360.controller;

import com.medical360.common.Result;
import com.medical360.dto.Patient360DTO;
import com.medical360.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/360/{patientId}")
    public Result<Patient360DTO> getPatient360(@PathVariable Long patientId) {
        return Result.success(patientService.getPatient360(patientId));
    }
}