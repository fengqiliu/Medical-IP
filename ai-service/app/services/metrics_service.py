"""
Metrics service for tracking LLM usage and performance.
"""
import time
import threading
from dataclasses import dataclass, field
from typing import Optional
from datetime import datetime


@dataclass
class LLMLatencyRecord:
    """Record for a single LLM call latency."""
    timestamp: datetime
    latency_ms: float
    success: bool
    tokens_used: Optional[int] = None
    model: Optional[str] = None


@dataclass
class MetricsSummary:
    """Aggregated metrics summary."""
    total_calls: int
    successful_calls: int
    failed_calls: int
    avg_latency_ms: float
    max_latency_ms: float
    min_latency_ms: float
    total_tokens_used: int
    success_rate: float
    start_time: str


class MetricsService:
    """Thread-safe metrics collector for LLM calls."""

    def __init__(self):
        self._records: list[LLMLatencyRecord] = []
        self._lock = threading.Lock()
        self._start_time = datetime.now()

    def record_call(
        self,
        latency_ms: float,
        success: bool,
        tokens_used: Optional[int] = None,
        model: Optional[str] = None
    ):
        """Record a single LLM call."""
        record = LLMLatencyRecord(
            timestamp=datetime.now(),
            latency_ms=latency_ms,
            success=success,
            tokens_used=tokens_used,
            model=model
        )
        with self._lock:
            self._records.append(record)

    def get_summary(self) -> MetricsSummary:
        """Get aggregated metrics summary."""
        with self._lock:
            if not self._records:
                return MetricsSummary(
                    total_calls=0,
                    successful_calls=0,
                    failed_calls=0,
                    avg_latency_ms=0.0,
                    max_latency_ms=0.0,
                    min_latency_ms=0.0,
                    total_tokens_used=0,
                    success_rate=100.0,
                    start_time=self._start_time.isoformat()
                )

            latencies = [r.latency_ms for r in self._records]
            successful = [r for r in self._records if r.success]
            failed = [r for r in self._records if not r.success]
            total_tokens = sum(r.tokens_used or 0 for r in self._records)

            return MetricsSummary(
                total_calls=len(self._records),
                successful_calls=len(successful),
                failed_calls=len(failed),
                avg_latency_ms=sum(latencies) / len(latencies),
                max_latency_ms=max(latencies),
                min_latency_ms=min(latencies),
                total_tokens_used=total_tokens,
                success_rate=(len(successful) / len(self._records)) * 100,
                start_time=self._start_time.isoformat()
            )

    def reset(self):
        """Reset all metrics (for testing)."""
        with self._lock:
            self._records.clear()
            self._start_time = datetime.now()


# Global metrics service instance
_metrics_service = MetricsService()


def get_metrics_service() -> MetricsService:
    """Get the global metrics service instance."""
    return _metrics_service


def record_llm_call(
    latency_ms: float,
    success: bool,
    tokens_used: Optional[int] = None,
    model: Optional[str] = None
):
    """Convenience function to record LLM call metrics."""
    _metrics_service.record_call(latency_ms, success, tokens_used, model)