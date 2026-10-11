package com.stateflow.common.core.aspect;

import com.stateflow.common.core.annotation.ExecutionTime;
import com.stateflow.common.core.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 搭配注解 {@link ExecutionTime} 使用
 */
@Aspect
@Component
public class ExecutionTimeAspect {

    private static final Logger log = LoggerFactory.getLogger(ExecutionTimeAspect.class);

    @Around("@annotation(executionTime)")
    public Object around(ProceedingJoinPoint joinPoint, ExecutionTime executionTime) throws Throwable {
        long startNanos = System.nanoTime();
        String label = resolveLabel(joinPoint, executionTime);
        try {
            return joinPoint.proceed();
        } finally {
            long costMs = (System.nanoTime() - startNanos) / 1_000_000L;
            if (costMs >= executionTime.slowThresholdMs()) {
                log.warn("{} 函数执行事件 {} ms", label, costMs);
            } else if (log.isDebugEnabled()) {
                log.debug("{} 函数执行事件 {} ms", label, costMs);
            } else {
                log.info("{} 函数执行事件 {} ms", label, costMs);
            }
        }
    }

    private String resolveLabel(ProceedingJoinPoint joinPoint, ExecutionTime executionTime) {
        if (StringUtils.isNotBlank(executionTime.value())) {
            return executionTime.value();
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getDeclaringClass().getSimpleName() + "." + method.getName();
    }
}
