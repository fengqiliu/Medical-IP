// frontend/src/types/patient.ts
export interface Patient {
  id: number;
  unifiedPatientId: string;
  name: string;
  gender: string;
  birthDate: string;
  idCardNo: string;
  phone: string;
}

export interface Patient360DTO {
  patient: Patient;
  encounter: Encounter;
  labSummary: LabSummary;
  imagingSummary: ImagingSummary;
  aiSummary: string;
  alerts: Alert[];
}

export interface Encounter {
  id: number;
  encounterType: string;
  departmentName: string;
  visitDatetime: string;
  admissionReason: string;
}

export interface LabSummary {
  recentCount: number;
  abnormalCount: number;
  recentOrders: LabOrder[];
}

export interface LabOrder {
  id: number;
  orderNo: string;
  orderDatetime: string;
  specimenType: string;
  status: string;
  departmentName: string;
}

export interface ImagingSummary {
  recentCount: number;
  recentOrders: ImagingOrder[];
}

export interface ImagingOrder {
  id: number;
  orderNo: string;
  orderDatetime: string;
  bodyPart: string;
  modality: string;
  status: string;
  departmentName: string;
}

export interface Alert {
  type: "lab" | "imaging" | "warning";
  message: string;
  datetime: string;
}
