package com.stateflow.system.concurrency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Delayed work index. The score is the next attempt time.
 * The database remains the source of truth for workflow state.
 */
@Component
public class RetryWatchQueue
{
    static final String ZSET_KEY = "workflow:watch";
    private static final String ATTEMPT_PREFIX = "workflow:watch:attempt:";
    private static final long[] BACKOFF_MS = {2_000L, 5_000L, 15_000L, 60_000L};
    public static final int MAX_ATTEMPTS = BACKOFF_MS.length;

    private final StringRedisTemplate stringRedisTemplate;

    public RetryWatchQueue(StringRedisTemplate stringRedisTemplate)
    {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void enqueue(String taskId)
    {
        if (taskId == null || taskId.isEmpty())
        {
            return;
        }
        stringRedisTemplate.opsForValue().set(ATTEMPT_PREFIX + taskId, "0", 1, TimeUnit.DAYS);
        stringRedisTemplate.opsForZSet().add(ZSET_KEY, taskId, System.currentTimeMillis() + BACKOFF_MS[0]);
    }

    public List<String> claimDue(int limit)
    {
        Set<String> due = stringRedisTemplate.opsForZSet()
                .rangeByScore(ZSET_KEY, 0, System.currentTimeMillis(), 0, limit);
        if (due == null || due.isEmpty())
        {
            return Collections.emptyList();
        }
        List<String> claimed = new ArrayList<String>();
        for (String taskId : due)
        {
            Long removed = stringRedisTemplate.opsForZSet().remove(ZSET_KEY, taskId);
            if (removed != null && removed > 0)
            {
                claimed.add(taskId);
            }
        }
        return claimed;
    }

    /**
     * @return false when the attempt budget is exhausted
     */
    public boolean backoff(String taskId)
    {
        Long attempt = stringRedisTemplate.opsForValue().increment(ATTEMPT_PREFIX + taskId);
        if (attempt == null || attempt >= MAX_ATTEMPTS)
        {
            stringRedisTemplate.delete(ATTEMPT_PREFIX + taskId);
            return false;
        }
        int index = attempt.intValue();
        if (index >= BACKOFF_MS.length)
        {
            index = BACKOFF_MS.length - 1;
        }
        stringRedisTemplate.opsForZSet().add(ZSET_KEY, taskId, System.currentTimeMillis() + BACKOFF_MS[index]);
        return true;
    }

    public void ack(String taskId)
    {
        stringRedisTemplate.opsForZSet().remove(ZSET_KEY, taskId);
        stringRedisTemplate.delete(ATTEMPT_PREFIX + taskId);
    }
}
