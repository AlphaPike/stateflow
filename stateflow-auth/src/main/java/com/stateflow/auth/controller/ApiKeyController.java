package com.stateflow.auth.controller;

import com.stateflow.common.core.constant.SecurityConstants;
import com.stateflow.common.core.domain.R;
import com.stateflow.common.security.annotation.InnerAuth;
import com.stateflow.system.api.RemoteApiKeyService;
import com.stateflow.system.api.model.ApiKeyInfo;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API Key 校验
 */
@RestController
@RequestMapping("/apiKey")
public class ApiKeyController
{
    @Resource
    private RemoteApiKeyService remoteApiKeyService;

    @InnerAuth
    @GetMapping("/validate")
    public R<ApiKeyInfo> validate(@RequestHeader(SecurityConstants.API_KEY_HEADER) String apiKey,
                                    @RequestParam("service") String service)
    {
        return remoteApiKeyService.validateApiKey(apiKey, service, SecurityConstants.INNER);
    }
}
