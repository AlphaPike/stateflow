package com.stateflow.system.api.domain.oss;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * OSS 预签名上传请求
 */
public class OssPresignForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 目标 Bucket */
    @NotBlank(message = "bucket不能为空")
    private String bucket;

    /**
     * 对象前缀/文件夹；不传则使用文件服务配置默认前缀
     */
    private String folderPrefix;

    @NotEmpty(message = "文件后缀列表不能为空")
    @Valid
    private List<OssPresignExtensionItem> items;

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

    public List<OssPresignExtensionItem> getItems() {
        return items;
    }

    public void setItems(List<OssPresignExtensionItem> items) {
        this.items = items;
    }

    public static class OssPresignExtensionItem implements Serializable {

        private static final long serialVersionUID = 1L;

        /** 文件后缀，如 jpg / png / mp4（可带或不带点） */
        @NotBlank(message = "文件后缀不能为空")
        private String extension;

        /** 该后缀需要生成的预签名数量 */
        @Min(value = 1, message = "文件数量至少为1")
        private int count;

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
