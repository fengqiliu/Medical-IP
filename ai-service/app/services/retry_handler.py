"""
Exponential Backoff Retry Handler
指数退避重试装饰器
"""
import time
import logging
from typing import TypeVar, Callable, Optional, Set, Type
from functools import wraps

from app.middleware.error_handler import (
    APIError,
    NetworkError,
    RateLimitError,
    QuotaError,
    retry_after_to_seconds
)

logger = logging.getLogger(__name__)

T = TypeVar('T')


class RetryConfig:
    """重试配置"""

    def __init__(
        self,
        max_attempts: int = 3,
        base_delay: float = 1.0,
        max_delay: float = 60.0,
        exponential_base: float = 2.0,
        jitter: bool = True,
        retryable_exceptions: Optional[Set[Type[Exception]]] = None
    ):
        """
        初始化重试配置

        Args:
            max_attempts: 最大重试次数
            base_delay: 基础延迟时间（秒）
            max_delay: 最大延迟时间（秒）
            exponential_base: 指数基数
            jitter: 是否添加随机抖动
            retryable_exceptions: 可重试的异常类型集合
        """
        self.max_attempts = max_attempts
        self.base_delay = base_delay
        self.max_delay = max_delay
        self.exponential_base = exponential_base
        self.jitter = jitter
        self.retryable_exceptions = retryable_exceptions or {
            NetworkError,
            RateLimitError,
            QuotaError,
            APIError
        }

    def calculate_delay(self, attempt: int) -> float:
        """计算延迟时间"""
        delay = min(self.base_delay * (self.exponential_base ** (attempt - 1)), self.max_delay)
        if self.jitter:
            import random
            delay *= (0.5 + random.random())  # 0.5 ~ 1.5 倍
        return delay


def is_retryable(exception: Exception) -> bool:
    """判断异常是否可重试"""
    # 网络错误始终可重试
    if isinstance(exception, NetworkError):
        return True

    # 限流错误可重试，但需要等待
    if isinstance(exception, RateLimitError):
        return True

    # 配额错误可重试
    if isinstance(exception, QuotaError):
        return True

    # API 错误检查是否可重试
    if isinstance(exception, APIError):
        return exception.retryable

    # 其他异常默认不可重试
    return False


def retry_with_backoff(
    max_attempts: int = 3,
    base_delay: float = 1.0,
    max_delay: float = 60.0,
    exponential_base: float = 2.0,
    jitter: bool = True,
    on_retry: Optional[Callable[[Exception, int, float], None]] = None
):
    """
    指数退避重试装饰器

    Args:
        max_attempts: 最大重试次数
        base_delay: 基础延迟时间（秒）
        max_delay: 最大延迟时间（秒）
        exponential_base: 指数基数
        jitter: 是否添加随机抖动
        on_retry: 重试时的回调函数 (exception, attempt, delay)

    Usage:
        @retry_with_backoff(max_attempts=3, base_delay=1.0)
        async def my_function():
            ...
    """
    config = RetryConfig(
        max_attempts=max_attempts,
        base_delay=base_delay,
        max_delay=max_delay,
        exponential_base=exponential_base,
        jitter=jitter
    )

    def decorator(func):
        @wraps(func)
        async def async_wrapper(*args, **kwargs):
            last_exception = None

            for attempt in range(1, config.max_attempts + 1):
                try:
                    return await func(*args, **kwargs)
                except Exception as e:
                    last_exception = e

                    # 检查是否可重试
                    if not is_retryable(e):
                        logger.warning(f"Non-retryable exception in {func.__name__}: {e}")
                        raise

                    # 如果是最后一次尝试
                    if attempt == config.max_attempts:
                        logger.error(f"Max retry attempts ({config.max_attempts}) reached for {func.__name__}")
                        raise

                    # 计算延迟时间
                    delay = config.calculate_delay(attempt)

                    # 如果是限流错误，使用建议的 retry_after
                    if isinstance(e, (RateLimitError, QuotaError)) and e.retry_after:
                        delay = min(e.retry_after, config.max_delay)

                    logger.warning(
                        f"Retry attempt {attempt}/{config.max_attempts} for {func.__name__} "
                        f"after {delay:.2f}s due to: {e}"
                    )

                    if on_retry:
                        on_retry(e, attempt, delay)

                    time.sleep(delay)

            # 不应该到达这里，但以防万一
            if last_exception:
                raise last_exception
            raise RuntimeError("Retry loop exited unexpectedly")

        @wraps(func)
        def sync_wrapper(*args, **kwargs):
            last_exception = None

            for attempt in range(1, config.max_attempts + 1):
                try:
                    return func(*args, **kwargs)
                except Exception as e:
                    last_exception = e

                    # 检查是否可重试
                    if not is_retryable(e):
                        logger.warning(f"Non-retryable exception in {func.__name__}: {e}")
                        raise

                    # 如果是最后一次尝试
                    if attempt == config.max_attempts:
                        logger.error(f"Max retry attempts ({config.max_attempts}) reached for {func.__name__}")
                        raise

                    # 计算延迟时间
                    delay = config.calculate_delay(attempt)

                    # 如果是限流错误，使用建议的 retry_after
                    if isinstance(e, (RateLimitError, QuotaError)) and e.retry_after:
                        delay = min(e.retry_after, config.max_delay)

                    logger.warning(
                        f"Retry attempt {attempt}/{config.max_attempts} for {func.__name__} "
                        f"after {delay:.2f}s due to: {e}"
                    )

                    if on_retry:
                        on_retry(e, attempt, delay)

                    time.sleep(delay)

            # 不应该到达这里，但以防万一
            if last_exception:
                raise last_exception
            raise RuntimeError("Retry loop exited unexpectedly")

        # 根据函数类型选择装饰器
        import asyncio
        if asyncio.iscoroutinefunction(func):
            return async_wrapper
        return sync_wrapper

    return decorator


