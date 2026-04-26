"""Middleware package"""
from app.middleware.error_handler import (
    APIError,
    NetworkError,
    TimeoutError,
    RateLimitError,
    QuotaError,
    ModelError,
    FormatError,
    ValidationError,
    AuthenticationError,
    ServiceUnavailableError,
    ErrorHandlerMiddleware,
    classify_openai_error,
    create_error_response
)

__all__ = [
    "APIError",
    "NetworkError",
    "TimeoutError",
    "RateLimitError",
    "QuotaError",
    "ModelError",
    "FormatError",
    "ValidationError",
    "AuthenticationError",
    "ServiceUnavailableError",
    "ErrorHandlerMiddleware",
    "classify_openai_error",
    "create_error_response"
]
