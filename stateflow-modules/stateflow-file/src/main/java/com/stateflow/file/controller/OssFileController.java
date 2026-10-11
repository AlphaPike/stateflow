package com.stateflow.file.controller;

import com.stateflow.common.core.constant.SecurityConstants;
import com.stateflow.common.core.domain.R;
import com.stateflow.common.core.exception.InnerAuthException;
import com.stateflow.common.core.utils.ServletUtils;
import com.stateflow.common.core.utils.StringUtils;
import com.stateflow.common.core.utils.file.FileUtils;
import com.stateflow.file.service.IAliyunOssFileService;
import com.stateflow.system.api.domain.SysFile;
import com.stateflow.system.api.domain.oss.OssPresignForm;
import com.stateflow.system.api.domain.oss.OssPresignResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

/**
 * 阿里云 OSS 文件接口（仅内部服务调用，Bucket 由调用方指定）
 */
@RestController
@RequestMapping("/oss")
@Validated
@ConditionalOnProperty(prefix = "aliyun.oss", name = "endpoint")
public class OssFileController {

    private static final Logger log = LoggerFactory.getLogger(OssFileController.class);

    @Resource(name = "aliyunOss")
    private IAliyunOssFileService aliyunOssFileService;

    @PostMapping("/upload")
    public R<SysFile> upload(@RequestParam("bucket") @NotBlank(message = "bucket不能为空") String bucket,
                             MultipartFile file) {
        assertInnerCall();
        try {
            return R.ok(aliyunOssFileService.uploadFile(bucket, file));
        } catch (Exception e) {
            log.error("OSS上传文件失败", e);
            return R.fail(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public R<Boolean> delete(@RequestParam("bucket") @NotBlank(message = "bucket不能为空") String bucket,
                             @RequestParam("fileUrl") String fileUrl) {
        assertInnerCall();
        try {
            if (!FileUtils.validateFilePath(fileUrl)) {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许删除。 ", fileUrl));
            }
            aliyunOssFileService.deleteFile(bucket, fileUrl);
            return R.ok();
        } catch (Exception e) {
            log.error("OSS删除文件失败", e);
            return R.fail(e.getMessage());
        }
    }

    @GetMapping("/rootPath")
    public R<String> rootPath(@RequestParam("bucket") @NotBlank(message = "bucket不能为空") String bucket) {
        assertInnerCall();
        return R.ok(aliyunOssFileService.getRootPath(bucket));
    }

    @PostMapping("/presign")
    public R<OssPresignResultVo> presign(@Validated @RequestBody OssPresignForm form) {
        assertInnerCall();
        try {
            return R.ok(aliyunOssFileService.generatePresignedUrls(form));
        } catch (Exception e) {
            log.error("OSS生成预签名失败", e);
            return R.fail(e.getMessage());
        }
    }

    /**
     * 与 {@code @InnerAuth} 一致：仅允许携带 from-source=inner 的服务间调用。
     * 网关会剥离外部请求中的该头，防止伪造。
     */
    private void assertInnerCall() {
        HttpServletRequest request = ServletUtils.getRequest();
        String source = request == null ? null : request.getHeader(SecurityConstants.FROM_SOURCE);
        if (!StringUtils.equals(SecurityConstants.INNER, source)) {
            throw new InnerAuthException("没有内部访问权限，不允许访问");
        }
    }
}
