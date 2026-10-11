package com.stateflow.file.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.stateflow.common.core.utils.StringUtils;
import com.stateflow.file.config.MinioConfig;
import com.stateflow.file.domain.vo.MinioToOssSyncResultVo;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.messages.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * MinIO → 阿里云 OSS 全量同步（仅测试环境使用）
 */
@Service
@ConditionalOnBean({MinioClient.class, OSS.class})
public class MinioToOssSyncServiceImpl implements IMinioToOssSyncService {

    private static final Logger log = LoggerFactory.getLogger(MinioToOssSyncServiceImpl.class);

    /** 失败 key 列表最多回传数量，避免响应过大 */
    private static final int MAX_FAILED_KEYS = 100;

    @Resource
    private MinioClient minioClient;

    @Resource
    private MinioConfig minioConfig;

    @Resource
    private OSS aliyunOssClient;

    @Override
    public MinioToOssSyncResultVo syncAllToOss(String targetBucket, String prefix, boolean overwrite) {
        if (StringUtils.isEmpty(targetBucket)) {
            throw new IllegalArgumentException("targetBucket不能为空");
        }
        if (StringUtils.isEmpty(minioConfig.getBucketName())) {
            throw new IllegalStateException("未配置 minio.bucketName");
        }

        String sourceBucket = minioConfig.getBucketName().trim();
        String resolvedTarget = targetBucket.trim();
        String normalizedPrefix = normalizePrefix(prefix);

        MinioToOssSyncResultVo result = new MinioToOssSyncResultVo();
        result.setSourceBucket(sourceBucket);
        result.setTargetBucket(resolvedTarget);
        result.setPrefix(normalizedPrefix);
        result.setFailedKeys(new ArrayList<>());

        ListObjectsArgs.Builder listBuilder = ListObjectsArgs.builder()
                .bucket(sourceBucket)
                .recursive(true);
        if (StringUtils.isNotEmpty(normalizedPrefix)) {
            listBuilder.prefix(normalizedPrefix);
        }

        Iterable<Result<Item>> objects = minioClient.listObjects(listBuilder.build());
        for (Result<Item> itemResult : objects) {
            Item item;
            try {
                item = itemResult.get();
            } catch (Exception e) {
                log.error("列举 MinIO 对象失败", e);
                result.setFailed(result.getFailed() + 1);
                addFailedKey(result, "<list-error>:" + e.getMessage());
                continue;
            }
            if (item == null || item.isDir() || StringUtils.isEmpty(item.objectName())) {
                continue;
            }

            String objectKey = item.objectName();
            result.setTotal(result.getTotal() + 1);
            try {
                if (!overwrite && aliyunOssClient.doesObjectExist(resolvedTarget, objectKey)) {
                    result.setSkipped(result.getSkipped() + 1);
                    continue;
                }
                syncOneObject(sourceBucket, resolvedTarget, objectKey);
                result.setSuccess(result.getSuccess() + 1);
            } catch (Exception e) {
                log.error("同步对象失败, objectKey={}", objectKey, e);
                result.setFailed(result.getFailed() + 1);
                addFailedKey(result, objectKey);
            }
        }
        return result;
    }

    private void syncOneObject(String sourceBucket, String targetBucket, String objectKey) throws Exception {
        StatObjectResponse stat = minioClient.statObject(
                StatObjectArgs.builder().bucket(sourceBucket).object(objectKey).build()
        );
        try (InputStream inputStream = minioClient.getObject(
                GetObjectArgs.builder().bucket(sourceBucket).object(objectKey).build()
        )) {
            ObjectMetadata metadata = new ObjectMetadata();
            if (stat.size() >= 0) {
                metadata.setContentLength(stat.size());
            }
            if (StringUtils.isNotEmpty(stat.contentType())) {
                metadata.setContentType(stat.contentType());
            }
            PutObjectRequest request = new PutObjectRequest(targetBucket, objectKey, inputStream, metadata);
            aliyunOssClient.putObject(request);
        }
    }

    private String normalizePrefix(String prefix) {
        if (StringUtils.isEmpty(prefix)) {
            return "";
        }
        String value = prefix.trim().replace("\\", "/");
        while (value.startsWith("/")) {
            value = value.substring(1);
        }
        return value;
    }

    private void addFailedKey(MinioToOssSyncResultVo result, String key) {
        List<String> failedKeys = result.getFailedKeys();
        if (failedKeys == null) {
            failedKeys = new ArrayList<>();
            result.setFailedKeys(failedKeys);
        }
        if (failedKeys.size() < MAX_FAILED_KEYS) {
            failedKeys.add(key);
        }
    }
}
