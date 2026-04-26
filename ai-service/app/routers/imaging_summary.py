import time
from fastapi import APIRouter, HTTPException

from app.models.summary_request import ImagingSummaryRequest
from app.services.llm_service import get_llm
from app.services.prompt_builder import build_imaging_summary_prompt
from app.services.metrics_service import record_llm_call
from app.services.rate_limiter import RateLimitConfig
from app.middleware.error_handler import (
    APIError,
    RateLimitError,
    QuotaError,
    NetworkError,
    ModelError,
    TimeoutError,
    AuthenticationError,
    classify_openai_error
)

router = APIRouter(prefix="/imaging", tags=["imaging"])

# 获取限流器实例 (5 QPS for imaging summaries)
_imaging_limiter = RateLimitConfig.get_limiter("imaging_summary", qps=5.0, burst_size=10.0)


def _build_error_response(error_type: str, message: str, retry_after: int = None, **kwargs):
    """构建统一格式的错误响应"""
    response = {
        "success": False,
        "error_type": error_type,
        "message": message
    }
    if retry_after is not None:
        response["retry_after"] = retry_after
    return {**response, **kwargs}


@router.post("/summary")
async def generate_imaging_summary(request: ImagingSummaryRequest):
    """
    生成影像报告摘要

    集成限流和指数退避重试机制
    """
    start_time = time.time()

    # 1. 限流检查
    if not _imaging_limiter.try_acquire():
        wait_time = int(_imaging_limiter.get_wait_time() + 1)
        raise HTTPException(
            status_code=429,
            detail=_build_error_response(
                error_type="rate_limit_error",
                message=f"请求过于频繁，请等待 {wait_time} 秒后重试",
                retry_after=wait_time
            )
        )

    # 2. 使用指数退避重试调用 LLM
    try:
        summary = await _generate_summary_with_retry(request)
        latency_ms = (time.time() - start_time) * 1000
        record_llm_call(latency_ms=latency_ms, success=True, model="gpt-3.5-turbo")
        return {
            "success": True,
            "summary": summary,
            "model_version": "gpt-3.5-turbo"
        }
    except APIError as e:
        latency_ms = (time.time() - start_time) * 1000
        record_llm_call(latency_ms=latency_ms, success=False)
        raise HTTPException(
            status_code=e.status_code,
            detail=e.to_dict()
        )
    except Exception as e:
        latency_ms = (time.time() - start_time) * 1000
        record_llm_call(latency_ms=latency_ms, success=False)
        api_error = classify_openai_error(e)
        raise HTTPException(
            status_code=api_error.status_code,
            detail=api_error.to_dict()
        )


async def _generate_summary_with_retry(request: ImagingSummaryRequest) -> str:
    """
    带重试的摘要生成

    使用指数退避策略重试可恢复的错误
    """
    for attempt in range(1, 4):  # 最多 3 次尝试
        try:
            llm = get_llm()
            prompt = build_imaging_summary_prompt(request)
            response = await llm.ainvoke(prompt)
            return response.content

        except Exception as e:
            error_msg = str(e).lower()

            # 判断是否为可重试的错误
            retryable = False
            retry_after = None

            if "rate_limit" in error_msg or "429" in error_msg:
                retryable = True
                retry_after = 60
            elif "quota" in error_msg or "insufficient" in error_msg:
                retryable = True
                retry_after = 60
            elif "timeout" in error_msg or "timed out" in error_msg:
                retryable = True
                retry_after = 30
            elif any(k in error_msg for k in ["connection", "network", "dns"]):
                retryable = True
                retry_after = 10

            # 非重试错误或最后一次尝试，直接抛出
            if not retryable or attempt == 3:
                raise classify_openai_error(e)

            # 计算指数退避延迟: 1s, 2s, 4s (max 30s)
            delay = min(2 ** (attempt - 1) * 1.0, 30.0)
            if retry_after:
                delay = min(delay, retry_after)

            time.sleep(delay)

    # 如果所有重试都失败
    raise APIError(
        message="摘要生成失败，请稍后重试",
        error_type="service_unavailable",
        status_code=503,
        retryable=True,
        retry_after=30
    )
