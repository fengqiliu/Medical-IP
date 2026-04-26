"""
Error Handler Middleware
统一异常处理和错误分类
"""
import time
import logging
from typing import Optional, Union, Callable

from fastapi import Request, Response
from fastapi.responses import JSONResponse
from starlette.middleware.base import BaseHTTPMiddleware
from starlette.types import ASGIApp

logger = logging.getLogger(__name__)


def retry_after_to_seconds(retry_after: Optional[Union[int, float, str]]) -> Optional[float]:
    """转换 retry_after 为秒数"""
    if retry_after is None:
        return None
    if isinstance(retry_after, (int, float)):
        return float(retry_after)
    if isinstance(retry_after, str):
        try:
            return float(retry_after)
        except ValueError:
            return None
    return None


class APIError(Exception):
    """API 错误基类"""

    def __init__(
        self,
        message: str,
        error_type: str = "api_error",
        status_code: int = 500,
        retryable: bool = False,
        retry_after: Optional[Union[int, float]] = None,
        details: Optional[dict] = None
    ):
        self.message = message
        self.error_type = error_type
        self.status_code = status_code
        self.retryable = retryable
        self.retry_after = retry_after_to_seconds(retry_after)
        self.details = details or {}
        super().__init__(self.message)

    def to_dict(self) -> dict:
        return {
            "success": False,
            "error_type": self.error_type,
            "message": self.message,
            "retry_after": self.retry_after,
            "retryable": self.retryable,
            "details": self.details
        }


class NetworkError(APIError):
    """网络错误 - 连接超时、DNS 解析失败等"""

    def __init__(
        self,
        message: str = "网络连接失败，请检查网络后重试",
        retry_after: Optional[Union[int, float]] = 5,
        details: Optional[dict] = None
    ):
        super().__init__(
            message=message,
            error_type="network_error",
            status_code=503,
            retryable=True,
            retry_after=retry_after,
            details=details
        )


class TimeoutError(APIError):
    """请求超时错误"""

    def __init__(
        self,
        message: str = "请求超时，请稍后重试",
        retry_after: Optional[Union[int, float]] = 10,
        details: Optional[dict] = None
    ):
        super().__init__(
            message=message,
            error_type="timeout_error",
            status_code=504,
            retryable=True,
            retry_after=retry_after,
            details=details
        )


class RateLimitError(APIError):
    """限流错误"""

    def __init__(
        self,
        message: str = "请求过于频繁，请稍后重试",
        retry_after: Optional[Union[int, float]] = 60,
        details: Optional[dict] = None
    ):
        super().__init__(
            message=message,
            error_type="rate_limit_error",
            status_code=429,
            retryable=True,
            retry_after=retry_after,
            details=details
        )


class QuotaError(APIError):
    """API 配额错误 - OpenAI API 配额超限等"""

    def __init__(
        self,
        message: str = "API 配额已用尽，请稍后重试",
        retry_after: Optional[Union[int, float]] = 60,
        details: Optional[dict] = None
    ):
        super().__init__(
            message=message,
            error_type="quota_error",
            status_code=429,
            retryable=True,
            retry_after=retry_after,
            details=details
        )


class ModelError(APIError):
    """模型错误 - 模型加载失败、不支持等"""

    def __init__(
        self,
        message: str = "AI 模型服务暂时不可用",
        retry_after: Optional[Union[int, float]] = 30,
        details: Optional[dict] = None
    ):
        super().__init__(
            message=message,
            error_type="model_error",
            status_code=503,
            retryable=True,
            retry_after=retry_after,
            details=details
        )


class FormatError(APIError):
    """格式错误 - 输入/输出格式不正确"""

    def __init__(
        self,
        message: str = "数据格式错误",
        retry_after: Optional[Union[int, float]] = None,
        details: Optional[dict] = None
    ):
        super().__init__(
            message=message,
            error_type="format_error",
            status_code=400,
            retryable=False,
            retry_after=retry_after,
            details=details
        )


class ValidationError(APIError):
    """验证错误 - 输入参数验证失败"""

    def __init__(
        self,
        message: str = "输入参数验证失败",
        retry_after: Optional[Union[int, float]] = None,
        details: Optional[dict] = None
    ):
        super().__init__(
            message=message,
            error_type="validation_error",
            status_code=422,
            retryable=False,
            retry_after=retry_after,
            details=details
        )


