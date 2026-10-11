package com.stateflow.common.redis.concurrency;

import com.stateflow.common.core.exception.ServiceException;
import com.stateflow.common.redis.service.RedisService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 基于 Redis 的通用分布式并发控制器。
 * <p>
 * 支持两类能力：
 * <ul>
 *     <li>独占锁：同一资源同一时刻仅允许一个持有者执行结构性变更</li>
 *     <li>Generation：结构性变更时递增版本，使进行中的读-改-写操作失效</li>
 * </ul>
 */
@Component
public class RedisConcurrencyController {

    @Resource
    private RedisService redisService;

    // --- 独占锁 ---

    public boolean tryAcquireExclusive(String lockKey, long ttlSeconds) {
        return Boolean.TRUE.equals(
                redisService.setIfAbsent(lockKey, "1", ttlSeconds, TimeUnit.SECONDS));
    }

    public void acquireExclusive(String lockKey, long ttlSeconds) {
        acquireExclusive(lockKey, ttlSeconds, "资源正在处理中，请稍后再试");
    }

    public void acquireExclusive(String lockKey, long ttlSeconds, String deniedMessage) {
        if (!tryAcquireExclusive(lockKey, ttlSeconds)) {
            throw new ServiceException(deniedMessage);
        }
    }

    public void releaseExclusive(String lockKey) {
        redisService.deleteObject(lockKey);
    }

    public boolean isExclusiveHeld(String lockKey) {
        return Boolean.TRUE.equals(redisService.hasKey(lockKey));
    }

    public void assertExclusiveNotHeld(String lockKey) {
        assertExclusiveNotHeld(lockKey, "当前资源正在独占处理，操作已中断，请刷新后重试");
    }

    /**
     * 确保锁资源没有被占用
     * @param lockKey 锁
     * @param message 占用时的提示消息
     */
    public void assertExclusiveNotHeld(String lockKey, String message) {
        if (isExclusiveHeld(lockKey)) {
            throw new ServiceException(message);
        }
    }

    public <T> T runWithExclusive(String lockKey, long ttlSeconds, String deniedMessage, Supplier<T> action) {
        acquireExclusive(lockKey, ttlSeconds, deniedMessage);
        try {
            return action.get();
        } finally {
            releaseExclusive(lockKey);
        }
    }

    public void runWithExclusive(String lockKey, long ttlSeconds, String deniedMessage, Runnable action) {
        acquireExclusive(lockKey, ttlSeconds, deniedMessage);
        try {
            action.run();
        } finally {
            releaseExclusive(lockKey);
        }
    }

    // --- Generation ---

    /**
     * 获取代际号
     * @param generationKey 锁键
     */
    public long captureGeneration(String generationKey) {
        Integer generation = redisService.getCacheObject(generationKey);
        return generation == null ? 0L : generation;
    }

    public long bumpGeneration(String generationKey) {
        Long generation = redisService.increment(generationKey);
        return generation == null ? 0L : generation;
    }

    public void assertGeneration(String generationKey, long expectedGeneration) {
        assertGeneration(generationKey, expectedGeneration, "数据版本已变更，当前操作已中断，请刷新后重试");
    }

    /**
     * 确保在函数执行过程中代际号没有发生过变更
     * @param generationKey 独占锁Key
     * @param expectedGeneration 函数执行时拿到的代际号
     * @param message 错误提醒
     */
    public void assertGeneration(String generationKey, long expectedGeneration, String message) {
        if (captureGeneration(generationKey) != expectedGeneration) {
            throw new ServiceException(message);
        }
    }

    // --- 独占 + Generation 组合 ---

    /**
     * 获取独占锁并递增 generation，用于强制中断进行中的操作。
     *
     * @return 递增后的 generation
     */
    public long beginExclusiveEpoch(String lockKey, String generationKey, long ttlSeconds, String deniedMessage) {
        acquireExclusive(lockKey, ttlSeconds, deniedMessage);
        return bumpGeneration(generationKey);
    }

    /**
     * 释放独占锁
     */
    public void endExclusiveEpoch(String lockKey) {
        releaseExclusive(lockKey);
    }
}
