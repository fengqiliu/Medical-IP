export interface ImagingOrderDTO {
  id: number;
  patientId: number;
  orderNo: string;
  departmentId: number;
  departmentName: string;
  bodyPart: string;
  modality: string;
  orderDatetime: string;
  status: string;
  pacsUrl: string;
  report: ImagingReportDTO;
}

export interface ImagingReportDTO {
  id: number;
  reportContent: string;
  impression: string;
  reportDoctor: string;
  reportDatetime: string;
}
