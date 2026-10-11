package com.stateflow.system.concurrency;

import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Mutual exclusion for a single business key.
 * The lock is released after the surrounding transaction commits or rolls back,
 * so a concurrent callback cannot observe a half-written state.
 */
@Component
public class ActionLock
{
    private static final String PREFIX = "workflow:action:";
    private static final long TTL_SECONDS = 30L;

    private final StringRedisTemplate stringRedisTemplate;

    public ActionLock(StringRedisTemplate stringRedisTemplate)
    {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public boolean tryLock(String bizKey)
    {
        Boolean locked = stringRedisTemplate.opsForValue()
                .setIfAbsent(PREFIX + bizKey, "1", TTL_SECONDS, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(locked);
    }

    public void unlock(String bizKey)
    {
        stringRedisTemplate.delete(PREFIX + bizKey);
    }

    public void unlockAfterCompletion(final String bizKey)
    {
        if (!TransactionSynchronizationManager.isSynchronizationActive())
        {
            unlock(bizKey);
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization()
        {
            @Override
            public void afterCompletion(int status)
            {
                unlock(bizKey);
            }
        });
    }
}