class RetryContext:
    """重试上下文，用于在重试过程中传递信息"""

    def __init__(self):
        self.attempts = 0
        self.last_exception: Optional[Exception] = None
        self.total_delay = 0.0

    def record_attempt(self, exception: Exception, delay: float):
        """记录一次重试尝试"""
        self.attempts += 1
        self.last_exception = exception
        self.total_delay += delay


def retry_with_context(
    max_attempts: int = 3,
    base_delay: float = 1.0,
    max_delay: float = 60.0
):
    """
    带上下文的重试装饰器，返回 RetryContext

    Args:
        max_attempts: 最大重试次数
        base_delay: 基础延迟时间（秒）
        max_delay: 最大延迟时间（秒）

    Usage:
        @retry_with_context(max_attempts=3)
        async def my_function(ctx: RetryContext):
            ctx.attempts  # 访问重试次数
            ...
    """
    config = RetryConfig(
        max_attempts=max_attempts,
        base_delay=base_delay,
        max_delay=max_delay
    )

    def decorator(func):
        @wraps(func)
        async def async_wrapper(*args, **kwargs):
            ctx = RetryContext()
            last_exception = None

            for attempt in range(1, config.max_attempts + 1):
                try:
                    # 将上下文传递给函数
                    return await func(*args, **kwargs, retry_context=ctx)
                except Exception as e:
                    last_exception = e

                    if not is_retryable(e):
                        raise

                    if attempt == config.max_attempts:
                        raise

                    delay = config.calculate_delay(attempt)
                    if isinstance(e, (RateLimitError, QuotaError)) and e.retry_after:
                        delay = min(e.retry_after, config.max_delay)

                    ctx.record_attempt(e, delay)
                    time.sleep(delay)

            if last_exception:
                raise last_exception
            raise RuntimeError("Retry loop exited unexpectedly")

        @wraps(func)
        def sync_wrapper(*args, **kwargs):
            ctx = RetryContext()
            last_exception = None

            for attempt in range(1, config.max_attempts + 1):
                try:
                    # 将上下文传递给函数
                    return func(*args, **kwargs, retry_context=ctx)
                except Exception as e:
                    last_exception = e

                    if not is_retryable(e):
                        raise

                    if attempt == config.max_attempts:
                        raise

                    delay = config.calculate_delay(attempt)
                    if isinstance(e, (RateLimitError, QuotaError)) and e.retry_after:
                        delay = min(e.retry_after, config.max_delay)

                    ctx.record_attempt(e, delay)
                    time.sleep(delay)

            if last_exception:
                raise last_exception
            raise RuntimeError("Retry loop exited unexpectedly")

        import asyncio
        if asyncio.iscoroutinefunction(func):
            return async_wrapper
        return sync_wrapper

    return decorator
