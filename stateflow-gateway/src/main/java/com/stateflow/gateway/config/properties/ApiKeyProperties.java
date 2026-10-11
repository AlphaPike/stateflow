package com.stateflow.gateway.config.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import com.stateflow.common.core.constant.ServiceNameConstants;
import com.stateflow.common.core.utils.StringUtils;

/**
 * API Key 鉴权配置
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "security.api-key")
public class ApiKeyProperties
{
    /** 是否启用 API Key 鉴权 */
    private boolean enabled = true;

    /** 请求头名称 */
    private String header = "X-Api-Key";

    /**
     * 路径与服务标识映射。
     * <p>同一网关下不同前缀可对应不同服务，校验 API Key 的 allowedServices 时按匹配到的 serviceName 校验。
     */
    private List<PathMapping> mappings = new ArrayList<>();

    /** Redis 缓存有效期（分钟） */
    private long cacheExpireMinutes = 60;

    public ApiKeyProperties()
    {
        mappings.add(new PathMapping("/creator/**", ServiceNameConstants.AISENCE_CREATOR_SERVICE));
        mappings.add(new PathMapping("/agent/**", ServiceNameConstants.AISENCE_AGENT_SERVICE));
    }

    /**
     * 兼容旧逻辑：返回所有需要 API Key 鉴权的路径模式
     */
    public List<String> getPaths()
    {
        if (mappings == null || mappings.isEmpty())
        {
            return new ArrayList<>();
        }
        return mappings.stream()
                .map(PathMapping::getPath)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList());
    }

    /**
     * 按请求路径解析应对应的服务标识；未匹配返回 {@code null}
     */
    public String resolveServiceName(String url)
    {
        if (StringUtils.isEmpty(url) || mappings == null || mappings.isEmpty())
        {
            return null;
        }
        for (PathMapping mapping : mappings)
        {
            if (mapping == null || StringUtils.isEmpty(mapping.getPath()) || StringUtils.isEmpty(mapping.getServiceName()))
            {
                continue;
            }
            if (StringUtils.isMatch(mapping.getPath(), url))
            {
                return mapping.getServiceName();
            }
        }
        return null;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PathMapping
    {
        /** 路径匹配模式，如 /creator/**、/agent/** */
        private String path;

        /** 对应服务标识，需与 API Key 的 allowedServices 一致 */
        private String serviceName;
    }
}
