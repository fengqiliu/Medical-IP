import asyncio
import time
import uuid
from datetime import datetime
from typing import List

from fastapi import APIRouter

from app.models.batch_summary_models import (
    LabSummaryRequest,
    ImagingSummaryRequest,
    BatchLabSummaryResponse,
    BatchImagingSummaryResponse,
    SummaryHistoryRecord,
)
from app.services.llm_service import get_llm
from app.services.prompt_builder import build_lab_summary_prompt, build_imaging_summary_prompt
from app.services.metrics_service import record_llm_call

router = APIRouter(prefix="/batch", tags=["batch"])

# In-memory storage for history
summaries_history: List[SummaryHistoryRecord] = []
MAX_HISTORY_SIZE = 1000

# Concurrency control: max 5 concurrent requests
semaphore = asyncio.Semaphore(5)


def _add_to_history(
    order_no: str,
    patient_name: str,
    summary_type: str,
    summary: str,
    latency_ms: float,
    success: bool,
):
    """Add a summary record to history with FIFO deletion when limit exceeded."""
    record = SummaryHistoryRecord(
        id=str(uuid.uuid4()),
        order_no=order_no,
        patient_name=patient_name,
        summary_type=summary_type,
        summary=summary,
        created_at=datetime.now(),
        latency_ms=latency_ms,
        success=success,
    )
    summaries_history.append(record)
    # FIFO deletion when exceeding max size
    if len(summaries_history) > MAX_HISTORY_SIZE:
        summaries_history.pop(0)
    return record


async def _process_single_lab_summary(request: LabSummaryRequest) -> BatchLabSummaryResponse:
    """Process a single lab summary request."""
    async with semaphore:
        start_time = time.time()
        try:
            llm = get_llm()
            prompt = build_lab_summary_prompt(request)
            response = llm.invoke(prompt)
            latency_ms = (time.time() - start_time) * 1000

            _add_to_history(
                order_no=request.order_no,
                patient_name=request.patient_name,
                summary_type="lab",
                summary=response.content,
                latency_ms=latency_ms,
                success=True,
            )
            record_llm_call(latency_ms=latency_ms, success=True, model="gpt-3.5-turbo")

            return BatchLabSummaryResponse(
                order_no=request.order_no,
                success=True,
                summary=response.content,
            )
        except Exception as e:
            latency_ms = (time.time() - start_time) * 1000
            error_msg = str(e)
            _add_to_history(
                order_no=request.order_no,
                patient_name=request.patient_name,
                summary_type="lab",
                summary="摘要生成失败",
                latency_ms=latency_ms,
                success=False,
            )
            record_llm_call(latency_ms=latency_ms, success=False)
            return BatchLabSummaryResponse(
                order_no=request.order_no,
                success=False,
                error=error_msg,
                summary="摘要生成失败，请查看原始数据",
            )


async def _process_single_imaging_summary(request: ImagingSummaryRequest) -> BatchImagingSummaryResponse:
    """Process a single imaging summary request."""
    async with semaphore:
        start_time = time.time()
        try:
            llm = get_llm()
            prompt = build_imaging_summary_prompt(request)
            response = llm.invoke(prompt)
            latency_ms = (time.time() - start_time) * 1000

            _add_to_history(
                order_no=request.order_no,
                patient_name=request.patient_name,
                summary_type="imaging",
                summary=response.content,
                latency_ms=latency_ms,
                success=True,
            )
            record_llm_call(latency_ms=latency_ms, success=True, model="gpt-3.5-turbo")

            return BatchImagingSummaryResponse(
                order_no=request.order_no,
                success=True,
                summary=response.content,
            )
        except Exception as e:
            latency_ms = (time.time() - start_time) * 1000
            error_msg = str(e)
            _add_to_history(
                order_no=request.order_no,
                patient_name=request.patient_name,
                summary_type="imaging",
                summary="摘要生成失败",
                latency_ms=latency_ms,
                success=False,
            )
            record_llm_call(latency_ms=latency_ms, success=False)
            return BatchImagingSummaryResponse(
                order_no=request.order_no,
                success=False,
                error=error_msg,
                summary="摘要生成失败，请查看原始报告",
            )


@router.post("/lab-summary", response_model=List[BatchLabSummaryResponse])
async def batch_generate_lab_summaries(requests: List[LabSummaryRequest]):
    """
    Batch generate lab summaries with concurrency control.
    Max 5 concurrent requests at a time.
    """
    tasks = [_process_single_lab_summary(req) for req in requests]
    results = await asyncio.gather(*tasks)
    return list(results)


@router.post("/imaging-summary", response_model=List[BatchImagingSummaryResponse])
async def batch_generate_imaging_summaries(requests: List[ImagingSummaryRequest]):
    """
    Batch generate imaging summaries with concurrency control.
    Max 5 concurrent requests at a time.
    """
    tasks = [_process_single_imaging_summary(req) for req in requests]
    results = await asyncio.gather(*tasks)
    return list(results)