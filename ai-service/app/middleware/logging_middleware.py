"""
Logging middleware for request/response logging with request ID tracking.
"""
import logging
import time
import uuid
from typing import Callable

from starlette.middleware.base import BaseHTTPMiddleware
from starlette.requests import Request
from starlette.responses import Response

# Configure logger
logger = logging.getLogger("app.middleware")


class LoggingMiddleware(BaseHTTPMiddleware):
    """Middleware to log request details, timing, and errors."""

    async def dispatch(self, request: Request, call_next: Callable) -> Response:
        # Generate unique request ID
        request_id = str(uuid.uuid4())[:8]

        # Store request ID in state for access in other parts of the app
        request.state.request_id = request_id

        # Log request start
        logger.info(
            f"[{request_id}] {request.method} {request.url.path} - Started",
            extra={
                "request_id": request_id,
                "method": request.method,
                "path": request.url.path,
                "client": request.client.host if request.client else "unknown"
            }
        )

        start_time = time.time()

        try:
            response = await call_next(request)
            elapsed_ms = (time.time() - start_time) * 1000

            # Log successful request
            logger.info(
                f"[{request_id}] {request.method} {request.url.path} - "
                f"Completed {response.status_code} in {elapsed_ms:.2f}ms",
                extra={
                    "request_id": request_id,
                    "method": request.method,
                    "path": request.url.path,
                    "status_code": response.status_code,
                    "elapsed_ms": elapsed_ms
                }
            )

            # Add request ID to response headers
            response.headers["X-Request-ID"] = request_id
            return response

        except Exception as exc:
            elapsed_ms = (time.time() - start_time) * 1000

            # Log error with full traceback
            logger.error(
                f"[{request_id}] {request.method} {request.url.path} - "
                f"Error after {elapsed_ms:.2f}ms: {exc}",
                exc_info=True,
                extra={
                    "request_id": request_id,
                    "method": request.method,
                    "path": request.url.path,
                    "elapsed_ms": elapsed_ms,
                    "error_type": type(exc).__name__
                }
            )
            raise