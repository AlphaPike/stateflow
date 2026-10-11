package com.stateflow.file.service;

import com.stateflow.file.domain.vo.MinioToOssSyncResultVo;

/**
 * MinIO → 阿里云 OSS 同步（仅测试）
 */
public interface IMinioToOssSyncService {

    /**
     * 将 MinIO 默认 bucket 中的对象同步到指定 OSS bucket，objectKey 保持一致。
     *
     * @param targetBucket OSS 目标 bucket（必填）
     * @param prefix       仅同步此前缀下的对象，可空表示全部
     * @param overwrite    true=覆盖已存在对象；false=已存在则跳过
     */
    MinioToOssSyncResultVo syncAllToOss(String targetBucket, String prefix, boolean overwrite);
}
