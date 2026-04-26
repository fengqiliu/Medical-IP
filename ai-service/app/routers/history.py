from datetime import datetime
from typing import Optional, List

from fastapi import APIRouter, Query

from app.models.batch_summary_models import (
    SummaryHistoryRecord,
    HistoryResponse,
)
from app.routers.batch_summary import summaries_history

router = APIRouter(prefix="/history", tags=["history"])


@router.get("/summaries", response_model=HistoryResponse)
async def query_summaries(
    page: int = Query(1, ge=1, description="Page number"),
    page_size: int = Query(10, ge=1, le=100, description="Items per page"),
    patient_name: Optional[str] = Query(None, description="Filter by patient name"),
    start_date: Optional[datetime] = Query(None, description="Filter by start date"),
    end_date: Optional[datetime] = Query(None, description="Filter by end date"),
):
    """
    Query historical summary records with pagination and filtering.

    - **page**: Page number (starts from 1)
    - **page_size**: Number of items per page (max 100)
    - **patient_name**: Filter by patient name (case-insensitive partial match)
    - **start_date**: Filter records created after this datetime
    - **end_date**: Filter records created before this datetime
    """
    # Apply filters
    filtered = summaries_history

    if patient_name:
        filtered = [r for r in filtered if patient_name.lower() in r.patient_name.lower()]

    if start_date:
        filtered = [r for r in filtered if r.created_at >= start_date]

    if end_date:
        filtered = [r for r in filtered if r.created_at <= end_date]

    # Sort by created_at descending (most recent first)
    filtered = sorted(filtered, key=lambda r: r.created_at, reverse=True)

    # Calculate pagination
    total = len(filtered)
    start_idx = (page - 1) * page_size
    end_idx = start_idx + page_size
    items = filtered[start_idx:end_idx]

    return HistoryResponse(
        total=total,
        page=page,
        page_size=page_size,
        items=items,
    )