package com.stateflow.system.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import com.stateflow.common.core.constant.SecurityConstants;
import com.stateflow.common.core.constant.ServiceNameConstants;
import com.stateflow.common.core.domain.R;
import com.stateflow.system.api.factory.RemoteApiKeyFallbackFactory;
import com.stateflow.system.api.model.ApiKeyInfo;

/**
 * API Key 服务
 */
@FeignClient(contextId = "remoteApiKeyService", value = ServiceNameConstants.SYSTEM_SERVICE,
        fallbackFactory = RemoteApiKeyFallbackFactory.class)
public interface RemoteApiKeyService
{
    @GetMapping("/apiKey/validate")
    R<ApiKeyInfo> validateApiKey(@RequestHeader(SecurityConstants.API_KEY_HEADER) String apiKey,
                                 @RequestParam("service") String service,
                                 @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
