package com.stateflow.file.controller;

import com.stateflow.common.core.constant.SecurityConstants;
import com.stateflow.common.core.domain.R;
import com.stateflow.common.core.exception.InnerAuthException;
import com.stateflow.common.core.utils.ServletUtils;
import com.stateflow.common.core.utils.StringUtils;
import com.stateflow.file.domain.vo.MinioToOssSyncResultVo;
import com.stateflow.file.service.IMinioToOssSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

/**
 * MinIO → OSS 同步（仅测试使用）
 */
@RestController
@RequestMapping("/test/minio-to-oss")
@Validated
@ConditionalOnBean(IMinioToOssSyncService.class)
public class MinioToOssSyncController {

    private static final Logger log = LoggerFactory.getLogger(MinioToOssSyncController.class);

    @Resource
    private IMinioToOssSyncService minioToOssSyncService;

    /**
     * 将 MinIO 配置 bucket 中全部对象同步到指定 OSS bucket（objectKey 不变）。
     * <p>
     * 仅内部调用；耗时可能很长，请在测试环境使用。
     *
     * @param targetBucket OSS 目标 bucket
     * @param prefix       可选，仅同步此前缀下对象
     * @param overwrite    是否覆盖 OSS 已存在对象，默认 false（跳过）
     */
    @PostMapping("/sync")
    public R<MinioToOssSyncResultVo> sync(
            @RequestParam("targetBucket") @NotBlank(message = "targetBucket不能为空") String targetBucket,
            @RequestParam(value = "prefix", required = false) String prefix,
            @RequestParam(value = "overwrite", defaultValue = "false") boolean overwrite) {
        try {
            MinioToOssSyncResultVo result = minioToOssSyncService.syncAllToOss(targetBucket, prefix, overwrite);
            return R.ok(result);
        } catch (Exception e) {
            log.error("MinIO 同步到 OSS 失败", e);
            return R.fail(e.getMessage());
        }
    }

    private void assertInnerCall() {
        HttpServletRequest request = ServletUtils.getRequest();
        String source = request == null ? null : request.getHeader(SecurityConstants.FROM_SOURCE);
        if (!StringUtils.equals(SecurityConstants.INNER, source)) {
            throw new InnerAuthException("没有内部访问权限，不允许访问");
        }
    }
}
