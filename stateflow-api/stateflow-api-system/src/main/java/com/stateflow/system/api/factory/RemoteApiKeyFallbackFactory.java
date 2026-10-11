package com.stateflow.system.api.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import com.stateflow.common.core.domain.R;
import com.stateflow.system.api.RemoteApiKeyService;
import com.stateflow.system.api.model.ApiKeyInfo;

/**
 * API Key 服务降级处理
 */
@Component
public class RemoteApiKeyFallbackFactory implements FallbackFactory<RemoteApiKeyService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteApiKeyFallbackFactory.class);

    @Override
    public RemoteApiKeyService create(Throwable throwable)
    {
        log.error("API Key 服务调用失败:{}", throwable.getMessage());
        return (apiKey, service, source) -> R.fail("API Key 校验失败:" + throwable.getMessage());
    }
}
