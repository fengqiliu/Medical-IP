from pydantic import BaseModel
from typing import List, Optional
from datetime import datetime

class LabSummaryRequest(BaseModel):
    order_no: str
    patient_name: str
    results: List["LabResultItem"]
    specimen_type: str

class LabResultItem(BaseModel):
    item_name: str
    result_value: str
    unit: Optional[str] = None
    ref_range: Optional[str] = None
    abnormal_flag: Optional[str] = None

class ImagingSummaryRequest(BaseModel):
    order_no: str
    patient_name: str
    body_part: str
    modality: str
    report_content: Optional[str] = None
    impression: Optional[str] = None
    report_doctor: Optional[str] = None

class BatchLabSummaryResponse(BaseModel):
    order_no: str
    success: bool
    summary: Optional[str] = None
    error: Optional[str] = None

class BatchImagingSummaryResponse(BaseModel):
    order_no: str
    success: bool
    summary: Optional[str] = None
    error: Optional[str] = None

class SummaryHistoryRecord(BaseModel):
    id: str
    order_no: str
    patient_name: str
    summary_type: str  # "lab" or "imaging"
    summary: str
    created_at: datetime
    latency_ms: float
    success: bool

class HistoryQueryParams(BaseModel):
    page: int = 1
    page_size: int = 10
    patient_name: Optional[str] = None
    start_date: Optional[datetime] = None
    end_date: Optional[datetime] = None

class HistoryResponse(BaseModel):
    total: int
    page: int
    page_size: int
    items: List[SummaryHistoryRecord]