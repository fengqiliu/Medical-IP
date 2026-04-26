"""
Token Bucket Rate Limiter
基于令牌桶算法的内存限流实现
"""
import time
import threading
from typing import Optional
from functools import wraps


class RateLimiter:
    """令牌桶限流器"""

    def __init__(self, qps: float = 10.0, burst_size: Optional[float] = None):
        """
        初始化限流器

        Args:
            qps: 每秒允许的请求数 (queries per second)
            burst_size: 令牌桶容量，默认为 qps 的 2 倍
        """
        self.qps = qps
        self.burst_size = burst_size if burst_size is not None else qps * 2
        self.tokens = self.burst_size
        self.last_update = time.monotonic()
        self._lock = threading.Lock()

    def _refill_tokens(self):
        """补充令牌"""
        now = time.monotonic()
        elapsed = now - self.last_update
        # 根据 elapsed 时间补充令牌
        new_tokens = elapsed * self.qps
        self.tokens = min(self.burst_size, self.tokens + new_tokens)
        self.last_update = now

    def acquire(self, tokens: float = 1.0, blocking: bool = True, timeout: Optional[float] = None) -> bool:
        """
        获取令牌

        Args:
            tokens: 需要获取的令牌数
            blocking: 是否阻塞等待
            timeout: 最大等待时间（秒），None 表示无限等待

        Returns:
            是否成功获取令牌
        """
        start_time = time.monotonic()

        while True:
            with self._lock:
                self._refill_tokens()

                if self.tokens >= tokens:
                    self.tokens -= tokens
                    return True

                if not blocking:
                    return False

                # 计算需要等待的时间
                wait_time = (tokens - self.tokens) / self.qps

                # 检查是否超时
                if timeout is not None:
                    elapsed = time.monotonic() - start_time
                    if elapsed + wait_time > timeout:
                        return False
                    wait_time = min(wait_time, timeout - elapsed)

            # 释放锁并等待
            time.sleep(min(wait_time, 0.1))  # 最多等待 0.1 秒再检查

    def try_acquire(self, tokens: float = 1.0) -> bool:
        """非阻塞尝试获取令牌"""
        return self.acquire(tokens, blocking=False)

    def get_wait_time(self, tokens: float = 1.0) -> float:
        """获取需要等待的时间（秒）"""
        with self._lock:
            self._refill_tokens()
            if self.tokens >= tokens:
                return 0.0
            return (tokens - self.tokens) / self.qps


class RateLimitConfig:
    """限流配置"""

    # 全局限流器实例
    _limiters: dict[str, RateLimiter] = {}
    _lock = threading.Lock()

    @classmethod
    def get_limiter(cls, name: str, qps: float = 10.0, burst_size: Optional[float] = None) -> RateLimiter:
        """
        获取或创建限流器

        Args:
            name: 限流器名称
            qps: 每秒请求数
            burst_size: 令牌桶容量
        """
        with cls._lock:
            if name not in cls._limiters:
                cls._limiters[name] = RateLimiter(qps=qps, burst_size=burst_size)
            return cls._limiters[name]

    @classmethod
    def reset(cls):
        """重置所有限流器（主要用于测试）"""
        with cls._lock:
            cls._limiters.clear()


def rate_limit(qps: float = 10.0, burst_size: Optional[float] = None, key_prefix: str = "default"):
    """
    限流装饰器

    Args:
        qps: 每秒允许的请求数
        burst_size: 令牌桶容量
        key_prefix: 限流器名称前缀

    Usage:
        @rate_limit(qps=5)
        async def my_endpoint():
            ...
    """
    def decorator(func):
        limiter = RateLimiter(qps=qps, burst_size=burst_size)

        @wraps(func)
        async def async_wrapper(*args, **kwargs):
            if not limiter.acquire(blocking=True, timeout=30.0):
                from app.middleware.error_handler import RateLimitError
                raise RateLimitError(
                    message=f"请求过于频繁，请稍后再试 (限流: {qps} QPS)",
                    retry_after=int(limiter.get_wait_time() + 1)
                )
            return await func(*args, **kwargs)

        @wraps(func)
        def sync_wrapper(*args, **kwargs):
            if not limiter.acquire(blocking=True, timeout=30.0):
                from app.middleware.error_handler import RateLimitError
                raise RateLimitError(
                    message=f"请求过于频繁，请稍后再试 (限流: {qps} QPS)",
                    retry_after=int(limiter.get_wait_time() + 1)
                )
            return func(*args, **kwargs)

        # 根据函数类型选择装饰器
        import asyncio
        if asyncio.iscoroutinefunction(func):
            return async_wrapper
        return sync_wrapper

    return decorator
