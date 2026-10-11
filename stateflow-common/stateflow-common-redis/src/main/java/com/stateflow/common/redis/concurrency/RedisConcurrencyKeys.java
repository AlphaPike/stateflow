package com.stateflow.common.redis.concurrency;

/**
 * Redis 并发控制键名构建工具。
 */
public final class RedisConcurrencyKeys {

    private static final String PREFIX = "concurrency";

    private RedisConcurrencyKeys() {
    }

    /**
     * 独占锁键：concurrency:{namespace}:{resourceId}:lock:{operation}
     */
    public static String exclusiveLock(String namespace, String resourceId, String operation) {
        return join(PREFIX, namespace, resourceId, "lock", operation);
    }

    /**
     * 世代/版本键：concurrency:{namespace}:{resourceId}:generation
     */
    public static String generation(String namespace, String resourceId) {
        return join(PREFIX, namespace, resourceId, "generation");
    }

    /**
     * 自定义键名拼接。
     */
    public static String key(String... parts) {
        return join(parts);
    }

    private static String join(String... parts) {
        return String.join(":", parts);
    }
}
