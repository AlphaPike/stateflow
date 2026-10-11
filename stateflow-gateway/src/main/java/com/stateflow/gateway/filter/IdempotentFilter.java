package com.stateflow.gateway.filter;

import com.stateflow.common.core.constant.SecurityConstants;
import com.stateflow.common.core.utils.StringUtils;
import com.stateflow.common.redis.service.RedisService;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class IdempotentFilter implements GlobalFilter, Ordered {
    @Resource
    private RedisService redisService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 1. 只拦截写请求（POST/PUT），查询请求通常不需要幂等
        HttpMethod method = request.getMethod();
        if (method == HttpMethod.GET || method == HttpMethod.DELETE) {
            return chain.filter(exchange);
        }

        // 2. 获取请求头中的唯一标识
        String requestId = request.getHeaders().getFirst(SecurityConstants.IDEMPOTENT_HEADER);

        // 如果前端没传，根据业务决定是直接放行还是报错
        if (StringUtils.isBlank(requestId)) {
            return chain.filter(exchange);
        }

        // 3. 原子性占位 (SETNX)
        // 设置 5 分钟过期时间，防止业务异常导致 Key 永久堆积
        String key = "idempotent:" + requestId;
        Boolean success = redisService.setIfAbsent(key, requestId, 1L, TimeUnit.MINUTES);

        if (Boolean.TRUE.equals(success)) {
            // 第一次请求，放行
            return chain.filter(exchange);
        } else {
            // 4. 重复请求，直接拦截并返回错误响应
            exchange.getResponse().setStatusCode(HttpStatus.CONFLICT);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap("请勿发起重复的请求".getBytes());
            return exchange.getResponse().writeWith(Mono.just(buffer));
        }
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
