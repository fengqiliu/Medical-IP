package com.medical360.service;

import com.medical360.dto.Patient360DTO;

public interface PatientService {
    Patient360DTO getPatient360(Long patientId);
}