// frontend/src/api/patient.ts
import api from "./index";
import type { Patient360DTO, Patient } from "@/types/patient";

export const getPatient360 = (patientId: number) =>
  api.get<Patient360DTO>(`/patient/360/${patientId}`);

export const searchPatients = (keyword: string) =>
  api.get<Patient[]>("/patient/search", { params: { keyword } });
