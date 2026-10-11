package com.stateflow.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import com.stateflow.common.core.constant.HttpStatus;
import com.stateflow.common.core.constant.SecurityConstants;
import com.stateflow.common.core.exception.ServiceException;
import com.stateflow.common.core.utils.ServletUtils;
import com.stateflow.common.core.utils.StringUtils;
import com.stateflow.gateway.config.properties.ApiKeyProperties;
import com.stateflow.gateway.service.ApiKeyValidateService;
import com.stateflow.system.api.model.ApiKeyInfo;
import reactor.core.publisher.Mono;

/**
 * API Key 鉴权过滤器
 */
@Component
public class ApiKeyFilter implements GlobalFilter, Ordered
{
    public static final String API_KEY_VALIDATED_ATTR = "apiKeyValidated";

    private static final Logger log = LoggerFactory.getLogger(ApiKeyFilter.class);

    @Autowired
    private ApiKeyProperties apiKeyProperties;

    @Autowired
    private ApiKeyValidateService apiKeyValidateService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        if (!apiKeyProperties.isEnabled())
        {
            return chain.filter(exchange);
        }

        String url = exchange.getRequest().getURI().getPath();
        String serviceName = apiKeyProperties.resolveServiceName(url);
        if (StringUtils.isEmpty(serviceName))
        {
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest();
        String apiKey = request.getHeaders().getFirst(resolveHeaderName());
        if (StringUtils.isEmpty(apiKey))
        {
            return unauthorizedResponse(exchange, "API密钥不能为空");
        }

        // flatMap 内 chain.filter() 返回 Mono<Void>（无 onNext），不能直接在其后 switchIfEmpty。
        // 先用 thenReturn 保留 ApiKeyInfo 信号，校验失败时再走 unauthorizedResponse。
        return apiKeyValidateService.validate(apiKey.trim(), serviceName)
                .flatMap(info -> continueWithApiKey(exchange, chain, info).thenReturn(info))
                .switchIfEmpty(Mono.defer(() -> unauthorizedResponse(exchange, "API密钥无效或已过期").then(Mono.empty())))
                .onErrorResume(ServiceException.class,
                        ex -> unauthorizedResponse(exchange, resolveErrorMessage(ex)).then(Mono.empty()))
                .then();
    }

    private String resolveErrorMessage(Throwable ex)
    {
        return StringUtils.isNotEmpty(ex.getMessage()) ? ex.getMessage() : "API密钥校验失败";
    }

    private Mono<Void> continueWithApiKey(ServerWebExchange exchange, GatewayFilterChain chain, ApiKeyInfo info)
    {
        ServerHttpRequest.Builder mutate = exchange.getRequest().mutate();
        addHeader(mutate, SecurityConstants.API_KEY_AUTH, "true");
        addHeader(mutate, SecurityConstants.API_KEY_ID, info.getApiKeyId());
        exchange.getAttributes().put(API_KEY_VALIDATED_ATTR, Boolean.TRUE);
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    private String resolveHeaderName()
    {
        return StringUtils.isEmpty(apiKeyProperties.getHeader())
                ? SecurityConstants.API_KEY_HEADER
                : apiKeyProperties.getHeader();
    }

    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value)
    {
        if (value == null)
        {
            return;
        }
        mutate.header(name, ServletUtils.urlEncode(value.toString()));
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg)
    {
        log.error("[API Key鉴权失败]请求路径:{},错误信息:{}", exchange.getRequest().getPath(), msg);
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public int getOrder()
    {
        return -250;
    }
}
