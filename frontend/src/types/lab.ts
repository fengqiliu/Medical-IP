export interface LabOrderDTO {
  id: number;
  patientId: number;
  orderNo: string;
  departmentId: number;
  departmentName: string;
  orderDatetime: string;
  status: string;
  specimenType: string;
  results: LabResultItem[];
}

export interface LabResultItem {
  id: number;
  itemCode: string;
  itemName: string;
  resultValue: string;
  unit: string;
  refRangeLow: number;
  refRangeHigh: number;
  abnormalFlag: string;
  resultDatetime: string;
}

export interface LabTrendDTO {
  itemCode: string;
  itemName: string;
  unit: string;
  history: Array<{
    datetime: string;
    value: string;
    abnormalFlag: string;
  }>;
}
