package com.stateflow.file.service;

import com.alibaba.nacos.common.utils.IoUtils;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.stateflow.common.core.utils.StringUtils;
import com.stateflow.common.core.utils.file.FileTypeUtils;
import com.stateflow.common.core.utils.file.FileUtils;
import com.stateflow.common.core.utils.uuid.UUID;
import com.stateflow.file.config.AliyunOssConfig;
import com.stateflow.system.api.domain.SysFile;
import com.stateflow.system.api.domain.oss.OssPresignForm;
import com.stateflow.system.api.domain.oss.OssPresignResultVo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 阿里云 OSS 文件存储（Bucket 由调用方传入）
 */
@Service("aliyunOss")
@ConditionalOnProperty(prefix = "aliyun.oss", name = "endpoint")
public class AliyunOssSysFileServiceImpl implements IAliyunOssFileService {

    @Resource
    private AliyunOssConfig ossConfig;

    @Resource
    private OSS aliyunOssClient;

    @Override
    public SysFile uploadFile(String bucket, MultipartFile file) throws Exception {
        String resolvedBucket = requireBucket(bucket);
        InputStream inputStream = null;
        try {
            String extension = FileTypeUtils.getExtension(file);
            String objectKey = buildObjectKey(ossConfig.getDefaultPrefix(), extension);

            inputStream = file.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            if (StringUtils.isNotEmpty(file.getContentType())) {
                metadata.setContentType(file.getContentType());
            }
            PutObjectRequest request = new PutObjectRequest(resolvedBucket, objectKey, inputStream, metadata);
            aliyunOssClient.putObject(request);

            SysFile sysFile = new SysFile();
            sysFile.setName(FileUtils.getName(objectKey));
            sysFile.setRelativeUrl(objectKey);
            sysFile.setUrl(buildAccessUrl(resolvedBucket, objectKey));
            return sysFile;
        } catch (Exception e) {
            throw new RuntimeException("Aliyun OSS Failed to upload file", e);
        } finally {
            IoUtils.closeQuietly(inputStream);
        }
    }

    @Override
    public void deleteFile(String bucket, String fileUrl) throws Exception {
        String resolvedBucket = requireBucket(bucket);
        try {
            String objectKey = resolveObjectKey(resolvedBucket, fileUrl);
            if (StringUtils.isEmpty(objectKey)) {
                throw new IllegalArgumentException("无法解析 OSS 对象路径: " + fileUrl);
            }
            aliyunOssClient.deleteObject(resolvedBucket, objectKey);
        } catch (Exception e) {
            throw new RuntimeException("Aliyun OSS Failed to delete file", e);
        }
    }

    @Override
    public String getRootPath(String bucket) {
        String resolvedBucket = requireBucket(bucket);
        String domain = normalizeDomain(resolveAccessDomain(resolvedBucket));
        return domain.endsWith("/") ? domain : domain + "/";
    }

    @Override
    public OssPresignResultVo generatePresignedUrls(OssPresignForm form) {
        if (form == null || form.getItems() == null || form.getItems().isEmpty()) {
            throw new IllegalArgumentException("文件后缀列表不能为空");
        }
        String bucket = requireBucket(form.getBucket());
        String folderPrefix = normalizePrefix(
                StringUtils.isNotEmpty(form.getFolderPrefix())
                        ? form.getFolderPrefix()
                        : ossConfig.getDefaultPrefix()
        );

        int total = 0;
        for (OssPresignForm.OssPresignExtensionItem item : form.getItems()) {
            if (item == null || StringUtils.isEmpty(item.getExtension())) {
                throw new IllegalArgumentException("文件后缀不能为空");
            }
            if (item.getCount() < 1) {
                throw new IllegalArgumentException("文件数量至少为1");
            }
            total += item.getCount();
        }
        int maxCount = ossConfig.getMaxPresignCount() <= 0 ? 100 : ossConfig.getMaxPresignCount();
        if (total > maxCount) {
            throw new IllegalArgumentException("单次最多生成 " + maxCount + " 个预签名地址");
        }

        long expireSeconds = ossConfig.getExpireSeconds() <= 0 ? 3600L : ossConfig.getExpireSeconds();
        Date expiration = new Date(System.currentTimeMillis() + expireSeconds * 1000L);

        List<OssPresignResultVo.OssPresignItemVo> resultItems = new ArrayList<>(total);
        for (OssPresignForm.OssPresignExtensionItem item : form.getItems()) {
            String extension = normalizeExtension(item.getExtension());
            for (int i = 0; i < item.getCount(); i++) {
                String objectKey = buildObjectKey(folderPrefix, extension);
                GeneratePresignedUrlRequest request =
                        new GeneratePresignedUrlRequest(bucket, objectKey, HttpMethod.PUT);
                request.setExpiration(expiration);
                request.setContentType("application/octet-stream");
                URL signedUrl = aliyunOssClient.generatePresignedUrl(request);

                OssPresignResultVo.OssPresignItemVo vo = new OssPresignResultVo.OssPresignItemVo();
                vo.setExtension(extension);
                vo.setObjectKey(objectKey);
                vo.setPresignedUrl(toCustomDomainPresignedUrl(signedUrl));
                vo.setHttpMethod(HttpMethod.PUT.name());
                vo.setRelativeUrl(objectKey);
                vo.setUrl(buildAccessUrl(bucket, objectKey));
                vo.setExpireAt(expiration.getTime());
                resultItems.add(vo);
            }
        }

        OssPresignResultVo result = new OssPresignResultVo();
        result.setBucket(bucket);
        result.setFolderPrefix(folderPrefix);
        result.setItems(resultItems);
        return result;
    }

