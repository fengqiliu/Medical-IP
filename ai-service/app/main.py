import logging
import sys
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from app.middleware.logging_middleware import LoggingMiddleware
from app.services.metrics_service import get_metrics_service
from app.routers import lab_summary, imaging_summary, batch_summary, history


# Configure structured logging for container stdout
def setup_logging():
    handler = logging.StreamHandler(sys.stdout)
    handler.setLevel(logging.INFO)

    class RequestIdFormatter(logging.Formatter):
        def format(self, record):
            if hasattr(record, "request_id"):
                record.request_id = record.request_id
            else:
                record.request_id = "-"
            return super().format(record)

    handler.setFormatter(RequestIdFormatter(
        fmt="%(asctime)s [%(request_id)s] %(levelname)s %(message)s",
        datefmt="%Y-%m-%d %H:%M:%S"
    ))

    root_logger = logging.getLogger("app")
    root_logger.setLevel(logging.INFO)
    root_logger.addHandler(handler)

    # Reduce noise from third-party libraries
    logging.getLogger("httpx").setLevel(logging.WARNING)
    logging.getLogger("openai").setLevel(logging.WARNING)
    logging.getLogger("langchain").setLevel(logging.WARNING)

    return logging.getLogger("app")


app = FastAPI(title="医技360 AI 服务", version="1.0.0")

# Setup logging
setup_logging()

# Add CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Add logging middleware
app.add_middleware(LoggingMiddleware)

# Include routers
app.include_router(lab_summary.router)
app.include_router(imaging_summary.router)
app.include_router(batch_summary.router)
app.include_router(history.router)


@app.get("/health")
async def health():
    return {"status": "ok"}


@app.get("/metrics")
async def metrics():
    """Return LLM usage and performance metrics."""
    service = get_metrics_service()
    summary = service.get_summary()
    return {
        "total_calls": summary.total_calls,
        "successful_calls": summary.successful_calls,
        "failed_calls": summary.failed_calls,
        "avg_latency_ms": round(summary.avg_latency_ms, 2),
        "max_latency_ms": round(summary.max_latency_ms, 2),
        "min_latency_ms": round(summary.min_latency_ms, 2),
        "total_tokens_used": summary.total_tokens_used,
        "success_rate": round(summary.success_rate, 2),
        "uptime_since": summary.start_time
    }


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)