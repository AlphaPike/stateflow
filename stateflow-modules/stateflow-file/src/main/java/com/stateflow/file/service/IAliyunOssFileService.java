package com.stateflow.file.service;

import com.stateflow.system.api.domain.SysFile;
import com.stateflow.system.api.domain.oss.OssPresignForm;
import com.stateflow.system.api.domain.oss.OssPresignResultVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 阿里云 OSS 文件服务（Bucket 由调用方指定，接口仅内部调用）
 */
public interface IAliyunOssFileService {

    SysFile uploadFile(String bucket, MultipartFile file) throws Exception;

    void deleteFile(String bucket, String fileUrl) throws Exception;

    String getRootPath(String bucket);

    /**
     * 按后缀批量生成 PUT 预签名上传地址（form.bucket 必填）
     */
    OssPresignResultVo generatePresignedUrls(OssPresignForm form);
}
