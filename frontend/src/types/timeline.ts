export interface TimelineEventDTO {
  id: number;
  eventType: "lab" | "imaging";
  eventDatetime: string;
  eventSummary: string;
  eventStatus: string;
  referenceId: number;
  departmentName: string;
  abnormal?: boolean;
}
