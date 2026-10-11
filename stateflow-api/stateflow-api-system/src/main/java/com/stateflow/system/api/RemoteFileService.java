package com.stateflow.system.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.stateflow.common.core.constant.ServiceNameConstants;
import com.stateflow.common.core.constant.SecurityConstants;
import com.stateflow.common.core.domain.R;
import com.stateflow.system.api.domain.SysFile;
import com.stateflow.system.api.domain.oss.OssPresignForm;
import com.stateflow.system.api.domain.oss.OssPresignResultVo;
import com.stateflow.system.api.factory.RemoteFileFallbackFactory;

/**
 * 文件服务
 * 
 * @author stateflow
 */
@FeignClient(contextId = "remoteFileService", value = ServiceNameConstants.FILE_SERVICE, fallbackFactory = RemoteFileFallbackFactory.class)
public interface RemoteFileService
{
    /**
     * 文件根目录
     */
    @GetMapping("/rootPath")
    public R<String> rootPath();

    /**
     * 上传文件
     *
     * @param file 文件信息
     * @return 结果
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<SysFile> upload(@RequestPart(value = "file") MultipartFile file);

    /**
     * 删除文件
     *
     * @param fileUrl 文件地址
     * @return 结果
     */
    @DeleteMapping(value = "/delete", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public R<Boolean> delete(@RequestParam("fileUrl") String fileUrl);

    /**
     * 阿里云 OSS 批量预签名（内部调用）
     */
    @PostMapping(value = "/oss/presign", consumes = MediaType.APPLICATION_JSON_VALUE)
    R<OssPresignResultVo> ossPresign(@RequestBody OssPresignForm form,
                                     @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 阿里云 OSS 根路径（内部调用）
     */
    @GetMapping("/oss/rootPath")
    R<String> ossRootPath(@RequestParam("bucket") String bucket,
                          @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 阿里云 OSS 上传（内部调用）
     */
    @PostMapping(value = "/oss/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<SysFile> ossUpload(@RequestParam("bucket") String bucket,
                         @RequestPart("file") MultipartFile file,
                         @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 阿里云 OSS 删除（内部调用）
     */
    @DeleteMapping(value = "/oss/delete", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    R<Boolean> ossDelete(@RequestParam("bucket") String bucket,
                         @RequestParam("fileUrl") String fileUrl,
                         @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
