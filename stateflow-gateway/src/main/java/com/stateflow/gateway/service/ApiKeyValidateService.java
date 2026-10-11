package com.stateflow.gateway.service;

import com.stateflow.common.core.constant.CacheConstants;
import com.stateflow.common.core.constant.SecurityConstants;
import com.stateflow.common.core.constant.ServiceNameConstants;
import com.stateflow.common.core.domain.R;
import com.stateflow.common.core.exception.ServiceException;
import com.stateflow.common.core.utils.ApiKeyUtils;
import com.stateflow.common.core.utils.StringUtils;
import com.stateflow.common.redis.service.RedisService;
import com.stateflow.gateway.config.properties.ApiKeyProperties;
import com.stateflow.system.api.model.ApiKeyInfo;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class ApiKeyValidateService
{
    private static final Logger log = LoggerFactory.getLogger(ApiKeyValidateService.class);

    private static final ParameterizedTypeReference<R<ApiKeyInfo>> RESPONSE_TYPE =
            new ParameterizedTypeReference<R<ApiKeyInfo>>()
            {
            };

    private final WebClient webClient;
    private final RedisService redisService;
    private final ApiKeyProperties apiKeyProperties;

    public ApiKeyValidateService(WebClient.Builder webClientBuilder,
                                 RedisService redisService,
                                 ApiKeyProperties apiKeyProperties)
    {
        this.webClient = webClientBuilder.build();
        this.redisService = redisService;
        this.apiKeyProperties = apiKeyProperties;
    }

    public Mono<ApiKeyInfo> validate(String apiKey, String serviceName)
    {
        if (StringUtils.isEmpty(apiKey) || StringUtils.isEmpty(serviceName))
        {
            return Mono.empty();
        }

        String cacheKey = buildCacheKey(apiKey, serviceName);
        ApiKeyInfo cached = redisService.getCacheObject(cacheKey);
        if (cached != null)
        {
            return Mono.just(cached);
        }

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host(ServiceNameConstants.AUTH_SERVICE)
                        .path("/apiKey/validate")
                        .queryParam("service", serviceName)
                        .build())
                .header(SecurityConstants.API_KEY_HEADER, apiKey)
                .header(SecurityConstants.FROM_SOURCE, SecurityConstants.INNER)
                .retrieve()
                .bodyToMono(RESPONSE_TYPE)
                .flatMap(response -> {
                    if (response == null || R.SUCCESS != response.getCode() || response.getData() == null)
                    {
                        String msg = (response != null && StringUtils.isNotEmpty(response.getMsg()))
                                ? response.getMsg()
                                : "API密钥无效或已过期";
                        return Mono.error(new ServiceException(msg));
                    }
                    ApiKeyInfo info = response.getData();
                    long expireMinutes = apiKeyProperties.getCacheExpireMinutes() > 0
                            ? apiKeyProperties.getCacheExpireMinutes()
                            : CacheConstants.API_KEY_CACHE_EXPIRE_MINUTES;
                    redisService.setCacheObject(cacheKey, info, expireMinutes, TimeUnit.MINUTES);
                    return Mono.just(info);
                })
                .onErrorMap(ex -> {
                    if (ex instanceof ServiceException)
                    {
                        return ex;
                    }
                    log.error("API密钥校验调用鉴权服务失败, service={}", serviceName, ex);
                    return new ServiceException(buildErrorMessage(ex));
                });
    }

    private String buildErrorMessage(Throwable ex)
    {
        if (ex instanceof WebClientResponseException)
        {
            WebClientResponseException webEx = (WebClientResponseException) ex;
            String body = webEx.getResponseBodyAsString();
            if (StringUtils.isNotEmpty(body))
            {
                return "API密钥校验失败: HTTP " + webEx.getStatusCode().value() + " - " + body;
            }
            return "API密钥校验失败: HTTP " + webEx.getStatusCode().value() + " - " + webEx.getStatusText();
        }
        String detail = StringUtils.isNotEmpty(ex.getMessage()) ? ex.getMessage() : ex.getClass().getSimpleName();
        return "API密钥校验失败: " + detail;
    }

    private String buildCacheKey(String apiKey, String serviceName)
    {
        return CacheConstants.API_KEY_CACHE_KEY
                + serviceName
                + ":"
                + ApiKeyUtils.sha256Hex(apiKey.trim());
    }
}
