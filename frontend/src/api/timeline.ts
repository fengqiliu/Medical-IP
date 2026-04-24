import api from "./index";
import type { TimelineEventDTO } from "@/types/timeline";

export const getTimeline = (
  patientId: number,
  params?: {
    startDate?: string;
    endDate?: string;
    eventType?: string;
    departmentId?: number;
  },
) => api.get<TimelineEventDTO[]>(`/timeline/${patientId}`, { params });
