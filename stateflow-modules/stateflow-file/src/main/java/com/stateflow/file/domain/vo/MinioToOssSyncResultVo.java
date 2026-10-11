package com.stateflow.file.domain.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * MinIO → OSS 同步结果（测试用）
 */
public class MinioToOssSyncResultVo {

    private String sourceBucket;

    private String targetBucket;

    private String prefix;

    private int total;

    private int success;

    private int skipped;

    private int failed;

    private List<String> failedKeys = new ArrayList<>();

    public String getSourceBucket() {
        return sourceBucket;
    }

    public void setSourceBucket(String sourceBucket) {
        this.sourceBucket = sourceBucket;
    }

    public String getTargetBucket() {
        return targetBucket;
    }

    public void setTargetBucket(String targetBucket) {
        this.targetBucket = targetBucket;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public List<String> getFailedKeys() {
        return failedKeys;
    }

    public void setFailedKeys(List<String> failedKeys) {
        this.failedKeys = failedKeys;
    }
}