class AuthenticationError(APIError):
    """认证错误 - API Key 无效等"""

    def __init__(
        self,
        message: str = "认证失败，请检查 API 配置",
        retry_after: Optional[Union[int, float]] = None,
        details: Optional[dict] = None
    ):
        super().__init__(
            message=message,
            error_type="authentication_error",
            status_code=401,
            retryable=False,
            retry_after=retry_after,
            details=details
        )


class ServiceUnavailableError(APIError):
    """服务不可用"""

    def __init__(
        self,
        message: str = "服务暂时不可用，请稍后重试",
        retry_after: Optional[Union[int, float]] = 30,
        details: Optional[dict] = None
    ):
        super().__init__(
            message=message,
            error_type="service_unavailable",
            status_code=503,
            retryable=True,
            retry_after=retry_after,
            details=details
        )


def classify_openai_error(exception: Exception) -> APIError:
    """将 OpenAI API 错误分类为我们的错误类型"""

    error_message = str(exception).lower()
    error_type = type(exception).__name__

    # 速率限制
    if "rate_limit" in error_message or "429" in error_message:
        return RateLimitError(
            message="OpenAI API 限流，请稍后重试",
            retry_after=60,
            details={"original_error": error_type}
        )

    # 配额错误
    if "quota" in error_message or "insufficient_quota" in error_message:
        return QuotaError(
            message="OpenAI API 配额已用尽",
            retry_after=60,
            details={"original_error": error_type}
        )

    # 超时
    if "timeout" in error_message or "timed out" in error_message:
        return TimeoutError(
            message="OpenAI API 请求超时",
            retry_after=30,
            details={"original_error": error_type}
        )

    # 网络错误
    if any(keyword in error_message for keyword in ["connection", "network", "dns", "refused"]):
        return NetworkError(
            message="网络连接 OpenAI API 失败",
            retry_after=10,
            details={"original_error": error_type}
        )

    # 认证错误
    if any(keyword in error_message for keyword in ["auth", "api key", "unauthorized", "invalid"]):
        return AuthenticationError(
            message="OpenAI API 认证失败",
            retry_after=None,
            details={"original_error": error_type}
        )

    # 模型错误
    if "model" in error_message or "not found" in error_message:
        return ModelError(
            message="AI 模型不可用",
            retry_after=30,
            details={"original_error": error_type}
        )

    # 默认为服务不可用
    return ServiceUnavailableError(
        message=f"OpenAI API 错误: {error_type}",
        retry_after=30,
        details={"original_error": error_type}
    )


def create_error_response(
    error_type: str,
    message: str,
    retry_after: Optional[float] = None,
    retryable: bool = False,
    details: Optional[dict] = None
) -> dict:
    """创建统一格式的错误响应"""
    response = {
        "success": False,
        "error_type": error_type,
        "message": message
    }
    if retry_after is not None:
        response["retry_after"] = int(retry_after)
    response["retryable"] = retryable
    if details:
        response["details"] = details
    return response


class ErrorHandlerMiddleware(BaseHTTPMiddleware):
    """全局错误处理中间件"""

    def __init__(
        self,
        app: ASGIApp,
        fallback_message: str = "服务暂时不可用，请稍后重试"
    ):
        super().__init__(app)
        self.fallback_message = fallback_message

    async def dispatch(self, request: Request, call_next: Callable) -> Response:
        try:
            response = await call_next(request)
            return response
        except APIError as e:
            logger.warning(f"APIError: {e.error_type} - {e.message}")
            return JSONResponse(
                status_code=e.status_code,
                content=e.to_dict()
            )
        except Exception as e:
            logger.exception(f"Unhandled exception: {e}")
            error_response = create_error_response(
                error_type="internal_error",
                message=self.fallback_message,
                retryable=False
            )
            return JSONResponse(
                status_code=500,
                content=error_response
            )


async def api_error_handler(request: Request, exc: APIError) -> JSONResponse:
    """APIError 的异常处理器（用于 FastAPI）"""
    return JSONResponse(
        status_code=exc.status_code,
        content=exc.to_dict()
    )


async def generic_error_handler(request: Request, exc: Exception) -> JSONResponse:
    """通用异常处理器（用于 FastAPI）"""
    logger.exception(f"Unhandled exception: {exc}")
    return JSONResponse(
        status_code=500,
        content=create_error_response(
            error_type="internal_error",
            message="服务暂时不可用，请稍后重试",
            retryable=False
        )
    )
