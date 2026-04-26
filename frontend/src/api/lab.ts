import api from "./index";
import type { LabOrderDTO, LabTrendDTO } from "@/types/lab";

export const getLabOrder = (orderId: number) =>
  api.get<LabOrderDTO>(`/lab/${orderId}`);

export const getLabTrend = (patientId: number, itemCode: string) =>
  api.get<LabTrendDTO>(`/lab/trend/${patientId}/${itemCode}`);
