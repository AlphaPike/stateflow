package com.stateflow.file.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云 OSS 配置（凭证与公共参数；业务 Bucket 由调用方传入）
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun.oss")
@ConditionalOnProperty(prefix = "aliyun.oss", name = "endpoint")
public class AliyunOssConfig {

    /**
     * Endpoint，如 https://oss-cn-hangzhou.aliyuncs.com
     */
    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    /**
     * 默认对象前缀，如 upload/（调用方未传 folderPrefix 时使用）
     */
    private String defaultPrefix = "upload/";

    /**
     * 已绑定到 Bucket 的自定义域名（可选），如 https://oss.example.com。
     * 用于访问地址 url，以及预签名 PUT 地址 presignedUrl 的主机。
     * 不配则按 bucket.endpoint 拼装。
     */
    private String domain;

    /**
     * 预签名有效期（秒）
     */
    private long expireSeconds = 3600L;

    /**
     * 单次预签名最大生成数量
     */
    private int maxPresignCount = 100;

    @Bean(destroyMethod = "shutdown")
    public OSS aliyunOssClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

}
