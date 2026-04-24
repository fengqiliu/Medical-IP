from pydantic import BaseModel
from typing import List, Optional

class LabResultItem(BaseModel):
    item_name: str
    result_value: str
    unit: Optional[str] = None
    ref_range: Optional[str] = None
    abnormal_flag: Optional[str] = None

class LabSummaryRequest(BaseModel):
    order_no: str
    patient_name: str
    results: List[LabResultItem]
    specimen_type: str

class ImagingSummaryRequest(BaseModel):
    order_no: str
    patient_name: str
    body_part: str
    modality: str
    report_content: Optional[str] = None
    impression: Optional[str] = None
    report_doctor: Optional[str] = None
