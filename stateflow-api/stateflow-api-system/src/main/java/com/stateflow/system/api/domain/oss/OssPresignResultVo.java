package com.stateflow.system.api.domain.oss;

import java.io.Serializable;
import java.util.List;

/**
 * OSS 预签名上传结果
 */
public class OssPresignResultVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String bucket;

    private String folderPrefix;

    private List<OssPresignItemVo> items;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getFolderPrefix() {
        return folderPrefix;
    }

    public void setFolderPrefix(String folderPrefix) {
        this.folderPrefix = folderPrefix;
    }

    public List<OssPresignItemVo> getItems() {
        return items;
    }

    public void setItems(List<OssPresignItemVo> items) {
        this.items = items;
    }

    public static class OssPresignItemVo implements Serializable {

        private static final long serialVersionUID = 1L;

        private String extension;

        private String objectKey;

        /** PUT 预签名地址，前端直传使用 */
        private String presignedUrl;

        private String httpMethod;

        /** 上传成功后的访问地址 */
        private String url;

        private String relativeUrl;

        /** 过期时间，epoch millis */
        private Long expireAt;

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public String getObjectKey() {
            return objectKey;
        }

        public void setObjectKey(String objectKey) {
            this.objectKey = objectKey;
        }

        public String getPresignedUrl() {
            return presignedUrl;
        }

        public void setPresignedUrl(String presignedUrl) {
            this.presignedUrl = presignedUrl;
        }

        public String getHttpMethod() {
            return httpMethod;
        }

        public void setHttpMethod(String httpMethod) {
            this.httpMethod = httpMethod;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getRelativeUrl() {
            return relativeUrl;
        }

        public void setRelativeUrl(String relativeUrl) {
            this.relativeUrl = relativeUrl;
        }

        public Long getExpireAt() {
            return expireAt;
        }

        public void setExpireAt(Long expireAt) {
            this.expireAt = expireAt;
        }
    }
}