    private String requireBucket(String bucket) {
        if (StringUtils.isEmpty(bucket)) {
            throw new IllegalArgumentException("bucket不能为空");
        }
        return bucket.trim();
    }

    private String buildObjectKey(String prefix, String extension) {
        String normalizedPrefix = normalizePrefix(prefix);
        String uuid = UUID.randomUUID().toString(true);
        String ext = normalizeExtension(extension);
        return normalizedPrefix + uuid + "." + ext;
    }

    private String normalizePrefix(String prefix) {
        if (StringUtils.isEmpty(prefix)) {
            return "";
        }
        String value = prefix.trim().replace("\\", "/");
        while (value.startsWith("/")) {
            value = value.substring(1);
        }
        if (value.isEmpty()) {
            return "";
        }
        if (!value.endsWith("/")) {
            value = value + "/";
        }
        return value;
    }

    private String normalizeExtension(String extension) {
        String value = extension.trim().toLowerCase();
        if (value.startsWith(".")) {
            value = value.substring(1);
        }
        if (value.isEmpty() || value.contains("/") || value.contains("\\")) {
            throw new IllegalArgumentException("非法文件后缀: " + extension);
        }
        return value;
    }

    /**
     * 预签名仍用官方 endpoint 计算签名，再把 host 换成已绑定的自定义域名。
     * 阿里云 V1 签名不含 Host，只替换域名、保留 path/query 时签名仍然有效。
     */
    private String toCustomDomainPresignedUrl(URL signedUrl) {
        if (signedUrl == null) {
            return null;
        }
        String original = signedUrl.toString();
        String customOrigin = resolveCustomOrigin();
        if (StringUtils.isEmpty(customOrigin)) {
            return original;
        }
        String originalOrigin = signedUrl.getProtocol() + "://" + signedUrl.getAuthority();
        if (!original.startsWith(originalOrigin)) {
            return original;
        }
        return customOrigin + original.substring(originalOrigin.length());
    }

    /**
     * @return 带协议、无末尾斜杠的自定义域名；未配置则返回 null
     */
    private String resolveCustomOrigin() {
        if (StringUtils.isEmpty(ossConfig.getDomain())) {
            return null;
        }
        String value = normalizeDomain(ossConfig.getDomain());
        if (value.isEmpty()) {
            return null;
        }
        if (!value.startsWith("http://") && !value.startsWith("https://")) {
            value = "https://" + value;
        }
        return value;
    }

    private String buildAccessUrl(String bucket, String objectKey) {
        String domain = normalizeDomain(resolveAccessDomain(bucket));
        if (domain.endsWith("/")) {
            return domain + objectKey;
        }
        return domain + "/" + objectKey;
    }

    private String resolveAccessDomain(String bucket) {
        if (StringUtils.isNotEmpty(ossConfig.getDomain())) {
            return ossConfig.getDomain().trim();
        }
        String endpoint = ossConfig.getEndpoint().trim();
        String host = endpoint;
        if (host.startsWith("https://")) {
            host = host.substring("https://".length());
            return "https://" + bucket + "." + host;
        }
        if (host.startsWith("http://")) {
            host = host.substring("http://".length());
            return "http://" + bucket + "." + host;
        }
        return "https://" + bucket + "." + host;
    }

    private String normalizeDomain(String domain) {
        if (domain == null) {
            return "";
        }
        String value = domain.trim();
        while (value.endsWith("/")) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    private String resolveObjectKey(String bucket, String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return null;
        }
        String value = fileUrl.trim();
        String rootPath = getRootPath(bucket);
        if (value.startsWith(rootPath)) {
            return value.substring(rootPath.length());
        }
        String domain = normalizeDomain(resolveAccessDomain(bucket));
        if (value.startsWith(domain + "/")) {
            return value.substring(domain.length() + 1);
        }
        if (!value.contains("://")) {
            while (value.startsWith("/")) {
                value = value.substring(1);
            }
            return value;
        }
        int idx = value.indexOf(bucket + "/");
        if (idx >= 0) {
            return value.substring(idx + bucket.length() + 1);
        }
        return null;
    }
}
