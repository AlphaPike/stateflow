package com.stateflow.system.api.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * API Key 校验结果
 */
@Data
public class ApiKeyInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String apiKeyId;

    private String keyPrefix;

    private String allowedServices;

    private LocalDateTime expireTime;
}
